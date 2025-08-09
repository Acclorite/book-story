/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.history

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.history.HistoryEvent
import ua.acclorite.book_story.ui.common.components.dialog.Dialog

@Composable
fun HistoryDeleteWholeHistoryDialog(
    actionDeleteWholeHistoryDialog: (HistoryEvent.OnActionDeleteWholeHistoryDialog) -> Unit,
    dismissDialog: (HistoryEvent.OnDismissDialog) -> Unit
) {
    Dialog(
        title = stringResource(id = R.string.delete_history),
        icon = Icons.Outlined.DeleteOutline,
        description = stringResource(id = R.string.delete_history_description),
        actionEnabled = true,
        onDismiss = {
            dismissDialog(HistoryEvent.OnDismissDialog)
        },
        onAction = {
            actionDeleteWholeHistoryDialog(HistoryEvent.OnActionDeleteWholeHistoryDialog)
        },
        withContent = false
    )
}