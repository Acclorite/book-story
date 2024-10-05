package ua.acclorite.book_story.data.parser.epub

import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
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
import ua.acclorite.book_story.presentation.core.util.clearMarkdown
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import javax.inject.Inject

private const val EPUB_TAG = "EPUB Parser"

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
                    yield()

                    zip.entries().asSequence().find { entry ->
                        entry.name.endsWith("toc.ncx", ignoreCase = true)
                    }.apply {
                        parseEpub(tocEntry = this, zip = zip).let {
                            if (it == null) {
                                Log.e(EPUB_TAG, "Could not parse EPUB.")
                                return@withContext
                            }
                            chapters.addAll(it)
                        }
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
     * @return Null if could not parse.
     */
    private suspend fun parseEpub(tocEntry: ZipEntry?, zip: ZipFile): List<ChapterWithText>? {
        Log.i(EPUB_TAG, "TOC Entry: ${tocEntry?.name ?: "NO TOC"}")

        yield()

        val chapters = mutableListOf<ChapterWithText>()
        var chapterTextIndex = -1

        val tocContent = tocEntry?.let {
            withContext(Dispatchers.IO) {
                zip.getInputStream(it)
            }.bufferedReader().use { it.readText() }
        }
        val tocDocument = tocContent?.let { Jsoup.parse(it) }
        val chaptersTitles = tocDocument.run {
            if (this == null) return@run null
            var titles = mutableMapOf<String, List<String>>()

            select("navPoint").forEach { navPoint ->
                val title = navPoint.selectFirst("navLabel > text")?.text()?.trim()
                    ?: return@forEach
                val source = navPoint.selectFirst("content")?.attr("src")?.trim().let {
                    if (it == null) return@forEach
                    Uri.parse(it).path ?: it
                }.substringAfterLast("/")

                titles[source] = (titles[source] ?: emptyList()) + title
            }

            titles
        }

        yield()

        zip.entries().asSequence().sortedBy {
            it.name.filter { it.isDigit() }.toIntOrNull()
        }.forEach { entry ->
            yield()

            if (
                !entry.name.endsWith(".xhtml")
                && !entry.name.endsWith(".html")
                && !entry.name.endsWith(".htm")
            ) return@forEach

            yield()

            val content = zip.getInputStream(entry).bufferedReader().use { it.readText() }
            var chapter = documentParser.run {
                Jsoup.parse(content).parseDocument()
            }

            if (chapter.isEmpty()) {
                Log.w(EPUB_TAG, "Chapter ${entry.name} is empty.")
                return@forEach
            }

            val chapterTitle = getChapterTitleFromToc(
                chapterSource = entry.name,
                chaptersTitles = chaptersTitles
            ).run {
                if (this != null) {
                    return@run this
                }
                chapter.first().clearMarkdown()
            }

            chapter = chapter.dropWhile {
                it.clearMarkdown().lowercase() == chapterTitle.lowercase()
            }
            
            if (chapter.isEmpty()) {
                Log.w(EPUB_TAG, "Chapter ${entry.name} is empty.")
                return@forEach
            }

            yield()

            chapters.add(
                ChapterWithText(
                    chapter = Chapter(
                        index = chapters.size,
                        title = chapterTitle,
                        startIndex = chapterTextIndex + 1,
                        endIndex = chapterTextIndex + chapter.size
                    ),
                    text = chapter
                )
            )
            chapterTextIndex += chapter.size
        }

        yield()

        if (chapters.isEmpty()) {
            Log.e(EPUB_TAG, "Could not parse file without toc.ncx")
            return null
        }

        return chapters
    }

    private fun getChapterTitleFromToc(
        chapterSource: String,
        chaptersTitles: Map<String, List<String>>?
    ): String? {
        if (chaptersTitles.isNullOrEmpty()) return null
        return chaptersTitles
            .getOrElse(chapterSource.substringAfterLast("/")) { null }
            ?.joinToString(separator = " / ")
            ?.ifBlank { null }
    }
}