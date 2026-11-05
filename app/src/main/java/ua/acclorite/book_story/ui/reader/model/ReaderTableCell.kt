/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.reader.model

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.domain.model.book.NodeContent

internal val ReaderTableCellPadding = 14.dp

data class ReaderTableCell(
    val cell: NodeContent.TableCell,
    val text: AnnotatedString,
    val startOffset: Int = cell.start,
    val endOffset: Int = cell.end
)

fun ReaderNode.Paragraph.tableColumns(): Int {
    val row = node.content as NodeContent.TableRow
    return maxOf(1, row.columns, row.cells.maxOfOrNull { it.column + it.columnSpan } ?: 1)
}

fun ReaderNode.Paragraph.tableCells(): List<ReaderTableCell> {
    val row = node.content as NodeContent.TableRow
    val cells = row.cells.ifEmpty { listOf(NodeContent.TableCell(0, row.text.length, 0, header = row.header)) }
        .sortedBy { it.column }
    val result = mutableListOf<ReaderTableCell>()
    var column = 0
    cells.forEach { cell ->
        if (cell.column < column) return@forEach
        while (column < cell.column) {
            result.add(ReaderTableCell(NodeContent.TableCell(0, 0, column++), AnnotatedString("")))
        }
        val start = toDisplayOffset(cell.start).coerceIn(0, text.length)
        val end = toDisplayOffset(cell.end).coerceIn(start, text.length)
        result.add(ReaderTableCell(cell, text.subSequence(start, end), start, end))
        column = cell.column + cell.columnSpan
    }
    while (column < tableColumns()) {
        result.add(ReaderTableCell(NodeContent.TableCell(0, 0, column++), AnnotatedString("")))
    }
    return result
}

fun tableColumnX(column: Int, columns: Int, width: Int): Int =
    (width.toLong() * column / columns.coerceAtLeast(1)).toInt()

fun tableCellWidth(cell: NodeContent.TableCell, columns: Int, width: Int): Int =
    (tableColumnX(cell.column + cell.columnSpan, columns, width) -
        tableColumnX(cell.column, columns, width)).coerceAtLeast(1)

fun tableCellTextWidth(cell: NodeContent.TableCell, columns: Int, width: Int, density: Density): Int =
    (tableCellWidth(cell, columns, width) - with(density) { ReaderTableCellPadding.roundToPx() * 2 })
        .coerceAtLeast(1)
