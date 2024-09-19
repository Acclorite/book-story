package ua.acclorite.book_story.data.parser

import kotlinx.coroutines.yield
import org.jsoup.nodes.Document
import javax.inject.Inject

class DocumentParser @Inject constructor() {
    /**
     * Parses document to get it's text.
     * If [fragment] is not null, searches document for specific [fragment].
     *
     * @return Parsed text line by line.
     */
    suspend fun Document.parseDocument(fragment: String?): List<String> {
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
            .run {
                fragment?.let { return@run getElementById(it) ?: this }
                this
            }
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