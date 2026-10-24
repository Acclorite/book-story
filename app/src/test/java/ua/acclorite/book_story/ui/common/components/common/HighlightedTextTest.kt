/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.common.components.common

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class HighlightedTextTest {
    @Test
    fun `Highlight adds the configured increase to paragraph weight`() {
        val result = getHighlightedText(
            highlight = true,
            text = AnnotatedString("reader"),
            highlightThickness = FontWeight.Normal,
            fontWeightIncrease = 200
        )

        assertEquals(FontWeight.SemiBold, result.weightAt(0, FontWeight.Normal))
        assertEquals(FontWeight.SemiBold, result.weightAt(2, FontWeight.Normal))
        assertEquals(FontWeight.Normal, result.weightAt(3, FontWeight.Normal))
    }

    @Test
    fun `Highlight weight is capped at 900 for heavy headers`() {
        val result = getHighlightedText(
            highlight = true,
            text = AnnotatedString("header"),
            highlightThickness = FontWeight.ExtraBold,
            fontWeightIncrease = 800
        )

        assertEquals(FontWeight.Black, result.weightAt(0, FontWeight.ExtraBold))
        assertEquals(FontWeight.ExtraBold, result.weightAt(3, FontWeight.ExtraBold))
    }

    @Test
    fun `Highlight adds to inline weight across span boundaries`() {
        val original = buildAnnotatedString {
            append("reader")
            addStyle(SpanStyle(fontWeight = FontWeight.Bold), 1, 2)
        }

        val result = getHighlightedText(
            highlight = true,
            text = original,
            highlightThickness = FontWeight.Normal,
            fontWeightIncrease = 100
        )

        assertEquals(FontWeight.Medium, result.weightAt(0, FontWeight.Normal))
        assertEquals(FontWeight.ExtraBold, result.weightAt(1, FontWeight.Normal))
        assertEquals(FontWeight.Medium, result.weightAt(2, FontWeight.Normal))
        assertEquals(FontWeight.Normal, result.weightAt(3, FontWeight.Normal))
    }

    @Test
    fun `Disabled highlighting returns original text and formatting`() {
        val original = AnnotatedString("reader", SpanStyle(fontWeight = FontWeight.Bold))

        val result = getHighlightedText(
            highlight = false,
            text = original,
            highlightThickness = FontWeight.Normal,
            fontWeightIncrease = 200
        )

        assertSame(original, result)
    }

    @Test
    fun `Absolute highlight weights still work without an increase`() {
        val result = getHighlightedText(
            highlight = true,
            text = AnnotatedString("reader"),
            highlightThickness = FontWeight.SemiBold
        )

        assertEquals(FontWeight.SemiBold, result.weightAt(0, FontWeight.Normal))
        assertEquals(FontWeight.Normal, result.weightAt(3, FontWeight.Normal))
    }

    @Test
    fun `Highlight preserves whitespace and character offsets for pagination`() {
        val original = AnnotatedString("  reader\ttext\nnext  ")
        val result = getHighlightedText(
            highlight = true,
            text = original,
            highlightThickness = FontWeight.Normal,
            fontWeightIncrease = 200
        )

        assertEquals(original.text, result.text)
        assertEquals(original.length, result.length)
        assertEquals(FontWeight.SemiBold, result.weightAt(2, FontWeight.Normal))
    }

    private fun AnnotatedString.weightAt(index: Int, default: FontWeight): FontWeight {
        return spanStyles.lastOrNull {
            it.start <= index && index < it.end && it.item.fontWeight != null
        }?.item?.fontWeight ?: default
    }
}
