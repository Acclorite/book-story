/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.reader

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.domain.model.book.NodeContent

fun NodeContent.textBlockStyle(base: TextStyle): TextStyle = when (this) {
    is NodeContent.Quote -> base.copy(
        textAlign = TextAlign.Start,
        textIndent = TextIndent(
            firstLine = base.fontSize * (0.9f * depth.coerceIn(1, 4)),
            restLine = base.fontSize * (0.9f * depth.coerceIn(1, 4))
        ),
        fontStyle = FontStyle.Italic
    )
    is NodeContent.CodeBlock -> base.copy(
        fontFamily = FontFamily.Monospace,
        fontSize = base.fontSize * 0.9f,
        textAlign = TextAlign.Start,
        textIndent = TextIndent.None
    )
    is NodeContent.Verse -> base.copy(textAlign = TextAlign.Start, textIndent = TextIndent.None)
    is NodeContent.ListItem -> base.copy(
        textAlign = TextAlign.Start,
        textIndent = TextIndent(
            firstLine = base.fontSize * (0.75f * (depth - 1).coerceIn(0, 3)),
            restLine = base.fontSize * (0.75f * depth.coerceIn(1, 4) + 0.5f)
        )
    )
    is NodeContent.Subtitle -> base.copy(
        fontSize = base.fontSize * 1.1f,
        fontWeight = FontWeight(((base.fontWeight?.weight ?: 400) + 100).coerceAtMost(900)),
        textIndent = TextIndent.None
    )
    is NodeContent.Caption -> base.copy(
        fontSize = base.fontSize * 0.9f,
        fontStyle = FontStyle.Italic,
        textIndent = TextIndent.None
    )
    is NodeContent.TableRow -> base.copy(
        textAlign = TextAlign.Start,
        textIndent = TextIndent.None
    )
    else -> base
}

fun NodeContent.textBlockPadding(): Dp = if (this is NodeContent.CodeBlock) 14.dp else 0.dp

@Composable
fun Modifier.readerTextBlockDecoration(
    content: NodeContent,
    color: Color,
    quoteGapAfter: Dp = 0.dp
): Modifier = when (content) {
    is NodeContent.CodeBlock -> clip(MaterialTheme.shapes.large)
        .background(color.copy(alpha = 0.07f))
        .padding(content.textBlockPadding())

    else -> drawBehind {
        when (content) {
            is NodeContent.Quote -> drawLine(
                color = color.copy(alpha = 0.35f),
                start = Offset(2f, 0f),
                end = Offset(2f, size.height + quoteGapAfter.toPx()),
                strokeWidth = 2f
            )
            is NodeContent.TableRow -> drawLine(
                color = color.copy(alpha = 0.2f),
                start = Offset(0f, size.height - 1f),
                end = Offset(size.width, size.height - 1f)
            )
            else -> Unit
        }
    }
}
