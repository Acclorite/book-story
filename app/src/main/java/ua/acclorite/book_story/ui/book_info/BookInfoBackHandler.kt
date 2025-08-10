/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.book_info

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import ua.acclorite.book_story.presentation.book_info.BookInfoEvent

@Composable
fun BookInfoBackHandler(
    navigateBack: (BookInfoEvent.OnNavigateBack) -> Unit
) {
    BackHandler {
        navigateBack(BookInfoEvent.OnNavigateBack)
    }
}