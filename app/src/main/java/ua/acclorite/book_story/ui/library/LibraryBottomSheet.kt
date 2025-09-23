/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.library

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import ua.acclorite.book_story.core.BottomSheet
import ua.acclorite.book_story.domain.model.library.Category
import ua.acclorite.book_story.presentation.library.LibraryEvent
import ua.acclorite.book_story.presentation.library.LibraryScreen
import ua.acclorite.book_story.presentation.library.model.LibrarySortOrder
import ua.acclorite.book_story.presentation.settings.SettingsEvent

@Composable
fun LibraryBottomSheet(
    bottomSheet: BottomSheet?,
    categories: List<Category>,
    showDefaultCategory: Boolean,
    pagerState: PagerState,
    sortOrder: LibrarySortOrder,
    sortOrderDescending: Boolean,
    perCategorySort: Boolean,
    changeSortOrder: (LibrarySortOrder) -> Unit,
    changeSortOrderDescending: (Boolean) -> Unit,
    updateCategory: (SettingsEvent.OnUpdateCategory) -> Unit,
    dismissBottomSheet: (LibraryEvent.OnDismissBottomSheet) -> Unit
) {
    when (bottomSheet) {
        LibraryScreen.FILTER_BOTTOM_SHEET -> {
            LibraryFilterBottomSheet(
                categories = categories,
                showDefaultCategory = showDefaultCategory,
                categoriesPagerState = pagerState,
                sortOrder = sortOrder,
                sortOrderDescending = sortOrderDescending,
                perCategorySort = perCategorySort,
                changeSortOrder = changeSortOrder,
                changeSortOrderDescending = changeSortOrderDescending,
                updateCategory = updateCategory,
                dismissBottomSheet = dismissBottomSheet
            )
        }
    }
}