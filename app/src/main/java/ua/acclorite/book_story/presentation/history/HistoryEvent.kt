/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.history

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.model.history.History

@Immutable
sealed class HistoryEvent {
    data class OnRefreshList(
        val loading: Boolean,
        val hideSearch: Boolean
    ) : HistoryEvent()

    data class OnSearchVisibility(
        val show: Boolean
    ) : HistoryEvent()

    data object OnRequestFocus : HistoryEvent()

    data class OnSearchQueryChange(
        val query: String
    ) : HistoryEvent()

    data object OnSearch : HistoryEvent()

    data class OnDeleteHistoryEntry(
        val history: History
    ) : HistoryEvent()

    data class OnRestoreHistoryEntry(
        val history: History
    ) : HistoryEvent()

    data object OnShowDeleteWholeHistoryDialog : HistoryEvent()

    data object OnActionDeleteWholeHistoryDialog : HistoryEvent()

    data object OnDismissDialog : HistoryEvent()

    data object OnNavigateToLibrary : HistoryEvent()

    data class OnNavigateToBookInfo(
        val bookId: Int
    ) : HistoryEvent()

    data class OnNavigateToReader(
        val bookId: Int
    ) : HistoryEvent()
}