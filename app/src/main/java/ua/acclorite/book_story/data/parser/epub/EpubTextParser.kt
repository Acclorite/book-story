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
import ua.acclorite.book_story.data.parser.DocumentParser
import ua.acclorite.book_story.data.parser.TextParser
import ua.acclorite.book_story.domain.reader.ReaderText
import ua.acclorite.book_story.presentation.core.constants.Constants
import ua.acclorite.book_story.presentation.core.constants.provideImageExtensions
import ua.acclorite.book_story.presentation.core.util.addAll
import ua.acclorite.book_story.presentation.core.util.containsVisibleText
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

    override suspend fun parse(file: File): List<ReaderText> {
        Log.i(EPUB_TAG, "Started EPUB parsing: ${file.name}.")

        return try {
            yield()
            var readerText = listOf<ReaderText>()

            withContext(Dispatchers.IO) {
                ZipFile(file).use { zip ->
                    val tocEntry = zip.entries().toList().find { entry ->
                        entry.name.endsWith(".ncx", ignoreCase = true)
                    }
                    val opfEntry = zip.entries().toList().find { entry ->
                        entry.name.endsWith(".opf", ignoreCase = true)
                    }

                    val chapterEntries = zip.getChapterEntries(opfEntry)
                    val imageEntries = zip.entries().toList().filter {
                        Constants.provideImageExtensions().any { format ->
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
    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun ZipFile.parseEpub(
        chapterEntries: List<ZipEntry>,
        imageEntries: List<ZipEntry>,
        chapterTitleEntries: Map<Title, List<String>>?
    ): List<ReaderText> {

        val readerText = mutableListOf<ReaderText>()
        coroutineScope {
            val unformattedText = ConcurrentLinkedQueue<Pair<Int, List<ReaderText>>>()

            // Asynchronously getting all chapters with text
            val jobs = chapterEntries.mapIndexed { index, entry ->
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
            }
            jobs.awaitAll()

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
        chapterTitleMap: Map<Title, List<String>>?
    ) {
        // Getting all text
        val content = zip.getInputStream(entry).bufferedReader().use { it.readText() }
        var readerText = documentParser.run {
            Jsoup.parse(content).parseDocument(
                zipFile = zip,
                imageEntries = imageEntries,
                includeChapter = false
            )
        }.toMutableList()

        // Adding chapter title from TOC if found
        getChapterTitleFromToc(
            chapterSource = entry.name,
            chapterTitleMap = chapterTitleMap
        ).apply {
            val chapterTitle = this ?: run {
                val firstVisibleText = readerText.firstOrNull { line ->
                    line is ReaderText.Text && line.line.text.containsVisibleText()
                } as? ReaderText.Text ?: return
                firstVisibleText.line.text
            }

            readerText = readerText.dropWhile { line ->
                (line is ReaderText.Text && line.line.text.lowercase() == chapterTitle.lowercase())
            }.toMutableList()

            readerText.add(
                0,
                ReaderText.Chapter(
                    title = chapterTitle
                )
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
            ?.trim()
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