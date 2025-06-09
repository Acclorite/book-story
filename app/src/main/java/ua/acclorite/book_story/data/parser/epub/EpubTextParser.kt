/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

@file:OptIn(ExperimentalCoroutinesApi::class)

package ua.acclorite.book_story.data.parser.epub

import android.util.Log
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import ua.acclorite.book_story.core.data.ExtensionsData
import ua.acclorite.book_story.core.helpers.addAll
import ua.acclorite.book_story.core.helpers.containsVisibleText
import ua.acclorite.book_story.data.model.file.CachedFile
import ua.acclorite.book_story.data.parser.DocumentParser
import ua.acclorite.book_story.data.parser.TextParser
import ua.acclorite.book_story.domain.model.reader.ReaderText
import java.io.File
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import javax.inject.Inject

private const val EPUB_TAG = "EPUB Parser"
private typealias Source = String

private val dispatcher = Dispatchers.IO.limitedParallelism(3)

class EpubTextParser @Inject constructor(
    private val documentParser: DocumentParser
) : TextParser {

    override suspend fun parse(cachedFile: CachedFile): List<ReaderText> {
        Log.i(EPUB_TAG, "Started EPUB parsing: ${cachedFile.name}.")

        return try {
            yield()
            var readerText = listOf<ReaderText>()

            val rawFile = cachedFile.rawFile
            if (rawFile == null || !rawFile.exists() || !rawFile.canRead()) return emptyList()

            withContext(Dispatchers.IO) {
                ZipFile(rawFile).use { zip ->
                    val tocEntry = zip.entries().toList().find { entry ->
                        entry.name.endsWith(".ncx", ignoreCase = true)
                    }
                    val opfEntry = zip.entries().toList().find { entry ->
                        entry.name.endsWith(".opf", ignoreCase = true)
                    }

                    val chapterEntries = zip.getChapterEntries(opfEntry)
                    val imageEntries = zip.entries().toList().filter {
                        ExtensionsData.imageExtensions.any { format ->
                            it.name.endsWith(format, ignoreCase = true)
                        }
                    }
                    val chapterTitleEntries = zip.getChapterTitleMapFromToc(tocEntry)

                    Log.i(EPUB_TAG, "TOC Entry: ${tocEntry?.name ?: "no toc.ncx"}")
                    Log.i(EPUB_TAG, "OPF Entry: ${opfEntry?.name ?: "no .opf entry"}")
                    Log.i(EPUB_TAG, "Chapter entries, size: ${chapterEntries.size}")
                    Log.i(EPUB_TAG, "Title entries, size: ${chapterTitleEntries?.size}")

                    readerText = zip.parseEpub(
                        chapterEntries = chapterEntries,
                        imageEntries = imageEntries,
                        chapterTitleEntries = chapterTitleEntries
                    )
                }
            }

            yield()

            if (
                readerText.filterIsInstance<ReaderText.Text>().isEmpty() ||
                readerText.filterIsInstance<ReaderText.Chapter>().isEmpty()
            ) {
                Log.e(EPUB_TAG, "Could not extract text from EPUB.")
                return emptyList()
            }

            Log.i(EPUB_TAG, "Successfully finished EPUB parsing.")
            readerText
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * Parses text and chapters from EPUB.
     * Uses toc.ncx(if present) to retrieve titles, otherwise uses first line as title.
     *
     * @param chapterTitleEntries Titles extracted from toc.ncx.
     * @param chapterEntries [ZipEntry]s to parse.
     *
     * @return Null if could not parse.
     */
    private suspend fun ZipFile.parseEpub(
        chapterEntries: List<ZipEntry>,
        imageEntries: List<ZipEntry>,
        chapterTitleEntries: Map<Source, ReaderText.Chapter>?
    ): List<ReaderText> {

        val readerText = mutableListOf<ReaderText>()
        withContext(Dispatchers.IO) {
            val unformattedText = ConcurrentLinkedQueue<Pair<Int, List<ReaderText>>>()

            // Asynchronously getting all chapters with text
            chapterEntries.mapIndexed { index, entry ->
                async(dispatcher) {
                    yield()

                    unformattedText.parseZipEntry(
                        zip = this@parseEpub,
                        index = index,
                        entry = entry,
                        imageEntries = imageEntries,
                        chapterTitleMap = chapterTitleEntries
                    )

                    yield()
                }
            }.awaitAll()

            // Sorting chapters in correct order
            readerText.addAll {
                unformattedText.toList()
                    .sortedBy { (index, _) -> index }
                    .map { it.second }
                    .flatten()
            }
        }

        return readerText
    }

    /**
     * Parses [entry] to get it's text and chapter.
     * Adds parsed entry in [ConcurrentLinkedQueue].
     *
     * @param zip [ZipFile] of the [entry].
     * @param index Index of the [entry].
     * @param entry [ZipEntry].
     * @param chapterTitleMap Titles from [getChapterTitleMapFromToc].
     */
    private suspend fun ConcurrentLinkedQueue<Pair<Int, List<ReaderText>>>.parseZipEntry(
        zip: ZipFile,
        index: Int,
        entry: ZipEntry,
        imageEntries: List<ZipEntry>,
        chapterTitleMap: Map<Source, ReaderText.Chapter>?
    ) {
        // Getting all text
        val content = withContext(Dispatchers.IO) {
            zip.getInputStream(entry)
        }.bufferedReader().use { it.readText() }
        var readerText = documentParser.parseDocument(
            document = Jsoup.parse(content, Parser.htmlParser()),
            zipFile = zip,
            imageEntries = imageEntries,
            includeChapter = false
        ).toMutableList()

        // Adding chapter title from TOC if found
        getChapterTitleFromToc(
            chapterSource = entry.name,
            chapterTitleMap = chapterTitleMap
        ).apply {
            val chapter = this ?: run {
                val firstVisibleText = readerText.firstOrNull { line ->
                    line is ReaderText.Text && line.line.text.containsVisibleText()
                } as? ReaderText.Text ?: return

                return@run ReaderText.Chapter(
                    title = firstVisibleText.line.text,
                    nested = false
                )
            }

            readerText = readerText.dropWhile { line ->
                (line is ReaderText.Text && line.line.text.lowercase() == chapter.title.lowercase())
            }.toMutableList()

            readerText.add(
                0,
                chapter
            )
        }

        if (
            readerText.filterIsInstance<ReaderText.Text>().isEmpty() ||
            readerText.filterIsInstance<ReaderText.Chapter>().isEmpty()
        ) {
            Log.w(EPUB_TAG, "Could not extract text from [${entry.name}].")
            return
        }

        add(index to readerText)
    }

    /**
     * Getting all titles from [tocEntry].
     *
     * @return null if [tocEntry] is null.
     */
    private suspend fun ZipFile.getChapterTitleMapFromToc(
        tocEntry: ZipEntry?
    ): Map<Source, ReaderText.Chapter>? {
        val tocContent = tocEntry?.let {
            withContext(Dispatchers.IO) {
                getInputStream(it)
            }.bufferedReader().use { it.readText() }
        }
        val tocDocument = tocContent?.let { Jsoup.parse(it) }

        if (tocDocument == null) return null
        val titleMap = mutableMapOf<Source, ReaderText.Chapter>()

        tocDocument.select("navPoint").forEach { navPoint ->
            val title = navPoint.selectFirst("navLabel > text")?.text()
                .let { title ->
                    if (title.isNullOrBlank()) return@forEach
                    title.trim()
                }

            val source = navPoint.selectFirst("content")?.attr("src")?.trim()
                .also { src -> if (src.isNullOrBlank()) return@forEach }
                .let { src -> URLDecoder.decode(src, StandardCharsets.UTF_8.name()) }
                .let { src -> src.toUri().path ?: src }
                .substringAfterLast(File.separator)

            val parent = navPoint.parent()
                ?.let { parent ->
                    if (!parent.tagName().equals("navPoint", ignoreCase = true)) return@let null

                    val parentSource = parent.selectFirst("content")?.attr("src")?.trim()
                        .also { src -> if (src.isNullOrBlank()) return@forEach }
                        .let { src -> URLDecoder.decode(src, StandardCharsets.UTF_8.name()) }
                        .let { src -> src.toUri().path ?: src }
                        .substringAfterLast(File.separator)

                    if (parentSource == source) return@let null
                    return@let parentSource
                }

            val chapter = ReaderText.Chapter(
                title = titleMap[source]?.title.run {
                    if (this == null) return@run title
                    return@run "$this / $title"
                },
                nested = titleMap[source]?.nested ?: (parent != null)
            )
            titleMap[source] = chapter
        }

        return titleMap
    }

    /**
     * Getting title from [chapterTitleMap].
     *
     * @return Null if did not find matching chapters to the [chapterSource].
     */
    private fun getChapterTitleFromToc(
        chapterSource: String,
        chapterTitleMap: Map<Source, ReaderText.Chapter>?
    ): ReaderText.Chapter? {
        if (chapterTitleMap.isNullOrEmpty()) return null
        return chapterTitleMap.getOrElse(chapterSource.substringAfterLast(File.separator)) { null }
    }

    /**
     * Getting all chapter entries.
     * If [opfEntry] is not null, then getting chapters from Spine.
     * If [opfEntry] is null, then getting chapters from the whole [ZipFile] and manually sorting them.
     *
     * @param opfEntry OPF entry. May be null.
     *
     * @return List of chapter entries in correct order (do not reorder).
     */
    private fun ZipFile.getChapterEntries(opfEntry: ZipEntry?): List<ZipEntry> {
        opfEntry?.let {
            val opfContent = getInputStream(opfEntry).bufferedReader().use {
                it.readText()
            }
            val document = Jsoup.parse(opfContent, Parser.xmlParser())
            val zipEntries = entries().toList()

            val manifestItems = document.select("manifest > item").associate {
                it.attr("id").to(
                    it.attr("href").let { src ->
                        URLDecoder.decode(src, StandardCharsets.UTF_8.name())
                    }
                )
            }

            document.select("spine > itemref").mapNotNull { itemRef ->
                val spineId = itemRef.attr("idref")
                val chapterSource = manifestItems[spineId]
                    .let { src ->
                        if (src.isNullOrBlank()) return@mapNotNull null
                        URLDecoder.decode(
                            src.substringAfterLast(File.separator).lowercase(),
                            StandardCharsets.UTF_8.name()
                        )
                    }

                zipEntries.find { entry ->
                    entry.name.substringAfterLast(File.separator).lowercase() == chapterSource
                }
            }.also { entries ->
                if (entries.isEmpty()) return@let

                Log.i(EPUB_TAG, "Successfully parsed OPF to get entries from spine.")
                return entries
            }
        }

        Log.w(EPUB_TAG, "Could not parse OPF, manual filtering.")
        return entries().toList().filter { entry ->
            listOf(".html", ".htm", ".xhtml").any {
                entry.name.endsWith(it, ignoreCase = true)
            }
        }.sortedBy {
            it.name.filter { char -> char.isDigit() }.toBigIntegerOrNull()
        }
    }
}