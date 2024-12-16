package ua.acclorite.book_story.data.parser

import kotlinx.coroutines.yield
import org.jsoup.nodes.Document
import ua.acclorite.book_story.presentation.core.util.clearMarkdown
import ua.acclorite.book_story.presentation.core.util.containsVisibleText
import javax.inject.Inject

class DocumentParser @Inject constructor() {
    /**
     * Parses document to get it's text.
     * Fixes issues such as manual line breaking in <p>.
     * Applies Markdown to the text: Bold(**), Italic(_), Section separator(---), and Links(a > href).
     *
     * @return Parsed text line by line with Markdown(all lines are not blank).
     */
    suspend fun Document.parseDocument(): List<String> {
        val lines = mutableListOf<String>()

        yield()

        body().apply {
            // Remove manual line breaks from all <p>
            select("p").forEach { element ->
                yield()
                element.html(element.html().replace(Regex("\\n+"), " "))
                element.append("\n")
            }

            // Remove <head>'s title
            select("title").remove()

            // Markdown
            select("hr").append("\n---\n")
            select("b").append("**").prepend("**")
            select("h1").append("**").prepend("**")
            select("h2").append("**").prepend("**")
            select("h3").append("**").prepend("**")
            select("strong").append("**").prepend("**")
            select("em").append("_").prepend("_")
            select("a").forEach { element ->
                val link = element.attr("href")
                if (!link.startsWith("http") || element.wholeText().isBlank()) return@forEach

                element.prepend("[")
                element.append("](${element.attr("href")})")
            }
        }.wholeText().lines().forEach { line ->
            yield()

            val formattedLine = line.replace(
                Regex("""\*\*\*\s*(.*?)\s*\*\*\*"""), "_**$1**_"
            ).replace(
                Regex("""\*\*\s*(.*?)\s*\*\*"""), "**$1**"
            ).replace(
                Regex("""_\s*(.*?)\s*_"""), "_$1_"
            ).trim()

            if (formattedLine.clearMarkdown().containsVisibleText()) {
                lines.add(formattedLine)
            }
        }

        yield()

        return lines
    }
}