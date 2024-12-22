package ua.acclorite.book_story.data.parser.html

import android.util.Log
import kotlinx.coroutines.yield
import org.jsoup.Jsoup
import ua.acclorite.book_story.R
import ua.acclorite.book_story.data.parser.DocumentParser
import ua.acclorite.book_story.data.parser.TextParser
import ua.acclorite.book_story.domain.reader.Chapter
import ua.acclorite.book_story.domain.reader.ChapterWithText
import ua.acclorite.book_story.domain.ui.UIText
import ua.acclorite.book_story.domain.util.Resource
import ua.acclorite.book_story.presentation.core.util.clearAllMarkdown
import java.io.File
import javax.inject.Inject

private const val HTML_TAG = "HTML Parser"

class HtmlTextParser @Inject constructor(
    private val documentParser: DocumentParser
) : TextParser {

    override suspend fun parse(file: File): Resource<List<ChapterWithText>> {
        Log.i(HTML_TAG, "Started HTML parsing: ${file.name}.")

        return try {
            val lines = documentParser.run {
                Jsoup.parse(file).parseDocument()
            }.toMutableList()

            yield()

            if (lines.size < 2) {
                Log.e(HTML_TAG, "Could not extract text from HTML.")
                return Resource.Error(UIText.StringResource(R.string.error_file_empty))
            }

            val title = lines.first().clearAllMarkdown().let { title ->
                lines.removeAt(0)
                if (title.isBlank()) return@let "Chapter 1"
                return@let title
            }

            Log.i(HTML_TAG, "Successfully finished HTML parsing.")
            Resource.Success(
                listOf(
                    ChapterWithText(
                        chapter = Chapter(
                            index = 0,
                            title = title,
                            startIndex = 0,
                            endIndex = lines.lastIndex
                        ),
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