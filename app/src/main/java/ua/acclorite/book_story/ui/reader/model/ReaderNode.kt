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
import androidx.compose.ui.unit.Density
import ua.acclorite.book_story.domain.model.book.Node
import ua.acclorite.book_story.domain.model.book.NodeContent
import ua.acclorite.book_story.ui.reader.textBlockStyle
import ua.acclorite.book_story.ui.reader.textBlockPadding
import ua.acclorite.book_story.domain.model.book.NodeContent.HeaderLevel
import kotlin.math.roundToInt

private fun Constraints.insetTextBlock(padding: Int): Constraints = copy(
    minWidth = (minWidth - padding * 2).coerceAtLeast(0),
    maxWidth = if (hasBoundedWidth) (maxWidth - padding * 2).coerceAtLeast(0) else Constraints.Infinity
)

@Immutable
sealed class ReaderNode(val node: Node) {
    fun getNodeVerticalOffsetPx(
        nodeIntraOffset: Int,
        textMeasurer: TextMeasurer,
        constraints: Constraints,
        density: Density,
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
                    result.getLineForOffset(nodeIntraOffset.coerceIn(0, title.length))
                result.getLineTop(lineIndex).roundToInt()
            }

            is Paragraph -> {
                if (nodeIntraOffset <= 0) return 0
                if (node.content is NodeContent.TableRow) {
                    val cells = tableCells()
                    val cell = cells.firstOrNull { nodeIntraOffset >= it.startOffset && nodeIntraOffset < it.endOffset }
                        ?: cells.lastOrNull { it.startOffset <= nodeIntraOffset } ?: return 0
                    val width = tableCellTextWidth(cell.cell, tableColumns(), constraints.maxWidth, density)
                    val result = textMeasurer.measure(
                        cell.text, node.content.textBlockStyle(paragraphTextStyle),
                        constraints = Constraints(minWidth = width, maxWidth = width)
                    )
                    val offset = (nodeIntraOffset - cell.startOffset).coerceIn(0, cell.text.length)
                    return result.getLineTop(result.getLineForOffset(offset)).roundToInt() +
                        with(density) { ReaderTableCellPadding.roundToPx() }
                }
                val padding = with(density) { node.content.textBlockPadding().roundToPx() }

                val result = textMeasurer.measure(
                    text = text,
                    style = node.content.textBlockStyle(paragraphTextStyle),
                    constraints = constraints.insetTextBlock(padding)
                )
                val lineIndex =
                    result.getLineForOffset(nodeIntraOffset.coerceIn(0, text.length))
                result.getLineTop(lineIndex).roundToInt() + padding
            }

            is Image, is Separator -> 0
        }
    }

    fun getNodeIntraOffset(
        nodeVerticalOffsetPx: Int,
        textMeasurer: TextMeasurer,
        constraints: Constraints,
        density: Density,
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
                if (node.content is NodeContent.TableRow) {
                    val y = (nodeVerticalOffsetPx - with(density) { ReaderTableCellPadding.roundToPx() }).coerceAtLeast(0)
                    val measured = tableCells().filter { it.text.isNotEmpty() }.map { cell ->
                        val width = tableCellTextWidth(cell.cell, tableColumns(), constraints.maxWidth, density)
                        cell to textMeasurer.measure(
                            cell.text, node.content.textBlockStyle(paragraphTextStyle),
                            constraints = Constraints(minWidth = width, maxWidth = width)
                        )
                    }
                    val (cell, result) = measured.firstOrNull { it.second.size.height > y }
                        ?: measured.lastOrNull() ?: return 0
                    return cell.startOffset + result.getOffsetForPosition(Offset(0f, y.toFloat()))
                }
                val padding = with(density) { node.content.textBlockPadding().roundToPx() }

                val result = textMeasurer.measure(
                    text = text,
                    style = node.content.textBlockStyle(paragraphTextStyle),
                    constraints = constraints.insetTextBlock(padding)
                )
                result.getOffsetForPosition(Offset(0f, (nodeVerticalOffsetPx - padding).coerceAtLeast(0).toFloat()))
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