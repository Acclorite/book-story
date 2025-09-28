/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.library

import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusRequester
import ua.acclorite.book_story.core.BottomSheet
import ua.acclorite.book_story.core.Dialog
import ua.acclorite.book_story.domain.model.library.Category
import ua.acclorite.book_story.presentation.library.LibraryEvent
import ua.acclorite.book_story.presentation.library.model.LibraryLayout
import ua.acclorite.book_story.presentation.library.model.LibrarySortOrder
import ua.acclorite.book_story.presentation.library.model.LibraryTitlePosition
import ua.acclorite.book_story.presentation.library.model.SelectableBook
import ua.acclorite.book_story.presentation.settings.SettingsEvent

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LibraryContent(
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
    doublePressExit: Boolean,
    categories: List<Category>,
    showDefaultCategory: Boolean,
    perCategorySort: Boolean,
    sortOrder: LibrarySortOrder,
    sortOrderDescending: Boolean,
    layout: LibraryLayout,
    gridSize: Int,
    autoGridSize: Boolean,
    refreshState: PullRefreshState,
    dialog: Dialog?,
    bottomSheet: BottomSheet?,
    updateCategory: (SettingsEvent.OnUpdateCategory) -> Unit,
    changeSortOrder: (LibrarySortOrder) -> Unit,
    changeSortOrderDescending: (Boolean) -> Unit,
    selectBook: (LibraryEvent.OnSelectBook) -> Unit,
    searchVisibility: (LibraryEvent.OnSearchVisibility) -> Unit,
    requestFocus: (LibraryEvent.OnRequestFocus) -> Unit,
    searchQueryChange: (LibraryEvent.OnSearchQueryChange) -> Unit,
    search: (LibraryEvent.OnSearch) -> Unit,
    selectBooks: (LibraryEvent.OnSelectBooks) -> Unit,
    clearSelectedBooks: (LibraryEvent.OnClearSelectedBooks) -> Unit,
    showMoveDialog: (LibraryEvent.OnShowMoveDialog) -> Unit,
    showDeleteDialog: (LibraryEvent.OnShowDeleteDialog) -> Unit,
    actionMoveDialog: (LibraryEvent.OnActionMoveDialog) -> Unit,
    actionDeleteDialog: (LibraryEvent.OnActionDeleteDialog) -> Unit,
    dismissDialog: (LibraryEvent.OnDismissDialog) -> Unit,
    showFilterBottomSheet: (LibraryEvent.OnShowFilterBottomSheet) -> Unit,
    dismissBottomSheet: (LibraryEvent.OnDismissBottomSheet) -> Unit,
    navigateToBrowse: (LibraryEvent.OnNavigateToBrowse) -> Unit,
    navigateToBookInfo: (LibraryEvent.OnNavigateToBookInfo) -> Unit,
    navigateToReader: (LibraryEvent.OnNavigateToReader) -> Unit,
    navigateToLibrarySettings: (LibraryEvent.OnNavigateToLibrarySettings) -> Unit
) {
    LibraryDialog(
        dialog = dialog,
        books = books,
        categories = categories,
        selectedItemsCount = selectedItemsCount,
        actionMoveDialog = actionMoveDialog,
        actionDeleteDialog = actionDeleteDialog,
        dismissDialog = dismissDialog,
        navigateToLibrarySettings = navigateToLibrarySettings
    )

    LibraryBottomSheet(
        bottomSheet = bottomSheet,
        categories = categories,
        showDefaultCategory = showDefaultCategory,
        pagerState = pagerState,
        sortOrder = sortOrder,
        sortOrderDescending = sortOrderDescending,
        perCategorySort = perCategorySort,
        changeSortOrder = changeSortOrder,
        changeSortOrderDescending = changeSortOrderDescending,
        updateCategory = updateCategory,
        dismissBottomSheet = dismissBottomSheet
    )

    LibraryScaffold(
        books = books,
        selectedItemsCount = selectedItemsCount,
        hasSelectedItems = hasSelectedItems,
        titlePosition = titlePosition,
        readButton = readButton,
        showProgress = showProgress,
        showBookCount = showBookCount,
        showCategoryTabs = showCategoryTabs,
        showSearch = showSearch,
        searchQuery = searchQuery,
        bookCount = bookCount,
        focusRequester = focusRequester,
        pagerState = pagerState,
        isLoading = isLoading,
        isRefreshing = isRefreshing,
        layout = layout,
        gridSize = gridSize,
        autoGridSize = autoGridSize,
        categories = categories,
        showDefaultCategory = showDefaultCategory,
        perCategorySort = perCategorySort,
        sortOrder = sortOrder,
        sortOrderDescending = sortOrderDescending,
        searchVisibility = searchVisibility,
        requestFocus = requestFocus,
        searchQueryChange = searchQueryChange,
        search = search,
        selectBook = selectBook,
        selectBooks = selectBooks,
        clearSelectedBooks = clearSelectedBooks,
        showMoveDialog = showMoveDialog,
        showDeleteDialog = showDeleteDialog,
        showFilterBottomSheet = showFilterBottomSheet,
        refreshState = refreshState,
        navigateToBrowse = navigateToBrowse,
        navigateToBookInfo = navigateToBookInfo,
        navigateToReader = navigateToReader
    )

    LibraryBackHandler(
        hasSelectedItems = hasSelectedItems,
        showSearch = showSearch,
        pagerState = pagerState,
        doublePressExit = doublePressExit,
        clearSelectedBooks = clearSelectedBooks,
        searchVisibility = searchVisibility
    )
}