/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.book_info

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import ua.acclorite.book_story.domain.library.Book
import ua.acclorite.book_story.presentation.common.components.dialog.DialogWithTextField
import ua.acclorite.book_story.ui.book_info.BookInfoEvent

@Composable
fun BookInfoTitleDialog(
    book: Book,
    actionTitleDialog: (BookInfoEvent.OnActionTitleDialog) -> Unit,
    dismissDialog: (BookInfoEvent.OnDismissDialog) -> Unit
) {
    val context = LocalContext.current
    DialogWithTextField(
        initialValue = book.title,
        lengthLimit = 100,
        onDismiss = {
            dismissDialog(BookInfoEvent.OnDismissDialog)
        },
        onAction = {
            if (it.isBlank()) return@DialogWithTextField
            actionTitleDialog(
                BookInfoEvent.OnActionTitleDialog(
                    title = it.trim().replace("\n", ""),
                    context = context
                )
            )
        }
    )
}