package ua.acclorite.book_story.data.parser.htm

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

private const val HTM_TAG = "HTM Parser"

class HtmTextParser @Inject constructor() : TextParser {

    override suspend fun parse(file: File): Resource<List<ChapterWithText>> {
        Log.i(HTM_TAG, "Started HTM parsing: ${file.name}.")

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

            Log.i(HTM_TAG, "Successfully finished HTM parsing.")
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