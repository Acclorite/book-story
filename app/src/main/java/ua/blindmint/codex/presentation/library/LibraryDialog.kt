/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.library

import androidx.compose.runtime.Composable
import ua.blindmint.codex.domain.library.book.SelectableBook
import ua.blindmint.codex.domain.library.category.CategoryWithBooks
import ua.blindmint.codex.domain.util.Dialog
import ua.blindmint.codex.ui.library.LibraryEvent
import ua.blindmint.codex.ui.library.LibraryScreen

@Composable
fun LibraryDialog(
    dialog: Dialog?,
    books: List<SelectableBook>,
    categories: List<CategoryWithBooks>,
    selectedItemsCount: Int,
    actionMoveDialog: (LibraryEvent.OnActionMoveDialog) -> Unit,
    actionDeleteDialog: (LibraryEvent.OnActionDeleteDialog) -> Unit,
    actionClearProgressHistoryDialog: (LibraryEvent.OnActionClearProgressHistoryDialog) -> Unit,
    dismissDialog: (LibraryEvent.OnDismissDialog) -> Unit
) {
    when (dialog) {
        LibraryScreen.MOVE_DIALOG -> {
            LibraryMoveDialog(
                books = books,
                categories = categories,
                selectedItemsCount = selectedItemsCount,
                actionMoveDialog = actionMoveDialog,
                dismissDialog = dismissDialog
            )
        }

        LibraryScreen.DELETE_DIALOG -> {
            LibraryDeleteDialog(
                selectedItemsCount = selectedItemsCount,
                actionDeleteDialog = actionDeleteDialog,
                dismissDialog = dismissDialog
            )
        }

        "clear_progress_history_dialog" -> {
            LibraryDeleteDialog(
                selectedItemsCount = selectedItemsCount,
                actionDeleteDialog = { event ->
                    actionClearProgressHistoryDialog(
                        LibraryEvent.OnActionClearProgressHistoryDialog(event.context)
                    )
                },
                dismissDialog = dismissDialog,
                title = "Clear Progress History?",
                description = "This will remove the progress history for all selected books. This action cannot be undone.",
                confirmText = "Clear History"
            )
        }
    }
}