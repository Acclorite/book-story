package ua.acclorite.book_story.data.parser

import kotlinx.coroutines.yield
import org.jsoup.nodes.Document
import ua.acclorite.book_story.domain.reader.ReaderText
import ua.acclorite.book_story.presentation.core.util.clearAllMarkdown
import ua.acclorite.book_story.presentation.core.util.clearMarkdown
import ua.acclorite.book_story.presentation.core.util.containsVisibleText
import javax.inject.Inject

class DocumentParser @Inject constructor(
    private val markdownParser: MarkdownParser
) {
    /**
     * Parses document to get it's text.
     * Fixes issues such as manual line breaking in <p>.
     * Applies Markdown to the text: Bold(**), Italic(_), Section separator(---), and Links(a > href).
     *
     * @return Parsed text line by line with Markdown(all lines are not blank).
     */
    suspend fun Document.parseDocument(includeChapter: Boolean = true): List<ReaderText> {
        yield()

        val readerText = mutableListOf<ReaderText>()
        var chapterAdded = false

        body().apply {
            // Remove manual line breaks from all <p>, <a>
            select("p, a").forEach { element ->
                yield()
                element.html(element.html().replace(Regex("\\n+"), " "))
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

                element.prepend("[")/* text in between */.append("](${element.attr("href")})")
            }
        }.wholeText().lines().forEachIndexed { index, line ->
            yield()

            val formattedLine = line.replace(
                Regex("""\*\*\*\s*(.*?)\s*\*\*\*"""), "_**$1**_"
            ).replace(
                Regex("""\*\*\s*(.*?)\s*\*\*"""), "**$1**"
            ).replace(
                Regex("""_\s*(.*?)\s*_"""), "_$1_"
            ).trim()

            if (line.containsVisibleText()) {
                when (line) {
                    "***", "---" -> readerText.add(ReaderText.Separator)

                    else -> {
                        if (
                            !chapterAdded &&
                            formattedLine.clearAllMarkdown().containsVisibleText() &&
                            includeChapter
                        ) {
                            readerText.add(
                                0, ReaderText.Chapter(
                                    title = formattedLine.clearAllMarkdown()
                                )
                            )
                            chapterAdded = true
                        } else if (
                            formattedLine.clearMarkdown().containsVisibleText()
                        ) {
                            readerText.add(
                                ReaderText.Text(
                                    line = markdownParser.parse(formattedLine)
                                )
                            )
                        }
                    }
                }
            }
        }

        yield()

        if (
            readerText.filterIsInstance<ReaderText.Text>().isEmpty() ||
            (includeChapter && readerText.filterIsInstance<ReaderText.Chapter>().isEmpty())
        ) {
            return emptyList()
        }

        return readerText
    }
}