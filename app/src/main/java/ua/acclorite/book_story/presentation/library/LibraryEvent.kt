/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.library

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.model.library.Category
import ua.acclorite.book_story.presentation.library.model.SelectableBook

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

    data object OnRequestFocus : LibraryEvent()

    data object OnClearSelectedBooks : LibraryEvent()

    data class OnSelectBook(
        val id: Int,
        val select: Boolean? = null
    ) : LibraryEvent()

    data class OnSelectBooks(
        val books: List<SelectableBook>
    ) : LibraryEvent()

    data object OnShowMoveDialog : LibraryEvent()

    data class OnActionMoveDialog(
        val selectedCategories: List<Category>
    ) : LibraryEvent()

    data object OnShowDeleteDialog : LibraryEvent()

    data object OnActionDeleteDialog : LibraryEvent()

    data object OnDismissDialog : LibraryEvent()

    data object OnShowFilterBottomSheet : LibraryEvent()

    data object OnDismissBottomSheet : LibraryEvent()

    data object OnNavigateToLibrarySettings : LibraryEvent()

    data object OnNavigateToBrowse : LibraryEvent()

    data class OnNavigateToBookInfo(
        val id: Int
    ) : LibraryEvent()

    data class OnNavigateToReader(
        val id: Int
    ) : LibraryEvent()
}