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

                        yield()
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
     * Parses text if no toc.ncx found, which is Table of Content.
     *
     * @return Null if could not parse.
     */
    private suspend fun parseWithoutToc(zip: ZipFile): List<ChapterWithText>? {
        val chapters = mutableListOf<ChapterWithText>()
        var chapterTextIndex = -1
        var chapterIndex = 1

        yield()

        zip.entries().asSequence().sortedBy { it.name }.forEach { entry ->
            yield()

            if (
                !entry.name.endsWith(".xhtml")
                && !entry.name.endsWith(".html")
                && !entry.name.endsWith(".xml")
                && !entry.name.endsWith(".htm")
                || entry.name.endsWith("container.xml")
            ) return@forEach

            val content = zip.getInputStream(entry)
                .bufferedReader()
                .use {
                    it.readText()
                }

            val chapter = documentParser.run { Jsoup.parse(content).parseDocument(fragment = null) }
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

        yield()

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
    private suspend fun parseWithToc(tocEntry: ZipEntry, zip: ZipFile): List<ChapterWithText>? {
        Log.i(EPUB_TAG, "TOC Entry: ${tocEntry.name}")

        val chapters = mutableListOf<ChapterWithText>()
        var emptyChapters = 0
        var chapterTextIndex = -1
        var chapterIndex = 1

        yield()

        val tocContent = withContext(Dispatchers.IO) {
            zip.getInputStream(tocEntry)
        }.bufferedReader().use { it.readText() }
        val tocDocument = Jsoup.parse(tocContent)

        yield()

        tocDocument.select("navPoint").forEach { navPoint ->
            yield()

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

                val content = zip.getInputStream(this)
                    .bufferedReader()
                    .use {
                        it.readText()
                    }

                val chapter = documentParser.run {
                    Jsoup.parse(content).parseDocument(
                        fragment = chapterSrc.second
                    ).dropWhile {
                        it == chapterTitle // Remove chapter title if present
                    }
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

        yield()

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
}