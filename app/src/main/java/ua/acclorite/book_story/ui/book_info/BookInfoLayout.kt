/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.book_info

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.domain.model.library.Book
import ua.acclorite.book_story.presentation.book_info.BookInfoEvent
import ua.acclorite.book_story.ui.common.components.common.LazyColumnWithScrollbar
import ua.acclorite.book_story.ui.common.data.ScrollbarData

@Composable
fun BookInfoLayout(
    book: Book,
    listState: LazyListState,
    paddingValues: PaddingValues,
    showChangeCoverBottomSheet: (BookInfoEvent.OnShowChangeCoverBottomSheet) -> Unit,
    showTitleDialog: (BookInfoEvent.OnShowTitleDialog) -> Unit,
    showAuthorDialog: (BookInfoEvent.OnShowAuthorDialog) -> Unit,
    showDescriptionDialog: (BookInfoEvent.OnShowDescriptionDialog) -> Unit,
    showMoveDialog: (BookInfoEvent.OnShowMoveDialog) -> Unit,
    showDeleteDialog: (BookInfoEvent.OnShowDeleteDialog) -> Unit,
    navigateToReader: (BookInfoEvent.OnNavigateToReader) -> Unit
) {
    LazyColumnWithScrollbar(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        scrollbarSettings = ScrollbarData.primaryScrollbar,
        contentPadding = PaddingValues(bottom = 18.dp)
    ) {
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                if (book.coverImage != null) {
                    BookInfoLayoutBackground(
                        height = paddingValues.calculateTopPadding() + 232.dp,
                        image = book.coverImage
                    )
                }

                Column(Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.height(paddingValues.calculateTopPadding() + 12.dp))
                    BookInfoLayoutInfo(
                        book = book,
                        showTitleDialog = showTitleDialog,
                        showAuthorDialog = showAuthorDialog,
                        showChangeCoverBottomSheet = showChangeCoverBottomSheet
                    )
                }
            }
        }

        item {
            Spacer(Modifier.height(18.dp))
            BookInfoLayoutActions(
                showMoveDialog = showMoveDialog,
                showDeleteDialog = showDeleteDialog
            )
        }

        item {
            Spacer(Modifier.height(18.dp))
            BookInfoLayoutDescription(
                book = book,
                showDescriptionDialog = showDescriptionDialog
            )
        }

        item {
            Spacer(Modifier.height(18.dp))
            BookInfoLayoutButton(
                book = book,
                navigateToReader = navigateToReader
            )
        }
    }
}