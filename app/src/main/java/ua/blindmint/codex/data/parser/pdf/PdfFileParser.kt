/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.data.parser.pdf

import android.app.Application
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import ua.blindmint.codex.R
import ua.blindmint.codex.data.parser.FileParser
import ua.blindmint.codex.domain.file.CachedFile
import ua.blindmint.codex.domain.library.book.Book
import ua.blindmint.codex.domain.library.book.BookWithCover
import ua.blindmint.codex.domain.library.category.Category
import ua.blindmint.codex.domain.ui.UIText
import javax.inject.Inject

class PdfFileParser @Inject constructor(
    private val application: Application
) : FileParser {

    override suspend fun parse(cachedFile: CachedFile): BookWithCover? {
        return try {
            PDFBoxResourceLoader.init(application)
            val document = PDDocument.load(cachedFile.openInputStream())

            val title = document.documentInformation.title
                ?: cachedFile.name.substringBeforeLast(".").trim()
            val author = document.documentInformation.author.run {
                if (isNullOrBlank()) UIText.StringResource(R.string.unknown_author)
                else UIText.StringValue(this)
            }
            val description = document.documentInformation.subject

            document.close()

            BookWithCover(
                book = Book(
                    title = title,
                    author = author,
                    description = description,
                    scrollIndex = 0,
                    scrollOffset = 0,
                    progress = 0f,
                    filePath = cachedFile.path,
                    lastOpened = null,
                    category = Category.entries[0],
                    coverImage = null
                ),
                coverImage = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}