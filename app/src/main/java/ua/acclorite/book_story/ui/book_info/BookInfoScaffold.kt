/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.book_info

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ua.acclorite.book_story.domain.model.library.Book
import ua.acclorite.book_story.presentation.book_info.BookInfoEvent

@Composable
fun BookInfoScaffold(
    book: Book,
    listState: LazyListState,
    showChangeCoverBottomSheet: (BookInfoEvent.OnShowChangeCoverBottomSheet) -> Unit,
    showDetailsBottomSheet: (BookInfoEvent.OnShowDetailsBottomSheet) -> Unit,
    showTitleDialog: (BookInfoEvent.OnShowTitleDialog) -> Unit,
    showAuthorDialog: (BookInfoEvent.OnShowAuthorDialog) -> Unit,
    showDescriptionDialog: (BookInfoEvent.OnShowDescriptionDialog) -> Unit,
    showMoveDialog: (BookInfoEvent.OnShowMoveDialog) -> Unit,
    showDeleteDialog: (BookInfoEvent.OnShowDeleteDialog) -> Unit,
    navigateToReader: (BookInfoEvent.OnNavigateToReader) -> Unit,
    navigateBack: (BookInfoEvent.OnNavigateBack) -> Unit
) {
    Scaffold(
        Modifier
            .fillMaxSize()
            .imePadding()
            .windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            BookInfoTopBar(
                book = book,
                listState = listState,
                showDetailsBottomSheet = showDetailsBottomSheet,
                navigateBack = navigateBack
            )
        }
    ) { paddingValues ->
        BookInfoLayout(
            book = book,
            listState = listState,
            paddingValues = paddingValues,
            showTitleDialog = showTitleDialog,
            showAuthorDialog = showAuthorDialog,
            showDescriptionDialog = showDescriptionDialog,
            showChangeCoverBottomSheet = showChangeCoverBottomSheet,
            showMoveDialog = showMoveDialog,
            showDeleteDialog = showDeleteDialog,
            navigateToReader = navigateToReader
        )
    }
}