/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.parser.file

import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.data.model.file.CachedFile
import ua.acclorite.book_story.domain.model.library.Book
import javax.inject.Inject

private const val TAG = "FileParser"

class FileParserImpl @Inject constructor(
    private val txtFileParser: TxtFileParser,
    private val pdfFileParser: PdfFileParser,
    private val epubFileParser: EpubFileParser,
    private val fb2FileParser: Fb2FileParser,
    private val htmlFileParser: HtmlFileParser,
) : FileParser {

    override suspend fun parse(cachedFile: CachedFile): Book? {
        if (!cachedFile.canAccess()) {
            logE(TAG, "File does not exist or no read access is granted.")
            return null
        }

        val fileFormat = ".${cachedFile.name.substringAfterLast(".")}".lowercase().trim()
        return when (fileFormat) {
            ".pdf" -> {
                pdfFileParser.parse(cachedFile)
            }

            ".epub" -> {
                epubFileParser.parse(cachedFile)
            }

            ".txt" -> {
                txtFileParser.parse(cachedFile)
            }

            ".fb2" -> {
                fb2FileParser.parse(cachedFile)
            }

            ".html" -> {
                htmlFileParser.parse(cachedFile)
            }

            ".htm" -> {
                htmlFileParser.parse(cachedFile)
            }

            ".md" -> {
                txtFileParser.parse(cachedFile)
            }

            else -> {
                logE(TAG, "Wrong file format, could not find supported extension.")
                null
            }
        }
    }
}