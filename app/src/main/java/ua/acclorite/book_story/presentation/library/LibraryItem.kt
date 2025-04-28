/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.library

import androidx.compose.runtime.Composable
import ua.acclorite.book_story.domain.library.book.SelectableBook
import ua.acclorite.book_story.domain.library.display.LibraryLayout
import ua.acclorite.book_story.domain.library.display.LibraryTitlePosition

@Composable
fun LibraryItem(
    book: SelectableBook,
    layout: LibraryLayout,
    hasSelectedItems: Boolean,
    titlePosition: LibraryTitlePosition,
    readButton: Boolean,
    showProgress: Boolean,
    selectBook: (select: Boolean?) -> Unit,
    navigateToBookInfo: () -> Unit,
    navigateToReader: () -> Unit
) {
    when (layout) {
        LibraryLayout.LIST -> {
            LibraryListItem(
                book = book,
                hasSelectedItems = hasSelectedItems,
                readButton = readButton,
                showProgress = showProgress,
                selectBook = selectBook,
                navigateToBookInfo = navigateToBookInfo,
                navigateToReader = navigateToReader
            )
        }

        LibraryLayout.GRID -> {
            LibraryGridItem(
                book = book,
                hasSelectedItems = hasSelectedItems,
                titlePosition = titlePosition,
                readButton = readButton,
                showProgress = showProgress,
                selectBook = selectBook,
                navigateToBookInfo = navigateToBookInfo,
                navigateToReader = navigateToReader
            )
        }
    }
}