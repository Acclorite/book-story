/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.reader

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.ui.common.components.common.StyledText
import ua.acclorite.book_story.ui.reader.model.ReaderTableCell
import ua.acclorite.book_story.ui.reader.model.ReaderTableCellPadding
import ua.acclorite.book_story.ui.reader.model.tableCellWidth
import ua.acclorite.book_story.ui.reader.model.tableColumnX

@Composable
fun ReaderTableRow(
    cells: List<ReaderTableCell>,
    columns: Int,
    style: TextStyle,
    firstRow: Boolean,
    lastRow: Boolean,
    modifier: Modifier = Modifier
) {
    val radius = 16.dp
    val shape = RoundedCornerShape(
        topStart = if (firstRow) radius else 0.dp,
        topEnd = if (firstRow) radius else 0.dp,
        bottomStart = if (lastRow) radius else 0.dp,
        bottomEnd = if (lastRow) radius else 0.dp
    )
    Layout(
        modifier = modifier.clip(shape).drawBehind {
            val stroke = 1.dp.toPx()
            val half = stroke / 2
            val border = style.color.copy(alpha = 0.5f)
            val top = if (firstRow) radius.toPx().coerceAtMost(size.minDimension / 2) else 0f
            val bottom = if (lastRow) radius.toPx().coerceAtMost(size.minDimension / 2) else 0f
            fun x(column: Int): Float {
                val value = tableColumnX(column, columns, size.width.toInt()).toFloat()
                return if (layoutDirection == LayoutDirection.Rtl) size.width - value else value
            }

            cells.forEach { item ->
                val left = minOf(x(item.cell.column), x(item.cell.column + item.cell.columnSpan))
                val right = maxOf(x(item.cell.column), x(item.cell.column + item.cell.columnSpan))
                if (item.cell.header) {
                    drawRect(style.color.copy(alpha = 0.05f), Offset(left, 0f), Size(right - left, size.height))
                }
                val boundary = item.cell.column + item.cell.columnSpan
                if (boundary < columns) {
                    drawLine(border, Offset(x(boundary), 0f), Offset(x(boundary), size.height), stroke)
                }
                if (!lastRow && item.cell.rowSpan == 1) {
                    drawLine(border, Offset(left, size.height - half), Offset(right, size.height - half), stroke)
                }
            }
            drawLine(border, Offset(half, top), Offset(half, size.height - bottom), stroke)
            drawLine(border, Offset(size.width - half, top), Offset(size.width - half, size.height - bottom), stroke)
            if (firstRow) {
                drawLine(border, Offset(top, half), Offset(size.width - top, half), stroke)
                val diameter = (top * 2 - stroke).coerceAtLeast(0f)
                if (diameter > 0) {
                    drawArc(border, 180f, 90f, false, Offset(half, half), Size(diameter, diameter), style = Stroke(stroke))
                    drawArc(border, 270f, 90f, false, Offset(size.width - top * 2 + half, half), Size(diameter, diameter), style = Stroke(stroke))
                }
            }
            if (lastRow) {
                drawLine(border, Offset(bottom, size.height - half), Offset(size.width - bottom, size.height - half), stroke)
                val diameter = (bottom * 2 - stroke).coerceAtLeast(0f)
                if (diameter > 0) {
                    drawArc(border, 90f, 90f, false, Offset(half, size.height - bottom * 2 + half), Size(diameter, diameter), style = Stroke(stroke))
                    drawArc(border, 0f, 90f, false, Offset(size.width - bottom * 2 + half, size.height - bottom * 2 + half), Size(diameter, diameter), style = Stroke(stroke))
                }
            }
        },
        content = {
            cells.forEach { cell ->
                StyledText(
                    text = cell.text,
                    style = style,
                    modifier = Modifier.padding(ReaderTableCellPadding)
                )
            }
        }
    ) { measurables, constraints ->
        val width = constraints.maxWidth
        val placeables = measurables.mapIndexed { index, measurable ->
            val cellWidth = tableCellWidth(cells[index].cell, columns, width)
            measurable.measure(Constraints(minWidth = cellWidth, maxWidth = cellWidth))
        }
        val height = (placeables.maxOfOrNull { it.height } ?: 0).coerceIn(constraints.minHeight, constraints.maxHeight)
        layout(width, height) {
            placeables.forEachIndexed { index, placeable ->
                placeable.placeRelative(tableColumnX(cells[index].cell.column, columns, width), 0)
            }
        }
    }
}
