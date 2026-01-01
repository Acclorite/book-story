/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.parser.cover

import ua.acclorite.book_story.core.CoverImage
import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.data.model.file.CachedFile
import javax.inject.Inject

private const val TAG = "CoverParser"

class CoverParserImpl @Inject constructor(
    private val epubCoverParser: EpubCoverParser,
    private val txtCoverParser: TxtCoverParser,
    private val pdfCoverParser: PdfCoverParser,
    private val fb2CoverParser: Fb2CoverParser,
    private val htmlCoverParser: HtmlCoverParser
) : CoverParser {

    override suspend fun parse(cachedFile: CachedFile): CoverImage? {
        if (!cachedFile.canAccess()) {
            logE(TAG, "File does not exist or no read access is granted.")
            return null
        }

        val fileFormat = ".${cachedFile.name.substringAfterLast(".")}".lowercase().trim()
        return when (fileFormat) {
            ".pdf" -> {
                pdfCoverParser.parse(cachedFile)
            }

            ".epub" -> {
                epubCoverParser.parse(cachedFile)
            }

            ".txt" -> {
                txtCoverParser.parse(cachedFile)
            }

            ".fb2" -> {
                fb2CoverParser.parse(cachedFile)
            }

            ".html" -> {
                htmlCoverParser.parse(cachedFile)
            }

            ".htm" -> {
                htmlCoverParser.parse(cachedFile)
            }

            ".md" -> {
                txtCoverParser.parse(cachedFile)
            }

            else -> {
                logE(TAG, "Wrong file format, could not find supported extension.")
                null
            }
        }
    }
}