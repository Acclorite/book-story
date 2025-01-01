package ua.acclorite.book_story.data.parser.html

import android.util.Log
import kotlinx.coroutines.yield
import org.jsoup.Jsoup
import ua.acclorite.book_story.data.parser.DocumentParser
import ua.acclorite.book_story.data.parser.MarkdownParser
import ua.acclorite.book_story.data.parser.TextParser
import ua.acclorite.book_story.domain.reader.ReaderText
import ua.acclorite.book_story.presentation.core.util.clearAllMarkdown
import java.io.File
import javax.inject.Inject

private const val HTML_TAG = "HTML Parser"

class HtmlTextParser @Inject constructor(
    private val markdownParser: MarkdownParser,
    private val documentParser: DocumentParser
) : TextParser {

    override suspend fun parse(file: File): List<ReaderText> {
        Log.i(HTML_TAG, "Started HTML parsing: ${file.name}.")

        return try {
            var chapterAdded = false
            val documentLines = documentParser.run {
                Jsoup.parse(file).parseDocument()
            }
            val readerText = mutableListOf<ReaderText>()

            for (line in documentLines) {
                yield()

                if (line.isNotBlank()) {
                    when (line) {
                        "***", "---" -> readerText.add(
                            ReaderText.Separator
                        )

                        else -> {
                            if (!chapterAdded && line.clearAllMarkdown().isNotBlank()) {
                                readerText.add(
                                    0, ReaderText.Chapter(
                                        title = line.clearAllMarkdown()
                                    )
                                )
                                chapterAdded = true
                            } else readerText.add(
                                ReaderText.Text(
                                    line = markdownParser.parse(line)
                                )
                            )
                        }
                    }
                }
            }

            yield()

            if (
                readerText.filterIsInstance<ReaderText.Text>().isEmpty() ||
                readerText.filterIsInstance<ReaderText.Chapter>().isEmpty()
            ) {
                Log.e(HTML_TAG, "Could not extract text from HTML.")
                return emptyList()
            }

            Log.i(HTML_TAG, "Successfully finished HTML parsing.")
            readerText
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}