/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.parser.text

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.data.model.file.CachedFile
import ua.acclorite.book_story.domain.model.reader.ReaderText
import javax.inject.Inject

private const val TAG = "TextParser"

class TextParserImpl @Inject constructor(
    // Markdown parser (Markdown)
    private val txtTextParser: TxtTextParser,
    private val pdfTextParser: PdfTextParser,

    // Document parser (HTML+Markdown)
    private val epubTextParser: EpubTextParser,
    private val htmlTextParser: HtmlTextParser,
    private val xmlTextParser: XmlTextParser
) : TextParser {

    override suspend fun parse(cachedFile: CachedFile): List<ReaderText> {
        if (!cachedFile.canAccess()) {
            logE(TAG, "File does not exist or no read access is granted.")
            return emptyList()
        }

        val fileFormat = ".${cachedFile.name.substringAfterLast(".")}".lowercase().trim()
        return withContext(Dispatchers.IO) {
            when (fileFormat) {
                ".pdf" -> {
                    pdfTextParser.parse(cachedFile)
                }

                ".epub" -> {
                    epubTextParser.parse(cachedFile)
                }

                ".txt" -> {
                    txtTextParser.parse(cachedFile)
                }

                ".fb2" -> {
                    xmlTextParser.parse(cachedFile)
                }

                ".html" -> {
                    htmlTextParser.parse(cachedFile)
                }

                ".htm" -> {
                    htmlTextParser.parse(cachedFile)
                }

                ".md" -> {
                    htmlTextParser.parse(cachedFile)
                }

                else -> {
                    logE(TAG, "Wrong file format, could not find supported extension.")
                    emptyList()
                }
            }
        }
    }
}