/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.book_info

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ua.blindmint.codex.R
import ua.blindmint.codex.presentation.core.components.dialog.Dialog
import ua.blindmint.codex.ui.book_info.BookInfoEvent

@Composable
fun BookInfoDeleteDialog(
    actionDeleteDialog: (BookInfoEvent.OnActionDeleteDialog) -> Unit,
    dismissDialog: (BookInfoEvent.OnDismissDialog) -> Unit,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current

    Dialog(
        title = stringResource(id = R.string.delete_book),
        icon = Icons.Outlined.DeleteOutline,
        description = stringResource(
            id = R.string.delete_book_description
        ),
        onDismiss = { dismissDialog(BookInfoEvent.OnDismissDialog) },
        withContent = false,
        actionEnabled = true,
        onAction = {
            actionDeleteDialog(
                BookInfoEvent.OnActionDeleteDialog(
                    context = context,
                    navigateBack = navigateBack
                )
            )
        }
    )
}