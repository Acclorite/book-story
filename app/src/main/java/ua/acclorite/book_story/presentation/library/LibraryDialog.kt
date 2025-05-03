/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.library

import androidx.compose.runtime.Composable
import ua.acclorite.book_story.core.Dialog
import ua.acclorite.book_story.domain.library.Category
import ua.acclorite.book_story.ui.library.LibraryEvent
import ua.acclorite.book_story.ui.library.LibraryScreen
import ua.acclorite.book_story.ui.library.model.SelectableBook

@Composable
fun LibraryDialog(
    dialog: Dialog?,
    books: List<SelectableBook>,
    categories: List<Category>,
    selectedItemsCount: Int,
    actionMoveDialog: (LibraryEvent.OnActionMoveDialog) -> Unit,
    actionDeleteDialog: (LibraryEvent.OnActionDeleteDialog) -> Unit,
    dismissDialog: (LibraryEvent.OnDismissDialog) -> Unit,
    navigateToLibrarySettings: () -> Unit
) {
    when (dialog) {
        LibraryScreen.MOVE_DIALOG -> {
            LibraryMoveDialog(
                books = books,
                categories = categories,
                actionMoveDialog = actionMoveDialog,
                dismissDialog = dismissDialog,
                navigateToLibrarySettings = navigateToLibrarySettings
            )
        }

        LibraryScreen.DELETE_DIALOG -> {
            LibraryDeleteDialog(
                selectedItemsCount = selectedItemsCount,
                actionDeleteDialog = actionDeleteDialog,
                dismissDialog = dismissDialog
            )
        }
    }
}