/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.ui.history

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Immutable
import androidx.compose.ui.focus.FocusRequester
import ua.blindmint.codex.domain.history.History

@Immutable
sealed class HistoryEvent {
    data class OnRefreshList(
        val loading: Boolean,
        val hideSearch: Boolean
    ) : HistoryEvent()

    data class OnSearchVisibility(
        val show: Boolean
    ) : HistoryEvent()

    data class OnRequestFocus(
        val focusRequester: FocusRequester
    ) : HistoryEvent()

    data class OnSearchQueryChange(
        val query: String
    ) : HistoryEvent()

    data object OnSearch : HistoryEvent()

    data class OnDeleteHistoryEntry(
        val history: History,
        val snackbarState: SnackbarHostState,
        val context: Context
    ) : HistoryEvent()

    data object OnShowDeleteWholeHistoryDialog : HistoryEvent()

    data class OnActionDeleteWholeHistoryDialog(
        val context: Context
    ) : HistoryEvent()

    data object OnDismissDialog : HistoryEvent()
}