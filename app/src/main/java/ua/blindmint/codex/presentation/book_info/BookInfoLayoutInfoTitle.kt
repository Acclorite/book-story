/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.book_info

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ua.blindmint.codex.domain.library.book.Book
import ua.blindmint.codex.presentation.core.components.common.StyledText
import ua.blindmint.codex.presentation.core.util.noRippleClickable
import ua.blindmint.codex.ui.book_info.BookInfoEvent

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