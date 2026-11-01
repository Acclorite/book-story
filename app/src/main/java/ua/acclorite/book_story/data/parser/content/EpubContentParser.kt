/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.parser.content

import android.util.Xml
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
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import ua.acclorite.book_story.R
import ua.acclorite.book_story.core.data.ExtensionsData
import ua.acclorite.book_story.core.helpers.concurrentMap
import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.core.log.logI
import ua.acclorite.book_story.core.log.logW
import ua.acclorite.book_story.core.ui.UIText
import ua.acclorite.book_story.data.parser.content.document.JsoupDocumentParser
import ua.acclorite.book_story.data.parser.content.document.cacheEmbeddedDocumentImages
import ua.acclorite.book_story.data.storage.file.CachedFile
import ua.acclorite.book_story.domain.model.book.NodeContent
import ua.acclorite.book_story.domain.model.file.FilePath
import ua.acclorite.book_story.domain.model.file.ImageMetadata
import ua.acclorite.book_story.data.parser.content.document.resolveDocumentPath
import java.io.InputStream
import java.util.concurrent.atomic.AtomicInteger
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import javax.inject.Inject

private const val TAG = "EpubContentParser"
private const val XHTML_NAMESPACE = "http://www.w3.org/1999/xhtml"

private data class EpubDocumentEntry(val entry: ZipEntry, val mediaType: String? = null)

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
                val opfEntry = findPackageEntry(zipFile, zipEntries)

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
                        transform = { documentEntry ->
                            parseDocumentEntry(
                                zipFile = zipFile,
                                entry = documentEntry.entry,
                                mediaType = documentEntry.mediaType,
                                imageEntries = imageEntries,
                                cacheImage = cacheImage,
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
        mediaType: String?,
        imageEntries: Map<String, ImageMetadata>,
        cacheImage: suspend (String, InputStream) -> ImageMetadata?,
        cacheNodes: suspend (List<NodeContent>) -> FilePath?
    ): FilePath? {
        imageEntries[entry.name]?.let { image ->
            return cacheNodes(
                listOf(NodeContent.Image(image.path.path, null, image.width, image.height, image.aspectRatio))
            ) ?: error("Could not save EPUB image document: ${entry.name}")
        }
        if (entry.name.substringAfterLast('.').lowercase() in
            setOf("jpg", "jpeg", "png", "webp", "gif", "bmp")
        ) {
            logW(TAG, "Skipping unreadable image document: ${entry.name}")
            return null
        }
        val parser = if (mediaType?.trim().equals("application/xhtml+xml", ignoreCase = true) &&
            isWellFormedXhtml(zipFile, entry)
        ) Parser.xmlParser() else Parser.htmlParser()
        return run {
            zipFile.getInputStream(entry).use { stream ->
                val document = Jsoup.parse(stream, null, "", parser)
                val embedded = cacheEmbeddedDocumentImages(document, cacheImage)
                val stylesheets = document.select("link[href]").filter {
                    "stylesheet" in it.attr("rel").lowercase().split(Regex("\\s+"))
                }.mapNotNull { link ->
                    val path = resolveDocumentPath(entry.name, link.attr("href")) ?: return@mapNotNull null
                    val sheet = zipFile.getEntry(path) ?: return@mapNotNull null
                    if (sheet.isDirectory) return@mapNotNull null
                    try {
                        zipFile.getInputStream(sheet).bufferedReader().use { it.readText() }
                    } catch (cancelled: CancellationException) {
                        throw cancelled
                    } catch (exception: Exception) {
                        logW(TAG, "Cannot read optional EPUB stylesheet [$path]: ${exception.message}")
                        null
                    }
                }
                cacheNodes(
                    jsoupDocumentParser.parseDocument(
                        document = document,
                        images = imageEntries + embedded,
                        documentPath = entry.name,
                        stylesheets = stylesheets
                    ).ifEmpty {
                        logW(
                            TAG,
                            "Could not extract nodes from document entry [${entry.name}]: empty."
                        )
                        return@use null
                    }
                ).also {
                    if (it == null) {
                        error("Could not save EPUB document to cache: ${entry.name}")
                    }
                }
            }
        }
    }

    private suspend fun isWellFormedXhtml(zipFile: ZipFile, entry: ZipEntry): Boolean {
        return try {
            zipFile.getInputStream(entry).use { stream ->
                val parser = Xml.newPullParser().apply {
                    setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true)
                    setInput(stream, null)
                }
                if (parser.nextTag() != XmlPullParser.START_TAG ||
                    parser.name != "html" || parser.namespace != XHTML_NAMESPACE
                ) return@use false
                var hasBody = false
                while (parser.eventType != XmlPullParser.END_DOCUMENT) {
                    currentCoroutineContext().ensureActive()
                    if (parser.eventType == XmlPullParser.START_TAG && parser.depth == 2 &&
                        parser.name == "body" && parser.namespace == XHTML_NAMESPACE
                    ) hasBody = true
                    parser.next()
                }
                hasBody
            }
        } catch (exception: XmlPullParserException) {
            logW(TAG, "Malformed XHTML [${entry.name}]; using HTML parsing: ${exception.message}")
            false
        }
    }

    private fun findPackageEntry(zipFile: ZipFile, entries: List<ZipEntry>): ZipEntry? {
        val container = zipFile.getEntry("META-INF/container.xml")
        if (container != null) {
            val document = zipFile.getInputStream(container).use {
                Jsoup.parse(it, null, "", Parser.xmlParser())
            }
            val rootfiles = document.getAllElements().filter { it.normalName().substringAfter(':') == "rootfile" }
            val rootfile = rootfiles.firstOrNull {
                it.attr("media-type") == "application/oebps-package+xml"
            } ?: rootfiles.firstOrNull()
            val path = rootfile?.attr("full-path")?.let { resolveDocumentPath("", it) }
            entries.firstOrNull { it.name == path }?.let { return it }
            logW(TAG, "Container did not resolve a package document; trying an OPF fallback.")
        }
        return entries.firstOrNull { !it.isDirectory && it.name.endsWith(".opf", true) }
    }

    private fun getSortedDocumentEntries(
        zipFile: ZipFile,
        zipEntries: List<ZipEntry>,
        opfEntry: ZipEntry?
    ): List<EpubDocumentEntry> {
        if (opfEntry != null) {
            val document = zipFile.getInputStream(opfEntry).use {
                Jsoup.parse(it, null, "", Parser.xmlParser())
            }
            val manifest = document.getAllElements().filter {
                it.normalName().substringAfter(':') == "item" &&
                    it.parent()?.normalName()?.substringAfter(':') == "manifest"
            }.associateBy { it.id() }
            val spine = document.getAllElements().filter {
                it.normalName().substringAfter(':') == "itemref" &&
                    it.parent()?.normalName()?.substringAfter(':') == "spine"
            }
            if (spine.isNotEmpty()) {
                fun resolveItem(id: String, visited: MutableSet<String>): EpubDocumentEntry {
                    check(visited.add(id)) { "Cyclic EPUB manifest fallback: $id" }
                    val item = manifest[id] ?: error("Missing EPUB manifest item: $id")
                    val path = resolveDocumentPath(opfEntry.name, item.attr("href"))
                    val entry = zipEntries.firstOrNull { !it.isDirectory && it.name == path }
                    val supported = item.attr("media-type") in setOf(
                        "application/xhtml+xml", "text/html", "image/svg+xml"
                    ) || entry?.name?.substringAfterLast('.')?.lowercase() in
                        setOf("html", "htm", "xhtml", "svg", "jpg", "jpeg", "png", "webp", "gif", "bmp")
                    if (entry != null && supported) return EpubDocumentEntry(entry, item.attr("media-type"))
                    val fallback = item.attr("fallback")
                    if (fallback.isNotBlank()) return resolveItem(fallback, visited)
                    error("Cannot resolve EPUB spine item [$id]: ${item.attr("href")}")
                }
                return spine.map { resolveItem(it.attr("idref"), mutableSetOf()) }
            }
            logW(TAG, "Package has no spine entries; using document filename order.")
        }
        return zipEntries.filter { entry ->
            !entry.isDirectory && listOf(".html", ".htm", ".xhtml", ".svg").any {
                entry.name.endsWith(it, ignoreCase = true)
            }
        }.sortedWith { first, second -> compareDocumentNames(first.name, second.name) }
            .map { EpubDocumentEntry(it) }
    }

    private fun compareDocumentNames(first: String, second: String): Int {
        val parts = Regex("\\d+|\\D+")
        val left = parts.findAll(first).map { it.value }.toList()
        val right = parts.findAll(second).map { it.value }.toList()
        for (index in 0 until minOf(left.size, right.size)) {
            val a = left[index]
            val b = right[index]
            val numberA = a.toBigIntegerOrNull()
            val numberB = b.toBigIntegerOrNull()
            val result = if (numberA != null && numberB != null) numberA.compareTo(numberB)
            else a.compareTo(b, ignoreCase = true)
            if (result != 0) return result
        }
        return left.size.compareTo(right.size).takeIf { it != 0 } ?: first.compareTo(second)
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
            if (entry.isDirectory || ExtensionsData.imageExtensions.none { format ->
                entry.name.endsWith(
                    format,
                    ignoreCase = true
                )
            }) return@mapNotNull null

            zipFile.getInputStream(entry).use { inputStream ->
                cacheImage(
                    entry.name,
                    inputStream
                ).let { if (it != null) entry.name to it else null }
            }
        }.associate { it }
            .also { logI(TAG, "Successfully saved to cache and got [${it.size}] image entries.") }
    }
}