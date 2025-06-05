/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.library

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.presentation.library.model.SelectableBook
import ua.acclorite.book_story.ui.common.components.common.LazyColumnWithScrollbar
import ua.acclorite.book_story.ui.common.data.ScrollbarData

@Composable
fun LibraryListLayout(
    books: List<SelectableBook>,
    itemContent: @Composable (book: SelectableBook) -> Unit
) {
    LazyColumnWithScrollbar(
        modifier = Modifier.fillMaxSize(),
        scrollbarSettings = ScrollbarData.primaryScrollbar,
        contentPadding = PaddingValues(8.dp)
    ) {
        items(
            books,
            key = { it.data.id }
        ) { book ->
            Box(modifier = Modifier.animateItem()) {
                itemContent(book)
            }
        }
    }
}