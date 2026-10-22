/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.parser.file

import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import ua.acclorite.book_story.R
import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.core.ui.UIText
import ua.acclorite.book_story.data.storage.file.CachedFile
import ua.acclorite.book_story.domain.model.book.Book
import ua.acclorite.book_story.domain.model.book.BookProgress
import javax.inject.Inject

private const val TAG = "HtmlFileParser"

class HtmlFileParser @Inject constructor() : FileParser {

    override suspend fun parse(cachedFile: CachedFile): Book? {
        return try {
            val document = cachedFile.openInputStream()?.use {
                Jsoup.parse(it, null, "", Parser.htmlParser())
            }

            val title = document?.select("head > title")?.text()?.trim().run {
                if (isNullOrBlank()) {
                    return@run cachedFile.name.substringBeforeLast(".").trim()
                }
                return@run this
            }

            Book(
                title = title,
                author = UIText.StringResource(R.string.unknown_author),
                description = null,
                progress = BookProgress.default,
                filePath = cachedFile.path,
                lastOpened = null,
                categories = emptyList(),
                coverImage = null
            )
        } catch (e: Exception) {
            logE(TAG, "Could not parse file with message: ${e.message}.")
            null
        }
    }
}