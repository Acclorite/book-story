/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.library

import android.content.Context
import androidx.compose.runtime.Immutable
import androidx.compose.ui.focus.FocusRequester
import ua.acclorite.book_story.domain.library.category.Category

@Immutable
sealed class LibraryEvent {
    data class OnRefreshList(
        val loading: Boolean,
        val hideSearch: Boolean
    ) : LibraryEvent()

    data class OnSearchVisibility(
        val show: Boolean
    ) : LibraryEvent()

    data class OnSearchQueryChange(
        val query: String
    ) : LibraryEvent()

    data object OnSearch : LibraryEvent()

    data class OnRequestFocus(
        val focusRequester: FocusRequester
    ) : LibraryEvent()

    data object OnClearSelectedBooks : LibraryEvent()

    data class OnSelectBook(
        val id: Int,
        val select: Boolean? = null
    ) : LibraryEvent()

    data object OnShowMoveDialog : LibraryEvent()

    data class OnActionMoveDialog(
        val selectedCategories: List<Category>,
        val context: Context
    ) : LibraryEvent()

    data object OnShowDeleteDialog : LibraryEvent()

    data class OnActionDeleteDialog(
        val context: Context
    ) : LibraryEvent()

    data object OnDismissDialog : LibraryEvent()

    data object OnShowFilterBottomSheet : LibraryEvent()

    data object OnDismissBottomSheet : LibraryEvent()
}