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
import ua.acclorite.book_story.data.model.file.CachedFile
import ua.acclorite.book_story.domain.model.library.Book
import javax.inject.Inject

private const val TAG = "Fb2FileParser"

class Fb2FileParser @Inject constructor() : FileParser {

    override suspend fun parse(cachedFile: CachedFile): Book? {
        return try {
            val document = cachedFile.openInputStream()?.use {
                Jsoup.parse(it, null, "", Parser.xmlParser())
            }

            val title = document?.selectFirst("book-title")?.text()?.trim().run {
                if (isNullOrBlank()) {
                    return@run cachedFile.name.substringBeforeLast(".").trim()
                }
                this
            }

            val author = document?.selectFirst("author")?.text()?.trim().run {
                if (isNullOrBlank()) {
                    return@run UIText.StringResource(R.string.unknown_author)
                }
                UIText.StringValue(this.trim())
            }

            val description = document?.selectFirst("annotation")?.text()?.trim().run {
                if (isNullOrBlank()) {
                    return@run null
                }
                this
            }

            Book(
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
        } catch (e: Exception) {
            logE(TAG, "Could not parse file with message: ${e.message}.")
            null
        }
    }
}