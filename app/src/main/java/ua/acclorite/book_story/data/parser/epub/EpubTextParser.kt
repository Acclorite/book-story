package ua.acclorite.book_story.data.parser.epub

import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import ua.acclorite.book_story.R
import ua.acclorite.book_story.data.parser.TextParser
import ua.acclorite.book_story.domain.model.Chapter
import ua.acclorite.book_story.domain.model.ChapterWithText
import ua.acclorite.book_story.domain.util.Resource
import ua.acclorite.book_story.domain.util.UIText
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import javax.inject.Inject

private const val EPUB_TAG = "EPUB Parser"

class EpubTextParser @Inject constructor() : TextParser {

    override suspend fun parse(file: File): Resource<List<ChapterWithText>> {
        Log.i(EPUB_TAG, "Started EPUB parsing: ${file.name}.")

        return try {
            val chapters = mutableListOf<ChapterWithText>()

            withContext(Dispatchers.IO) {
                ZipFile(file).use { zip ->
                    zip.entries().asSequence().find { entry ->
                        entry.name.endsWith("toc.ncx", ignoreCase = true)
                    }.apply {
                        if (this == null) {
                            Log.w(EPUB_TAG, "toc.ncx was not found.")
                            parseWithoutToc(zip)?.let { chapters.addAll(it) }
                            return@withContext
                        }

                        parseWithToc(this, zip).apply {
                            if (this == null) {
                                parseWithoutToc(zip)?.let { chapters.addAll(it) }
                                return@withContext
                            }

                            chapters.addAll(this)
                        }
                    }
                }
            }

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
     * Parses text if no toc.ncx found, which is Table of Content.
     *
     * @return Null if could not parse.
     */
    private fun parseWithoutToc(zip: ZipFile): List<ChapterWithText>? {
        val chapters = mutableListOf<ChapterWithText>()
        var chapterTextIndex = -1
        var chapterIndex = 1

        zip.entries().asSequence().sortedBy { it.name }.forEach { entry ->
            if (
                !entry.name.endsWith(".xhtml")
                && !entry.name.endsWith(".html")
                && !entry.name.endsWith(".xml")
                && !entry.name.endsWith(".htm")
                || entry.name.endsWith("container.xml")
            ) return@forEach

            val chapter = zip.parseDocument(entry = entry, fragment = null)
            if (chapter.isEmpty()) {
                Log.w(EPUB_TAG, "Chapter ${entry.name} is empty.")
                return@forEach
            }

            chapters.add(
                ChapterWithText(
                    chapter = Chapter(
                        index = chapters.size,
                        title = "Chapter $chapterIndex", // Generic name, but at least something
                        startIndex = chapterTextIndex + 1,
                        endIndex = chapterTextIndex + chapter.size
                    ),
                    text = chapter
                )
            )
            chapterTextIndex += chapter.size
            chapterIndex++
        }

        if (chapters.isEmpty()) {
            Log.e(EPUB_TAG, "Could not parse file without toc.ncx")
            return null
        }

        return chapters
    }

    /**
     * Parses text with toc.ncx. Extracts all chapters.
     *
     * @return Null if could not parse toc.ncx.
     */
    private fun parseWithToc(tocEntry: ZipEntry, zip: ZipFile): List<ChapterWithText>? {
        Log.i(EPUB_TAG, "TOC Entry: ${tocEntry.name}")

        val chapters = mutableListOf<ChapterWithText>()
        var emptyChapters = 0
        var chapterTextIndex = -1
        var chapterIndex = 1

        val tocContent = zip.getInputStream(tocEntry)
            .bufferedReader()
            .use { it.readText() }
        val tocDocument = Jsoup.parse(tocContent)

        tocDocument.select("navPoint").forEach { navPoint ->
            val chapterTitle = navPoint.selectFirst("navLabel > text")?.text()?.trim()
                ?: "Chapter $chapterIndex"
            val chapterSrc = navPoint.selectFirst("content")?.attr("src")?.trim()
                .run {
                    if (this == null) {
                        Log.e(EPUB_TAG, "No source of the chapter found: $chapterTitle")
                        return null
                    }

                    val uri = Uri.parse(this) ?: return@run this to null
                    (uri.path ?: this) to uri.fragment
                }

            zip.entries().asSequence().find { entry ->
                entry.name.endsWith(chapterSrc.first)
            }.apply {
                if (this == null) {
                    Log.e(EPUB_TAG, "No chapter entry found: $chapterTitle")
                    return null
                }

                val chapter = zip.parseDocument(
                    entry = this,
                    fragment = chapterSrc.second
                ).dropWhile {
                    it == chapterTitle // Remove chapter title if present
                }
                if (chapter.isEmpty()) {
                    Log.w(EPUB_TAG, "Chapter $chapterTitle is empty.")
                    emptyChapters += 1
                    return@forEach
                }

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
                chapterIndex++
            }
        }

        if (chapters.isEmpty()) {
            Log.e(EPUB_TAG, "Could not parse text with toc.ncx")
            return null
        }

        if (emptyChapters >= ((emptyChapters + chapters.size) * 0.25f)) {
            Log.e(EPUB_TAG, "More than 25% of the chapters are empty.")
            return null
        }

        return chapters
    }

    /**
     * Parses [entry] to get it's text.
     *
     * @return Parsed text line by line. Can have line break issues due to bad [entry] formatting.
     */
    private fun ZipFile.parseDocument(entry: ZipEntry, fragment: String?): List<String> {
        val lines = mutableListOf<String>()
        val content = getInputStream(entry)
            .bufferedReader()
            .use {
                it.readText()
            }

        val document = Jsoup.parse(content)
        document.select("p").append("\n")
        document.select("head > title").remove()

        document
            .run {
                fragment?.let { return@run getElementById(it) ?: this }
                this
            }
            .wholeText()
            .lines()
            .forEach { line ->
                if (line.isNotBlank()) {
                    lines.add(line.trim())
                }
            }

        return lines
    }
}