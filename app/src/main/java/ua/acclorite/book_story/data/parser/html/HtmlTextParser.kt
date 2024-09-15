package ua.acclorite.book_story.data.parser.html

import android.util.Log
import org.jsoup.Jsoup
import ua.acclorite.book_story.R
import ua.acclorite.book_story.data.parser.TextParser
import ua.acclorite.book_story.domain.model.Chapter
import ua.acclorite.book_story.domain.model.ChapterWithText
import ua.acclorite.book_story.domain.util.Resource
import ua.acclorite.book_story.domain.util.UIText
import java.io.File
import javax.inject.Inject

private const val HTML_TAG = "HTML Parser"

class HtmlTextParser @Inject constructor() : TextParser {

    override suspend fun parse(file: File): Resource<List<ChapterWithText>> {
        Log.i(HTML_TAG, "Started HTML parsing: ${file.name}.")

        return try {
            val lines = mutableListOf<String>()

            val document = Jsoup.parse(file)
            document.select("p").append("\n")
            document.select("head > title").remove()

            document
                .wholeText()
                .lines()
                .forEach { line ->
                    if (line.isNotBlank()) {
                        lines.add(line.trim())
                    }
                }

            if (lines.isEmpty()) {
                return Resource.Error(UIText.StringResource(R.string.error_file_empty))
            }

            Log.i(HTML_TAG, "Successfully finished HTML parsing.")
            Resource.Success(
                listOf(
                    ChapterWithText(
                        chapter = Chapter(title = "", startIndex = 0, endIndex = 0),
                        text = lines
                    )
                )
            )
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
}