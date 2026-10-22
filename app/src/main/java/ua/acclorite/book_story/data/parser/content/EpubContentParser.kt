/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.parser.content

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import ua.acclorite.book_story.R
import ua.acclorite.book_story.core.data.ExtensionsData
import ua.acclorite.book_story.core.helpers.concurrentMap
import ua.acclorite.book_story.core.helpers.ifTrue
import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.core.log.logI
import ua.acclorite.book_story.core.log.logW
import ua.acclorite.book_story.core.ui.UIText
import ua.acclorite.book_story.data.parser.content.document.JsoupDocumentParser
import ua.acclorite.book_story.data.storage.file.CachedFile
import ua.acclorite.book_story.domain.model.book.NodeContent
import ua.acclorite.book_story.domain.model.file.FilePath
import ua.acclorite.book_story.domain.model.file.ImageMetadata
import java.io.File
import java.io.InputStream
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.concurrent.atomic.AtomicInteger
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import javax.inject.Inject

private const val TAG = "EpubContentParser"

class EpubContentParser @Inject constructor(
    private val jsoupDocumentParser: JsoupDocumentParser
) : ContentParser {

    override fun parse(
        cachedFile: CachedFile,
        cacheImage: suspend (filename: String, inputStream: InputStream) -> ImageMetadata?,
        cacheNodes: suspend (List<NodeContent>) -> FilePath?
    ): Flow<ContentParserState> = flow {
        try {
            logI(TAG, "Started EPUB parsing of [${cachedFile.name}].")
            emit(ContentParserState.Progress(0.0f))

            val rawFile = cachedFile.rawFile
            if (rawFile == null || !rawFile.exists() || !rawFile.canRead()) {
                emit(ContentParserState.Error(UIText.StringResource(R.string.error_could_not_access_file)))
                logE(TAG, "Could not access raw file in EPUB.")
                return@flow
            }

            currentCoroutineContext().ensureActive()

            ZipFile(rawFile).use { zipFile ->
                val zipEntries = zipFile.entries().toList()
                val opfEntry = zipEntries.find { entry ->
                    entry.name.endsWith(".opf", ignoreCase = true)
                }

                val imageEntries = getImageEntries(zipFile, zipEntries, cacheImage)
                val sortedDocumentEntries = getSortedDocumentEntries(zipFile, zipEntries, opfEntry)

                if (sortedDocumentEntries.isEmpty()) {
                    emit(ContentParserState.Error(UIText.StringResource(R.string.error_empty_file)))
                    logE(TAG, "Could not extract nodes from EPUB (sortedDocumentEntries is empty).")
                    return@flow
                }

                currentCoroutineContext().ensureActive()

                logI(TAG, "OPF entry: ${opfEntry?.name ?: "no .opf entry"}")
                logI(TAG, "Image entries, size: ${imageEntries.size}")
                logI(TAG, "Sorted document entries, size: ${sortedDocumentEntries.size}")

                val parserFlow = channelFlow {
                    val parsedCount = AtomicInteger(0)
                    val totalEntries = sortedDocumentEntries.size.toFloat()

                    val nodePaths = sortedDocumentEntries.concurrentMap(
                        transform = { entry ->
                            parseDocumentEntry(
                                zipFile = zipFile,
                                entry = entry,
                                imageEntries = imageEntries,
                                cacheNodes = cacheNodes
                            ).also {
                                val current = parsedCount.incrementAndGet()
                                send(ContentParserState.Progress(current / totalEntries))
                            }
                        }
                    ).filterNotNull()

                    if (nodePaths.isEmpty()) {
                        send(ContentParserState.Error(UIText.StringResource(R.string.error_empty_file)))
                        logE(TAG, "Could not extract nodes from EPUB (file is empty).")
                        return@channelFlow
                    }

                    send(ContentParserState.Success(nodePaths))
                }.flowOn(Dispatchers.IO)

                currentCoroutineContext().ensureActive()

                emitAll(parserFlow)
            }

            logI(TAG, "Successfully finished EPUB parsing.")
        } catch (c: CancellationException) {
            throw c
        } catch (e: Exception) {
            emit(
                ContentParserState.Error(
                    UIText.StringResource(
                        R.string.error_could_not_parse_file,
                        e.message ?: ""
                    )
                )
            )
            logE(TAG, "Could not extract nodes from EPUB with error: ${e.message}.")
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Parses document entry with Jsoup and persists it to Protobuf in cache.
     *
     * @return File path that points to .proto file in cache containing nodes.
     */
    private suspend fun parseDocumentEntry(
        zipFile: ZipFile,
        entry: ZipEntry,
        imageEntries: Map<String, ImageMetadata>,
        cacheNodes: suspend (List<NodeContent>) -> FilePath?
    ): FilePath? {
        return run {
            zipFile.getInputStream(entry).use { stream ->
                cacheNodes(
                    jsoupDocumentParser.parseDocument(
                        document = Jsoup.parse(stream, null, "", Parser.htmlParser()),
                        images = imageEntries
                    ).ifEmpty {
                        logW(
                            TAG,
                            "Could not extract nodes from document entry [${entry.name}]: empty."
                        )
                        return@use null
                    }
                ).also {
                    if (it == null) {
                        logW(
                            TAG,
                            "Could not extract nodes from document entry [${entry.name}]: could not save to cache."
                        )
                    }
                }
            }
        }
    }

    /**
     * Gets all zip entries with sorting to parse from [ZipFile].
     * * If [opfEntry] is not null, then gets entries from spine.
     * * If [opfEntry] is null, then gets entries from the whole [ZipFile] and manually sorts them.
     *
     * @return A list of [ZipEntry] in the correct order (should not be reordered).
     */
    private fun getSortedDocumentEntries(
        zipFile: ZipFile,
        zipEntries: List<ZipEntry>,
        opfEntry: ZipEntry?
    ): List<ZipEntry> {
        opfEntry?.also { opfEntry ->
            val document = zipFile.getInputStream(opfEntry).use {
                Jsoup.parse(it, null, "", Parser.xmlParser())
            }

            val manifestEntries = document.select("manifest > item").associate { element ->
                element.attr("id") to element.attr("href").let { src ->
                    URLDecoder.decode(src, StandardCharsets.UTF_8.name())
                }
            }
            val spineZipEntries = document.select("spine > itemref").mapNotNull { itemRef ->
                val spineId = itemRef.attr("idref")
                val manifestEntry = manifestEntries[spineId].let { src ->
                    if (src.isNullOrBlank()) return@mapNotNull null
                    URLDecoder.decode(
                        src.substringAfterLast(File.separator),
                        StandardCharsets.UTF_8.name()
                    )
                }
                zipEntries.find { entry ->
                    manifestEntry.equals(
                        entry.name.substringAfterLast(File.separator),
                        ignoreCase = true
                    )
                }
            }.ifEmpty { return@also }

            logI(
                TAG, "Successfully parsed OPF to get [${spineZipEntries.size}] entries from spine."
            )
            return spineZipEntries
        }

        logW(TAG, "Could not parse OPF, manual filtering.")
        return zipEntries.filter { entry ->
            listOf(".html", ".htm", ".xhtml").any {
                entry.name.endsWith(it, ignoreCase = true)
            }
        }.sortedBy {
            // Trying to sort by incremental numbers in file names,
            // though not all files use such a structure, so this solution
            // is a fallback in case of OPF error and should not be used
            // in normal conditions.
            it.name.filter { char -> char.isDigit() }.toBigIntegerOrNull()
        }
    }

    /**
     * Gets images (and saves them to cache for further use) from Zip File.
     *
     * Also extracts metadata from them (such as width and height).
     *
     * @return A list of [ImageMetadata] for further parsing with Jsoup.
     */
    private suspend fun getImageEntries(
        zipFile: ZipFile,
        zipEntries: List<ZipEntry>,
        cacheImage: suspend (filename: String, inputStream: InputStream) -> ImageMetadata?
    ): Map<String, ImageMetadata> {
        return zipEntries.mapNotNull { entry ->
            ExtensionsData.imageExtensions.none { format ->
                entry.name.endsWith(
                    format,
                    ignoreCase = true
                )
            }.ifTrue { return@mapNotNull null }

            zipFile.getInputStream(entry).use { inputStream ->
                cacheImage(
                    entry.name,
                    inputStream
                ).let { if (it != null) it.originalName to it else null }
            }
        }.associate { it }
            .also { logI(TAG, "Successfully saved to cache and got [${it.size}] image entries.") }
    }
}