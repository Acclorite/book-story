/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.reader.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import ua.acclorite.book_story.domain.model.book.Node
import ua.acclorite.book_story.domain.model.book.NodeContent.HeaderLevel
import kotlin.math.roundToInt

@Immutable
sealed class ReaderNode(val node: Node) {
    fun getNodeVerticalOffsetPx(
        nodeIntraOffset: Int,
        textMeasurer: TextMeasurer,
        constraints: Constraints,
        paragraphTextStyle: TextStyle,
        header1TextStyle: TextStyle,
        header2TextStyle: TextStyle,
        header3TextStyle: TextStyle,
        header4TextStyle: TextStyle,
        header5TextStyle: TextStyle,
        header6TextStyle: TextStyle
    ): Int {
        return when (this) {
            is Header -> {
                if (nodeIntraOffset <= 0) return 0

                val result = textMeasurer.measure(
                    title,
                    when (level) {
                        HeaderLevel.H1 -> header1TextStyle
                        HeaderLevel.H2 -> header2TextStyle
                        HeaderLevel.H3 -> header3TextStyle
                        HeaderLevel.H4 -> header4TextStyle
                        HeaderLevel.H5 -> header5TextStyle
                        HeaderLevel.H6 -> header6TextStyle
                    },
                    constraints = constraints
                )

                val lineIndex =
                    result.getLineForOffset(nodeIntraOffset)
                result.getLineTop(lineIndex).roundToInt()
            }

            is Paragraph -> {
                if (nodeIntraOffset <= 0) return 0

                val result = textMeasurer.measure(
                    text = text,
                    style = paragraphTextStyle,
                    constraints = constraints
                )
                val lineIndex =
                    result.getLineForOffset(nodeIntraOffset)
                result.getLineTop(lineIndex).roundToInt()
            }

            is Image, is Separator -> 0
        }
    }

    fun getNodeIntraOffset(
        nodeVerticalOffsetPx: Int,
        textMeasurer: TextMeasurer,
        constraints: Constraints,
        paragraphTextStyle: TextStyle,
        header1TextStyle: TextStyle,
        header2TextStyle: TextStyle,
        header3TextStyle: TextStyle,
        header4TextStyle: TextStyle,
        header5TextStyle: TextStyle,
        header6TextStyle: TextStyle
    ): Int {
        return when (this) {
            is Header -> {
                if (nodeVerticalOffsetPx <= 0) return 0

                val result = textMeasurer.measure(
                    title,
                    when (level) {
                        HeaderLevel.H1 -> header1TextStyle
                        HeaderLevel.H2 -> header2TextStyle
                        HeaderLevel.H3 -> header3TextStyle
                        HeaderLevel.H4 -> header4TextStyle
                        HeaderLevel.H5 -> header5TextStyle
                        HeaderLevel.H6 -> header6TextStyle
                    },
                    constraints = constraints
                )

                result.getOffsetForPosition(Offset(0f, nodeVerticalOffsetPx.toFloat()))
            }

            is Paragraph -> {
                if (nodeVerticalOffsetPx <= 0) return 0

                val result = textMeasurer.measure(
                    text = text,
                    style = paragraphTextStyle,
                    constraints = constraints
                )
                result.getOffsetForPosition(Offset(0f, nodeVerticalOffsetPx.toFloat()))
            }

            is Image, is Separator -> 0
        }
    }

    @Immutable
    data class Header(
        val originalNode: Node,
        val title: AnnotatedString,
        val level: HeaderLevel
    ) : ReaderNode(originalNode)

    @Immutable
    data class Paragraph(
        val originalNode: Node,
        val text: AnnotatedString
    ) : ReaderNode(originalNode)

    @Immutable
    data class Image(
        val originalNode: Node,
        val content: String,
        val caption: String?,
        val originalWidth: Dp,
        val originalHeight: Dp,
        val aspectRatio: Float
    ) : ReaderNode(originalNode)

    @Immutable
    data class Separator(val originalNode: Node) : ReaderNode(originalNode)
}