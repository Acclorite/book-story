@file:OptIn(ExperimentalCoroutinesApi::class)

package ua.acclorite.book_story.data.parser.epub

import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import org.jsoup.Jsoup
import ua.acclorite.book_story.R
import ua.acclorite.book_story.data.parser.DocumentParser
import ua.acclorite.book_story.data.parser.TextParser
import ua.acclorite.book_story.domain.model.Chapter
import ua.acclorite.book_story.domain.model.ChapterWithText
import ua.acclorite.book_story.domain.util.Resource
import ua.acclorite.book_story.domain.util.UIText
import ua.acclorite.book_story.presentation.core.util.addAll
import ua.acclorite.book_story.presentation.core.util.clearMarkdown
import java.io.File
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import javax.inject.Inject
import kotlin.collections.set

private const val EPUB_TAG = "EPUB Parser"
private typealias Title = String

private val dispatcher = Dispatchers.IO.limitedParallelism(2)

class EpubTextParser @Inject constructor(
    private val documentParser: DocumentParser
) : TextParser {

    override suspend fun parse(file: File): Resource<List<ChapterWithText>> {
        Log.i(EPUB_TAG, "Started EPUB parsing: ${file.name}.")

        return try {
            val chapters = mutableListOf<ChapterWithText>()

            yield()

            withContext(Dispatchers.IO) {
                ZipFile(file).use { zip ->
                    val tocEntry = zip.entries().toList().find { entry ->
                        entry.name.endsWith("toc.ncx", ignoreCase = true)
                    }
                    val opfEntry = zip.entries().toList().find { entry ->
                        entry.name.endsWith(".opf", ignoreCase = true)
                    }

                    val chapterEntries = zip.getChapterEntries(opfEntry)
                    val chapterTitleEntries = zip.getChapterTitleMapFromToc(tocEntry)

                    Log.i(EPUB_TAG, "TOC Entry: ${tocEntry?.name ?: "no toc.ncx"}")
                    Log.i(EPUB_TAG, "OPF Entry: ${opfEntry?.name ?: "no .opf entry"}")
                    Log.i(EPUB_TAG, "Chapter entries, size: ${chapterEntries.size}")
                    Log.i(EPUB_TAG, "Title entries, size: ${chapterTitleEntries?.size}")

                    zip.parseEpub(
                        chapterEntries = chapterEntries,
                        chapterTitleEntries = chapterTitleEntries
                    ).let {
                        if (it == null || it.isEmpty()) {
                            Log.e(EPUB_TAG, "Could not parse EPUB (null or empty).")
                            return@withContext
                        }
                        chapters.addAll(it)
                    }
                }
            }

            yield()

            if (chapters.isEmpty()) {
                return Resource.Error(UIText.StringResource(R.string.error_file_empty))
            }

            Log.i(EPUB_TAG, "Successfully finished EPUB parsing.")
            Resource.Success(chapters)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                UIText.StringResource(
                    R.string.error_query,
                    e.message?.take(40)?.trim() ?: ""
                )
            )
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
    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun ZipFile.parseEpub(
        chapterEntries: List<ZipEntry>,
        chapterTitleEntries: Map<Title, List<String>>?
    ): List<ChapterWithText>? {

        val chapters = mutableListOf<ChapterWithText>()
        coroutineScope {
            val unformattedChapters = ConcurrentLinkedQueue<ChapterWithText>()

            // Asynchronously getting all chapters with text
            val jobs = chapterEntries.mapIndexed { index, entry ->
                async(dispatcher) {
                    yield()

                    unformattedChapters.parseZipEntry(
                        zip = this@parseEpub,
                        index = index,
                        entry = entry,
                        chapterTitleMap = chapterTitleEntries
                    )

                    yield()
                }
            }
            jobs.awaitAll()

            // Sorting chapters in correct order
            chapters.addAll {
                var textIndex = -1
                unformattedChapters.toList()
                    .sortedBy { it.chapter.index }
                    .mapIndexed { index, item ->
                        item.copy(
                            chapter = item.chapter.copy(
                                index = index,
                                startIndex = textIndex + 1,
                                endIndex = textIndex + item.text.size
                            )
                        ).also { textIndex += item.text.size }
                    }
            }
        }

        if (chapters.isEmpty()) {
            return null
        }

        return chapters
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
    private suspend fun ConcurrentLinkedQueue<ChapterWithText>.parseZipEntry(
        zip: ZipFile,
        index: Int,
        entry: ZipEntry,
        chapterTitleMap: Map<Title, List<String>>?
    ) {
        // Getting all text
        val content = zip.getInputStream(entry).bufferedReader().use { it.readText() }
        var chapter = documentParser.run {
            Jsoup.parse(content).parseDocument()
        }

        if (chapter.isEmpty()) {
            Log.w(EPUB_TAG, "Chapter ${entry.name} is empty.")
            return
        }

        // Getting title and removing first line (if matches title)
        val chapterTitle = getChapterTitleFromToc(
            chapterSource = entry.name,
            chapterTitleMap = chapterTitleMap
        ).run {
            if (this != null) {
                return@run this
            }
            chapter.first().clearMarkdown()
        }.also { title ->
            chapter = chapter.dropWhile { line ->
                line.clearMarkdown().lowercase() == title.lowercase()
            }
        }

        if (chapter.isEmpty()) {
            Log.w(EPUB_TAG, "Chapter ${entry.name} is empty.")
            return
        }

        add(
            ChapterWithText(
                Chapter(
                    index = index,
                    title = chapterTitle,
                    startIndex = 0,
                    endIndex = 0
                ),
                text = chapter
            )
        )
    }

    /**
     * Getting all titles from [tocEntry].
     *
     * @return null if [tocEntry] is null.
     */
    private suspend fun ZipFile.getChapterTitleMapFromToc(
        tocEntry: ZipEntry?
    ): Map<Title, List<String>>? {
        val tocContent = tocEntry?.let {
            withContext(Dispatchers.IO) {
                getInputStream(it)
            }.bufferedReader().use { it.readText() }
        }
        val tocDocument = tocContent?.let { Jsoup.parse(it) }

        if (tocDocument == null) return null
        var titleMap = mutableMapOf<Title, List<String>>()

        tocDocument.select("navPoint").forEach { navPoint ->
            val title = navPoint.selectFirst("navLabel > text")?.text()?.trim()
                ?: return@forEach
            val source = navPoint.selectFirst("content")?.attr("src")?.trim()
                .let {
                    if (it == null) return@forEach
                    Uri.parse(it).path ?: it
                }.substringAfterLast("/")

            titleMap[source] = (titleMap[source] ?: emptyList()) + title
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
        chapterTitleMap: Map<String, List<String>>?
    ): String? {
        if (chapterTitleMap.isNullOrEmpty()) return null
        return chapterTitleMap
            .getOrElse(chapterSource.substringAfterLast("/")) { null }
            ?.joinToString(separator = " / ")
            ?.ifBlank { null }
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
        opfEntry.let { opfEntry ->
            if (opfEntry == null) {
                return@let
            }

            val opfContent = getInputStream(opfEntry).bufferedReader().use {
                it.readText()
            }
            val document = Jsoup.parse(opfContent)
            val zipEntries = entries().toList()

            val manifestItems = document.select("manifest > item").associate {
                it.attr("id") to it.attr("href")
            }

            document.select("spine > itemref").mapNotNull { itemRef ->
                val spineId = itemRef.attr("idref")
                val chapterSource = manifestItems[spineId]?.substringAfterLast('/')?.lowercase()
                    ?: return@mapNotNull null

                zipEntries.find { entry ->
                    entry.name.substringAfterLast('/').lowercase() == chapterSource
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