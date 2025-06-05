/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.history

import androidx.compose.runtime.Composable
import ua.acclorite.book_story.core.Dialog
import ua.acclorite.book_story.presentation.history.HistoryEvent
import ua.acclorite.book_story.presentation.history.HistoryScreen

@Composable
fun HistoryDialog(
    dialog: Dialog?,
    actionDeleteWholeHistoryDialog: (HistoryEvent.OnActionDeleteWholeHistoryDialog) -> Unit,
    dismissDialog: (HistoryEvent.OnDismissDialog) -> Unit
) {
    when (dialog) {
        HistoryScreen.DELETE_WHOLE_HISTORY_DIALOG -> {
            HistoryDeleteWholeHistoryDialog(
                actionDeleteWholeHistoryDialog = actionDeleteWholeHistoryDialog,
                dismissDialog = dismissDialog
            )
        }
    }
}