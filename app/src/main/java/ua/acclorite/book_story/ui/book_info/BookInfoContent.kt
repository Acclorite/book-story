/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.book_info

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import ua.acclorite.book_story.core.BottomSheet
import ua.acclorite.book_story.core.Dialog
import ua.acclorite.book_story.domain.model.file.File
import ua.acclorite.book_story.domain.model.library.Book
import ua.acclorite.book_story.domain.model.library.Category
import ua.acclorite.book_story.presentation.book_info.BookInfoEvent

@Composable
fun BookInfoContent(
    book: Book,
    file: File?,
    categories: List<Category>,
    bottomSheet: BottomSheet?,
    dialog: Dialog?,
    listState: LazyListState,
    canResetCover: Boolean,
    showChangeCoverBottomSheet: (BookInfoEvent.OnShowChangeCoverBottomSheet) -> Unit,
    showDetailsBottomSheet: (BookInfoEvent.OnShowDetailsBottomSheet) -> Unit,
    showTitleDialog: (BookInfoEvent.OnShowTitleDialog) -> Unit,
    actionTitleDialog: (BookInfoEvent.OnActionTitleDialog) -> Unit,
    showAuthorDialog: (BookInfoEvent.OnShowAuthorDialog) -> Unit,
    actionAuthorDialog: (BookInfoEvent.OnActionAuthorDialog) -> Unit,
    showDescriptionDialog: (BookInfoEvent.OnShowDescriptionDialog) -> Unit,
    actionDescriptionDialog: (BookInfoEvent.OnActionDescriptionDialog) -> Unit,
    showPathDialog: (BookInfoEvent.OnShowPathDialog) -> Unit,
    actionPathDialog: (BookInfoEvent.OnActionPathDialog) -> Unit,
    showMoveDialog: (BookInfoEvent.OnShowMoveDialog) -> Unit,
    showDeleteDialog: (BookInfoEvent.OnShowDeleteDialog) -> Unit,
    actionDeleteDialog: (BookInfoEvent.OnActionDeleteDialog) -> Unit,
    actionMoveDialog: (BookInfoEvent.OnActionMoveDialog) -> Unit,
    changeCover: (BookInfoEvent.OnChangeCover) -> Unit,
    resetCover: (BookInfoEvent.OnResetCover) -> Unit,
    deleteCover: (BookInfoEvent.OnDeleteCover) -> Unit,
    checkCoverReset: (BookInfoEvent.OnCheckCoverReset) -> Unit,
    dismissDialog: (BookInfoEvent.OnDismissDialog) -> Unit,
    dismissBottomSheet: (BookInfoEvent.OnDismissBottomSheet) -> Unit,
    navigateToReader: (BookInfoEvent.OnNavigateToReader) -> Unit,
    navigateToLibrarySettings: (BookInfoEvent.OnNavigateToLibrarySettings) -> Unit,
    navigateBack: (BookInfoEvent.OnNavigateBack) -> Unit
) {
    BookInfoDialog(
        dialog = dialog,
        book = book,
        categories = categories,
        actionTitleDialog = actionTitleDialog,
        actionAuthorDialog = actionAuthorDialog,
        actionDescriptionDialog = actionDescriptionDialog,
        actionPathDialog = actionPathDialog,
        actionDeleteDialog = actionDeleteDialog,
        actionMoveDialog = actionMoveDialog,
        dismissDialog = dismissDialog,
        navigateToLibrarySettings = navigateToLibrarySettings
    )

    BookInfoBottomSheet(
        bottomSheet = bottomSheet,
        book = book,
        file = file,
        showPathDialog = showPathDialog,
        canResetCover = canResetCover,
        changeCover = changeCover,
        resetCover = resetCover,
        deleteCover = deleteCover,
        checkCoverReset = checkCoverReset,
        dismissBottomSheet = dismissBottomSheet
    )

    BookInfoScaffold(
        book = book,
        listState = listState,
        showTitleDialog = showTitleDialog,
        showAuthorDialog = showAuthorDialog,
        showDescriptionDialog = showDescriptionDialog,
        showChangeCoverBottomSheet = showChangeCoverBottomSheet,
        showDetailsBottomSheet = showDetailsBottomSheet,
        showMoveDialog = showMoveDialog,
        showDeleteDialog = showDeleteDialog,
        navigateToReader = navigateToReader,
        navigateBack = navigateBack
    )

    BookInfoBackHandler(
        navigateBack = navigateBack
    )
}