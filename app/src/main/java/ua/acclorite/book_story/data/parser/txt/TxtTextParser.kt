/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.parser.txt

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import ua.acclorite.book_story.core.helpers.clearAllMarkdown
import ua.acclorite.book_story.data.model.file.CachedFile
import ua.acclorite.book_story.data.parser.MarkdownParser
import ua.acclorite.book_story.data.parser.TextParser
import ua.acclorite.book_story.domain.model.reader.ReaderText
import javax.inject.Inject

private const val TXT_TAG = "TXT Parser"

class TxtTextParser @Inject constructor(
    private val markdownParser: MarkdownParser
) : TextParser {

    override suspend fun parse(cachedFile: CachedFile): List<ReaderText> {
        Log.i(TXT_TAG, "Started TXT parsing: ${cachedFile.name}.")

        return try {
            val readerText = mutableListOf<ReaderText>()
            var chapterAdded = false

            withContext(Dispatchers.IO) {
                cachedFile.openInputStream()?.bufferedReader()?.use { reader ->
                    reader.forEachLine { line ->
                        if (line.isNotBlank()) {
                            when (line) {
                                "***", "---" -> readerText.add(
                                    ReaderText.Separator
                                )

                                else -> {
                                    if (!chapterAdded && line.clearAllMarkdown().isNotBlank()) {
                                        readerText.add(
                                            0, ReaderText.Chapter(
                                                title = line.clearAllMarkdown(),
                                                nested = false
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