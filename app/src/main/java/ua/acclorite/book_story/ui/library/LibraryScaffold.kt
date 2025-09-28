/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.library

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import ua.acclorite.book_story.domain.model.library.Category
import ua.acclorite.book_story.presentation.library.LibraryEvent
import ua.acclorite.book_story.presentation.library.model.LibraryLayout
import ua.acclorite.book_story.presentation.library.model.LibrarySortOrder
import ua.acclorite.book_story.presentation.library.model.LibraryTitlePosition
import ua.acclorite.book_story.presentation.library.model.SelectableBook

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LibraryScaffold(
    books: List<SelectableBook>,
    selectedItemsCount: Int,
    hasSelectedItems: Boolean,
    titlePosition: LibraryTitlePosition,
    readButton: Boolean,
    showProgress: Boolean,
    showCategoryTabs: Boolean,
    showBookCount: Boolean,
    showSearch: Boolean,
    searchQuery: String,
    bookCount: Int,
    focusRequester: FocusRequester,
    pagerState: PagerState,
    isLoading: Boolean,
    isRefreshing: Boolean,
    refreshState: PullRefreshState,
    layout: LibraryLayout,
    gridSize: Int,
    autoGridSize: Boolean,
    categories: List<Category>,
    showDefaultCategory: Boolean,
    perCategorySort: Boolean,
    sortOrder: LibrarySortOrder,
    sortOrderDescending: Boolean,
    searchVisibility: (LibraryEvent.OnSearchVisibility) -> Unit,
    selectBook: (LibraryEvent.OnSelectBook) -> Unit,
    requestFocus: (LibraryEvent.OnRequestFocus) -> Unit,
    searchQueryChange: (LibraryEvent.OnSearchQueryChange) -> Unit,
    search: (LibraryEvent.OnSearch) -> Unit,
    selectBooks: (LibraryEvent.OnSelectBooks) -> Unit,
    clearSelectedBooks: (LibraryEvent.OnClearSelectedBooks) -> Unit,
    showMoveDialog: (LibraryEvent.OnShowMoveDialog) -> Unit,
    showDeleteDialog: (LibraryEvent.OnShowDeleteDialog) -> Unit,
    showFilterBottomSheet: (LibraryEvent.OnShowFilterBottomSheet) -> Unit,
    navigateToBrowse: (LibraryEvent.OnNavigateToBrowse) -> Unit,
    navigateToBookInfo: (LibraryEvent.OnNavigateToBookInfo) -> Unit,
    navigateToReader: (LibraryEvent.OnNavigateToReader) -> Unit,
) {
    Scaffold(
        Modifier
            .fillMaxSize()
            .pullRefresh(refreshState),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            LibraryTopBar(
                books = books,
                selectedItemsCount = selectedItemsCount,
                hasSelectedItems = hasSelectedItems,
                showBookCount = showBookCount,
                showCategoryTabs = showCategoryTabs,
                showSearch = showSearch,
                searchQuery = searchQuery,
                bookCount = bookCount,
                focusRequester = focusRequester,
                pagerState = pagerState,
                isLoading = isLoading,
                isRefreshing = isRefreshing,
                categories = categories,
                showDefaultCategory = showDefaultCategory,
                searchVisibility = searchVisibility,
                requestFocus = requestFocus,
                searchQueryChange = searchQueryChange,
                search = search,
                selectBooks = selectBooks,
                clearSelectedBooks = clearSelectedBooks,
                showMoveDialog = showMoveDialog,
                showDeleteDialog = showDeleteDialog,
                showFilterBottomSheet = showFilterBottomSheet
            )
        }
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            LibraryPager(
                books = books,
                pagerState = pagerState,
                categories = categories,
                showDefaultCategory = showDefaultCategory,
                perCategorySort = perCategorySort,
                sortOrder = sortOrder,
                sortOrderDescending = sortOrderDescending,
                layout = layout,
                gridSize = gridSize,
                autoGridSize = autoGridSize,
                hasSelectedItems = hasSelectedItems,
                titlePosition = titlePosition,
                readButton = readButton,
                showProgress = showProgress,
                isLoading = isLoading,
                isRefreshing = isRefreshing,
                selectBook = selectBook,
                navigateToBrowse = navigateToBrowse,
                navigateToReader = navigateToReader,
                navigateToBookInfo = navigateToBookInfo
            )

            LibraryRefreshIndicator(
                isRefreshing = isRefreshing,
                refreshState = refreshState
            )
        }
    }
}