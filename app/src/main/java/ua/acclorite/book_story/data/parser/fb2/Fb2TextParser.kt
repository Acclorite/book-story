package ua.acclorite.book_story.data.parser.fb2

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import ua.acclorite.book_story.data.parser.MarkdownParser
import ua.acclorite.book_story.data.parser.TextParser
import ua.acclorite.book_story.domain.reader.ReaderText
import ua.acclorite.book_story.presentation.core.util.clearAllMarkdown
import java.io.File
import javax.inject.Inject
import javax.xml.parsers.DocumentBuilderFactory

private const val FB2_TAG = "FB2 Parser"

class Fb2TextParser @Inject constructor(
    private val markdownParser: MarkdownParser
) : TextParser {

    override suspend fun parse(file: File): List<ReaderText> {
        Log.i(FB2_TAG, "Started FB2 parsing: ${file.name}.")

        return try {
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val document = withContext(Dispatchers.IO) {
                builder.parse(file)
            }

            val readerText = mutableListOf<ReaderText>()

            val bodyNodes = document.getElementsByTagName("body")
            if (bodyNodes.length == 0) {
                return emptyList()
            }

            yield()

            val unformattedLines = mutableListOf<String>()
            val bodyNode = bodyNodes.item(0) as Element
            val paragraphNodes = bodyNode.getElementsByTagName("p")

            for (element in paragraphNodes.asList()) {
                yield()

                if (element.textContent.isBlank()) {
                    continue
                }

                unformattedLines.add(
                    element.textContent.trim()
                )
            }

            yield()

            val lines = mutableListOf<String>()
            unformattedLines.forEachIndexed { index, string ->
                try {
                    yield()

                    val line = string.trim()

                    if (index == 0) {
                        lines.add(line)
                        return@forEachIndexed
                    }

                    if (line.all { it.isDigit() }) {
                        return@forEachIndexed
                    }

                    if (line.first().isLowerCase()) {
                        val currentLine = lines[lines.lastIndex]

                        if (currentLine.last() == '-') {
                            if (currentLine[currentLine.lastIndex - 1].isLowerCase()) {
                                lines[lines.lastIndex] = currentLine.dropLast(1) + line
                                return@forEachIndexed
                            }
                        }

                        lines[lines.lastIndex] += " $line"
                        return@forEachIndexed
                    }

                    if (line.first().isUpperCase() || line.first().isDigit()) {
                        lines.add(line)
                        return@forEachIndexed
                    }

                    if (line.first().isLetter()) {
                        lines[lines.lastIndex] += " $line"
                        return@forEachIndexed
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    return@forEachIndexed
                }
            }

            yield()

            var chapterAdded = false
            lines.forEach { line ->
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
                Log.e(FB2_TAG, "Could not extract text from FB2.")
                return emptyList()
            }

            Log.i(FB2_TAG, "Successfully finished FB2 parsing.")
            readerText
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private fun NodeList.asList(): List<Element> {
        val list = mutableListOf<Element>()
        for (i in 0 until this.length) {
            list.add(this.item(i) as Element)
        }
        return list
    }
}