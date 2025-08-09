/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.history

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.model.history.History

@Immutable
sealed class HistoryEffect {
    data object OnRequestFocus : HistoryEffect()

    data class OnShowSnackbar(
        val history: History
    ) : HistoryEffect()

    data object OnWholeHistoryDeleted : HistoryEffect()

    data object OnNavigateToLibrary : HistoryEffect()

    data class OnNavigateToBookInfo(
        val bookId: Int
    ) : HistoryEffect()

    data class OnNavigateToReader(
        val bookId: Int
    ) : HistoryEffect()
}