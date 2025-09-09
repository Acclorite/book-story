/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.book_info

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import ua.blindmint.codex.R
import ua.blindmint.codex.domain.library.book.Book
import ua.blindmint.codex.domain.ui.UIText
import ua.blindmint.codex.presentation.core.components.dialog.DialogWithTextField
import ua.blindmint.codex.ui.book_info.BookInfoEvent

@Composable
fun BookInfoAuthorDialog(
    book: Book,
    actionAuthorDialog: (BookInfoEvent.OnActionAuthorDialog) -> Unit,
    dismissDialog: (BookInfoEvent.OnDismissDialog) -> Unit
) {
    val context = LocalContext.current
    DialogWithTextField(
        initialValue = book.author.getAsString() ?: "",
        lengthLimit = 100,
        onDismiss = {
            dismissDialog(BookInfoEvent.OnDismissDialog)
        },
        onAction = {
            actionAuthorDialog(
                BookInfoEvent.OnActionAuthorDialog(
                    author = if (it.isBlank()) UIText.StringResource(R.string.unknown_author)
                    else UIText.StringValue(it.trim().replace("\n", "")),
                    context = context
                )
            )
        }
    )
}