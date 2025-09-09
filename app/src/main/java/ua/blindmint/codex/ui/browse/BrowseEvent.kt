/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.ui.browse

import android.content.Context
import androidx.compose.runtime.Immutable
import androidx.compose.ui.focus.FocusRequester
import ua.blindmint.codex.domain.browse.file.SelectableFile
import ua.blindmint.codex.domain.library.book.SelectableNullableBook

@Immutable
sealed class BrowseEvent {
    data class OnRefreshList(
        val loading: Boolean,
        val hideSearch: Boolean
    ) : BrowseEvent()

    data class OnSearchVisibility(
        val show: Boolean
    ) : BrowseEvent()

    data class OnRequestFocus(
        val focusRequester: FocusRequester
    ) : BrowseEvent()

    data class OnSearchQueryChange(
        val query: String
    ) : BrowseEvent()

    data object OnSearch : BrowseEvent()

    data object OnClearSelectedFiles : BrowseEvent()

    data class OnSelectFiles(
        val includedFileFormats: List<String>,
        val files: List<SelectableFile>
    ) : BrowseEvent()

    data class OnSelectFile(
        val includedFileFormats: List<String>,
        val file: SelectableFile
    ) : BrowseEvent()

    data object OnShowFilterBottomSheet : BrowseEvent()

    data object OnDismissBottomSheet : BrowseEvent()

    data object OnShowAddDialog : BrowseEvent()

    data object OnDismissAddDialog : BrowseEvent()

    data class OnSelectAddDialog(
        val book: SelectableNullableBook
    ) : BrowseEvent()

    data class OnActionAddDialog(
        val context: Context,
        val navigateToLibrary: () -> Unit
    ) : BrowseEvent()

    data object OnDismissDialog : BrowseEvent()
}