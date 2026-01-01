/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.parser.file

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import ua.acclorite.book_story.R
import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.core.ui.UIText
import ua.acclorite.book_story.data.model.file.CachedFile
import ua.acclorite.book_story.domain.model.library.Book
import java.util.zip.ZipFile
import javax.inject.Inject

private const val TAG = "EpubFileParser"

class EpubFileParser @Inject constructor() : FileParser {

    override suspend fun parse(cachedFile: CachedFile): Book? {
        return try {
            var book: Book? = null

            val rawFile = cachedFile.rawFile
            if (rawFile == null || !rawFile.exists() || !rawFile.canRead()) return null

            withContext(Dispatchers.IO) {
                ZipFile(rawFile).use { zip ->
                    val opfEntry = zip.entries().asSequence().find { entry ->
                        entry.name.endsWith(".opf", ignoreCase = true)
                    } ?: return@withContext

                    val opfContent = zip
                        .getInputStream(opfEntry)
                        .bufferedReader()
                        .use { it.readText() }
                    val document = Jsoup.parse(opfContent, Parser.xmlParser())

                    val title = document.select("metadata > dc|title").text().trim().run {
                        ifBlank {
                            cachedFile.name.substringBeforeLast(".").trim()
                        }
                    }

                    val author = document.select("metadata > dc|creator").text().trim().run {
                        if (isBlank()) {
                            UIText.StringResource(R.string.unknown_author)
                        } else {
                            UIText.StringValue(this)
                        }
                    }

                    val description = Jsoup.parse(
                        document.select("metadata > dc|description").text()
                    ).text().run {
                        ifBlank {
                            null
                        }
                    }

                    book = Book(
                        title = title,
                        author = author,
                        description = description,
                        scrollIndex = 0,
                        scrollOffset = 0,
                        progress = 0f,
                        filePath = cachedFile.path,
                        lastOpened = null,
                        categories = emptyList(),
                        coverImage = null
                    )
                }
            }
            book
        } catch (e: Exception) {
            logE(TAG, "Could not parse file with message: ${e.message}.")
            null
        }
    }
}