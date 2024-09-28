package ua.acclorite.book_story.data.parser

import kotlinx.coroutines.yield
import org.jsoup.nodes.Document
import javax.inject.Inject

class DocumentParser @Inject constructor() {
    /**
     * Parses document to get it's text.
     *
     * @return Parsed text line by line.
     */
    suspend fun Document.parseDocument(): List<String> {
        val lines = mutableListOf<String>()

        yield()

        body()
            .select("p")
            .apply {
                forEach { element ->
                    yield()

                    val cleanedText = element.html().replace(Regex("\\n+"), " ")
                    element.html(cleanedText)
                }

                append("\n")
            }

        yield()

        body()
            .wholeText()
            .lines()
            .forEach { line ->
                yield()
                if (line.isNotBlank()) {
                    lines.add(line.trim())
                }
            }

        yield()

        return lines
    }
}