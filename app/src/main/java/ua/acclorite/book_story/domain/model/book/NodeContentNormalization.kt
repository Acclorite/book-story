/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.model.book

import ua.acclorite.book_story.core.helpers.cleanHeaderText
import ua.acclorite.book_story.core.helpers.containsVisibleText

fun NodeContent.normalized(): NodeContent? = when (this) {
    is NodeContent.Header -> title.cleanHeaderText().takeIf { it.containsVisibleText() }?.let { copy(title = it) }
    is NodeContent.TextBlock -> {
        if (!text.containsVisibleText() && !(this is NodeContent.TableRow && cells.isNotEmpty())) null
        else {
            val valid = styles.mapNotNull {
                val start = it.start.coerceIn(0, text.length)
                val end = it.end.coerceIn(0, text.length)
                if (start < end) it.copy(start = start, end = end) else null
            }.distinct()
            when (this) {
                is NodeContent.Paragraph -> copy(styles = valid)
                is NodeContent.Quote -> copy(styles = valid, depth = depth.coerceIn(1, 12))
                is NodeContent.CodeBlock -> copy(styles = valid)
                is NodeContent.Verse -> copy(styles = valid)
                is NodeContent.ListItem -> copy(styles = valid, depth = depth.coerceIn(1, 12))
                is NodeContent.Subtitle -> copy(styles = valid)
                is NodeContent.TableRow -> copy(
                    styles = valid,
                    cells = cells.map { cell ->
                        val start = cell.start.coerceIn(0, text.length)
                        cell.copy(
                            start = start,
                            end = cell.end.coerceIn(start, text.length),
                            column = cell.column.coerceIn(0, 999),
                            columnSpan = cell.columnSpan.coerceIn(1, 1000 - cell.column.coerceIn(0, 999)),
                            rowSpan = cell.rowSpan.coerceIn(1, 65534)
                        )
                    },
                    columns = columns.coerceIn(0, 1000)
                )
                is NodeContent.Caption -> copy(styles = valid)
            }
        }
    }
    is NodeContent.Image -> if (content.isBlank()) null else this
    is NodeContent.Separator -> this
}
