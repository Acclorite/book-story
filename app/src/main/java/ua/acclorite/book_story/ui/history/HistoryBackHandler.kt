/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.history

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import ua.acclorite.book_story.presentation.history.HistoryEvent

@Composable
fun HistoryBackHandler(
    showSearch: Boolean,
    searchVisibility: (HistoryEvent.OnSearchVisibility) -> Unit,
    navigateToLibrary: (HistoryEvent.OnNavigateToLibrary) -> Unit
) {
    BackHandler {
        if (showSearch) {
            searchVisibility(HistoryEvent.OnSearchVisibility(false))
            return@BackHandler
        }

        navigateToLibrary(HistoryEvent.OnNavigateToLibrary)
    }
}