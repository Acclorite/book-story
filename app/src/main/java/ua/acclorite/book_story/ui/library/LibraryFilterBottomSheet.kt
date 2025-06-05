/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.library

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ua.acclorite.book_story.domain.model.library.Category
import ua.acclorite.book_story.domain.model.library.CategorySort
import ua.acclorite.book_story.presentation.library.LibraryEvent
import ua.acclorite.book_story.presentation.library.model.LibrarySortOrder
import ua.acclorite.book_story.presentation.main.MainEvent
import ua.acclorite.book_story.presentation.settings.SettingsEvent
import ua.acclorite.book_story.ui.common.components.common.LazyColumnWithScrollbar
import ua.acclorite.book_story.ui.common.components.modal_bottom_sheet.ModalBottomSheet
import ua.acclorite.book_story.ui.settings.library.display.LibraryDisplaySubcategory
import ua.acclorite.book_story.ui.settings.library.sort.components.LibrarySortOption
import ua.acclorite.book_story.ui.settings.library.tabs.LibraryTabsSubcategory

private var initialPage = 0

@Composable
fun LibraryFilterBottomSheet(
    categories: List<Category>,
    showDefaultCategory: Boolean,
    categoriesPagerState: PagerState,
    sortOrder: LibrarySortOrder,
    sortOrderDescending: Boolean,
    categoriesSort: List<CategorySort>,
    perCategorySort: Boolean,
    changeLibrarySortOrder: (MainEvent.OnChangeLibrarySortOrder) -> Unit,
    changeLibrarySortOrderDescending: (MainEvent.OnChangeLibrarySortOrderDescending) -> Unit,
    updateCategorySort: (SettingsEvent.OnUpdateCategorySort) -> Unit,
    dismissBottomSheet: (LibraryEvent.OnDismissBottomSheet) -> Unit
) {
    val currentCategory = remember(
        categories,
        showDefaultCategory,
        categoriesPagerState.currentPage
    ) {
        derivedStateOf {
            categories.toMutableList().apply {
                if (showDefaultCategory) {
                    add(0, Category(id = -1, title = ""))
                }
            }[categoriesPagerState.currentPage]
        }
    }

    val currentCategorySort = remember(currentCategory, categoriesSort) {
        derivedStateOf {
            categoriesSort.firstOrNull { it.categoryId == currentCategory.value.id }
                ?: CategorySort(
                    categoryId = currentCategory.value.id,
                    sortOrder = LibrarySortOrder.LAST_READ,
                    sortOrderDescending = true
                )
        }
    }

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage) { 2 }
    DisposableEffect(Unit) { onDispose { initialPage = pagerState.currentPage } }

    ModalBottomSheet(
        hasFixedHeight = true,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
        dragHandle = {},
        onDismissRequest = {
            dismissBottomSheet(LibraryEvent.OnDismissBottomSheet)
        },
        sheetGesturesEnabled = false
    ) {
        LibraryFilterBottomSheetTabRow(
            currentPage = pagerState.currentPage,
            scrollToPage = {
                scope.launch {
                    pagerState.animateScrollToPage(it)
                }
            }
        )

        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            when (page) {
                0 -> {
                    LazyColumnWithScrollbar(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(top = 18.dp)
                    ) {
                        LibrarySortOption(
                            sortOrder = if (perCategorySort) {
                                currentCategorySort.value.sortOrder
                            } else sortOrder,
                            sortOrderDescending = if (perCategorySort) {
                                currentCategorySort.value.sortOrderDescending
                            } else sortOrderDescending,
                            onChange = { sortOrder, sortOrderDescending ->
                                if (perCategorySort) {
                                    updateCategorySort(
                                        SettingsEvent.OnUpdateCategorySort(
                                            categoryId = currentCategorySort.value.categoryId,
                                            sortOrder = sortOrder,
                                            sortOrderDescending = sortOrderDescending
                                        )
                                    )
                                } else {
                                    changeLibrarySortOrder(
                                        MainEvent.OnChangeLibrarySortOrder(
                                            sortOrder.name
                                        )
                                    )
                                    changeLibrarySortOrderDescending(
                                        MainEvent.OnChangeLibrarySortOrderDescending(
                                            sortOrderDescending
                                        )
                                    )
                                }
                            }
                        )
                    }
                }

                1 -> {
                    LazyColumnWithScrollbar(modifier = Modifier.fillMaxSize()) {
                        LibraryDisplaySubcategory(
                            showTitle = false
                        )
                        LibraryTabsSubcategory(
                            titleColor = { MaterialTheme.colorScheme.onSurface },
                            showDivider = false
                        )
                    }
                }
            }
        }
    }
}