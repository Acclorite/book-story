/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.library

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ua.blindmint.codex.R
import ua.blindmint.codex.presentation.core.components.dialog.Dialog
import ua.blindmint.codex.ui.library.LibraryEvent

@Composable
fun LibraryDeleteDialog(
    selectedItemsCount: Int,
    actionDeleteDialog: (LibraryEvent.OnActionDeleteDialog) -> Unit,
    dismissDialog: (LibraryEvent.OnDismissDialog) -> Unit,
    title: String = stringResource(id = R.string.delete_books),
    description: String = stringResource(
        id = R.string.delete_books_description,
        selectedItemsCount
    ),
    confirmText: String = stringResource(id = R.string.delete)
) {
    val context = LocalContext.current

    Dialog(
        title = title,
        icon = Icons.Outlined.DeleteOutline,
        description = description,
        actionEnabled = true,
        onDismiss = {
            dismissDialog(LibraryEvent.OnDismissDialog)
        },
        onAction = {
            actionDeleteDialog(
                LibraryEvent.OnActionDeleteDialog(
                    context = context
                )
            )
        },
        withContent = false
    )
}