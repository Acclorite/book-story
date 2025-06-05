/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.book_info

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ua.acclorite.book_story.domain.model.library.Book
import ua.acclorite.book_story.presentation.book_info.BookInfoEvent
import ua.acclorite.book_story.ui.common.components.common.StyledText
import ua.acclorite.book_story.ui.common.helpers.noRippleClickable

@Composable
fun BookInfoLayoutInfoTitle(
    book: Book,
    showTitleDialog: (BookInfoEvent.OnShowTitleDialog) -> Unit
) {
    StyledText(
        text = book.title,
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable(
                onClick = {},
                onLongClick = {
                    showTitleDialog(BookInfoEvent.OnShowTitleDialog)
                }
            ),
        style = MaterialTheme.typography.headlineSmall.copy(
            color = MaterialTheme.colorScheme.onSurface
        ),
        maxLines = 4
    )
}