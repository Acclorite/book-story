/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.library

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import ua.acclorite.book_story.domain.library.category.Category
import ua.acclorite.book_story.domain.library.category.CategorySort
import ua.acclorite.book_story.domain.library.display.LibrarySortOrder
import ua.acclorite.book_story.domain.util.BottomSheet
import ua.acclorite.book_story.ui.library.LibraryEvent
import ua.acclorite.book_story.ui.library.LibraryScreen
import ua.acclorite.book_story.ui.main.MainEvent
import ua.acclorite.book_story.ui.settings.SettingsEvent

@Composable
fun LibraryBottomSheet(
    bottomSheet: BottomSheet?,
    categories: List<Category>,
    showDefaultCategory: Boolean,
    pagerState: PagerState,
    categoriesSort: List<CategorySort>,
    sortOrder: LibrarySortOrder,
    sortOrderDescending: Boolean,
    perCategorySort: Boolean,
    changeLibrarySortOrder: (MainEvent.OnChangeLibrarySortOrder) -> Unit,
    changeLibrarySortOrderDescending: (MainEvent.OnChangeLibrarySortOrderDescending) -> Unit,
    updateCategorySort: (SettingsEvent.OnUpdateCategorySort) -> Unit,
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
                categoriesSort = categoriesSort,
                perCategorySort = perCategorySort,
                changeLibrarySortOrder = changeLibrarySortOrder,
                changeLibrarySortOrderDescending = changeLibrarySortOrderDescending,
                updateCategorySort = updateCategorySort,
                dismissBottomSheet = dismissBottomSheet
            )
        }
    }
}