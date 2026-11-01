/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.parser.content.document

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DocumentResourcePathTest {
    @Test
    fun relativePathsKeepDirectoriesCasePlusSignsAndEncodedCharacters() {
        assertEquals(
            "OEBPS/Images/Cover+One 1.png",
            resolveDocumentPath("OEBPS/Text/chapter.xhtml", "../Images/Cover+One%201.png?size=2#image")
        )
        assertEquals("a+b", decodeDocumentPath("a+b"))
        assertEquals("a+b", decodeDocumentPath("a%2Bb"))
    }

    @Test
    fun externalAndEscapingPathsAreRejected() {
        assertNull(resolveDocumentPath("text.xhtml", "../../outside.png"))
        assertNull(resolveDocumentPath("text.xhtml", "https://example.org/image.png"))
        assertNull(resolveDocumentPath("text.xhtml", "//example.org/image.png"))
        assertNull(resolveDocumentPath("text.xhtml", "data:image/png;base64,YQ=="))
    }

    @Test
    fun malformedPercentEncodingDoesNotCrashParsing() {
        assertEquals("images/a%xx.png", resolveDocumentPath("chapter.xhtml", "images/a%xx.png"))
    }
}
