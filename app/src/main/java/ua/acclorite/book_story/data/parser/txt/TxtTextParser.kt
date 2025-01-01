package ua.acclorite.book_story.data.parser.txt

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import ua.acclorite.book_story.data.parser.MarkdownParser
import ua.acclorite.book_story.data.parser.TextParser
import ua.acclorite.book_story.domain.reader.ReaderText
import ua.acclorite.book_story.presentation.core.util.clearAllMarkdown
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import javax.inject.Inject

private const val TXT_TAG = "TXT Parser"

class TxtTextParser @Inject constructor(
    private val markdownParser: MarkdownParser
) : TextParser {

    override suspend fun parse(file: File): List<ReaderText> {
        Log.i(TXT_TAG, "Started TXT parsing: ${file.name}.")

        return try {
            val readerText = mutableListOf<ReaderText>()
            var chapterAdded = false

            withContext(Dispatchers.IO) {
                BufferedReader(FileReader(file)).forEachLine { line ->
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
            }

            yield()

            if (
                readerText.filterIsInstance<ReaderText.Text>().isEmpty() ||
                readerText.filterIsInstance<ReaderText.Chapter>().isEmpty()
            ) {
                Log.e(TXT_TAG, "Could not extract text from TXT.")
                return emptyList()
            }

            Log.i(TXT_TAG, "Successfully finished TXT parsing.")
            readerText
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}