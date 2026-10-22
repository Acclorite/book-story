/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.parser.file

import android.app.Application
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import ua.acclorite.book_story.R
import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.core.ui.UIText
import ua.acclorite.book_story.data.storage.file.CachedFile
import ua.acclorite.book_story.domain.model.book.Book
import ua.acclorite.book_story.domain.model.book.BookProgress
import javax.inject.Inject

private const val TAG = "PdfFileParser"

class PdfFileParser @Inject constructor(
    private val application: Application
) : FileParser {

    override suspend fun parse(cachedFile: CachedFile): Book? {
        return try {
            PDFBoxResourceLoader.init(application)
            val document = PDDocument.load(cachedFile.openInputStream())

            val title = document.documentInformation.title?.takeIf { it.isNotBlank() }
                ?: cachedFile.name.substringBeforeLast(".").trim()
            val author = document.documentInformation.author.run {
                if (isNullOrBlank()) UIText.StringResource(R.string.unknown_author)
                else UIText.StringValue(this)
            }
            val description = document.documentInformation.subject

            document.close()

            Book(
                title = title,
                author = author,
                description = description,
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