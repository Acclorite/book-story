/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.core.data

object ParsersData {
    private const val JSOUP_PARSER = 1
    private const val EPUB_PARSER = 1
    private const val PDF_PARSER = 1
    private const val HTML_PARSER = 1
    private const val TXT_PARSER = 1
    private const val XML_PARSER = 1

    fun getCurrentParserVersion(parser: String): Int {
        when (parser.lowercase().trim()) {
            "epub" -> {
                return EPUB_PARSER + JSOUP_PARSER
            }

            "pdf" -> {
                return PDF_PARSER
            }

            "html" -> {
                return HTML_PARSER + JSOUP_PARSER
            }

            "txt" -> {
                return TXT_PARSER
            }

            "xml" -> {
                return XML_PARSER + JSOUP_PARSER
            }

            else -> {
                error("Incorrect parser.")
            }
        }
    }
}