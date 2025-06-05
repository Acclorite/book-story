/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.library

import androidx.compose.runtime.Composable
import ua.acclorite.book_story.presentation.library.model.LibraryLayout
import ua.acclorite.book_story.presentation.library.model.SelectableBook

@Composable
fun LibraryLayout(
    books: List<SelectableBook>,
    layout: LibraryLayout,
    gridSize: Int,
    autoGridSize: Boolean,
    itemContent: @Composable (book: SelectableBook) -> Unit
) {
    when (layout) {
        LibraryLayout.LIST -> {
            LibraryListLayout(
                books = books,
                itemContent = itemContent
            )
        }

        LibraryLayout.GRID -> {
            LibraryGridLayout(
                books = books,
                gridSize = gridSize,
                autoGridSize = autoGridSize,
                itemContent = itemContent
            )
        }
    }
}