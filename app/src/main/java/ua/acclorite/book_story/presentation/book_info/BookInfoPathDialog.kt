/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.book_info

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import ua.acclorite.book_story.domain.library.book.Book
import ua.acclorite.book_story.presentation.core.components.dialog.DialogWithTextField
import ua.acclorite.book_story.ui.book_info.BookInfoEvent

@Composable
fun BookInfoPathDialog(
    book: Book,
    actionPathDialog: (BookInfoEvent.OnActionPathDialog) -> Unit,
    dismissDialog: (BookInfoEvent.OnDismissDialog) -> Unit
) {
    val context = LocalContext.current
    DialogWithTextField(
        initialValue = book.filePath.trim(),
        lengthLimit = 10000,
        onDismiss = {
            dismissDialog(BookInfoEvent.OnDismissDialog)
        },
        onAction = {
            if (it.isBlank()) return@DialogWithTextField
            actionPathDialog(
                BookInfoEvent.OnActionPathDialog(
                    path = it.trim().replace("\n", ""),
                    context = context
                )
            )
        }
    )
}