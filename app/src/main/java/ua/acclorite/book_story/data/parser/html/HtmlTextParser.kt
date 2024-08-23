package ua.acclorite.book_story.data.parser.html

import org.jsoup.Jsoup
import ua.acclorite.book_story.R
import ua.acclorite.book_story.data.parser.TextParser
import ua.acclorite.book_story.domain.util.Resource
import ua.acclorite.book_story.domain.util.UIText
import java.io.File
import javax.inject.Inject


class HtmlTextParser @Inject constructor() : TextParser {

    override suspend fun parse(file: File): Resource<List<String>> {
        if (!file.name.endsWith(".html", true) || !file.exists()) {
            return Resource.Error(UIText.StringResource(R.string.error_wrong_file_format))
        }

        try {
            val lines = mutableListOf<String>()

            val document = Jsoup.parse(file)
            document.select("p").append("\n")
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

            return Resource.Success(lines)
        } catch (e: Exception) {
            e.printStackTrace()
            return Resource.Error(
                UIText.StringResource(
                    R.string.error_query,
                    e.message?.take(40)?.trim() ?: ""
                )
            )
        }
    }
}