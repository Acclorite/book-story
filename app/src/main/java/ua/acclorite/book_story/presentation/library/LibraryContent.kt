/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.library

import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusRequester
import ua.acclorite.book_story.domain.library.book.SelectableBook
import ua.acclorite.book_story.domain.library.category.Category
import ua.acclorite.book_story.domain.library.category.CategorySort
import ua.acclorite.book_story.domain.library.display.LibraryLayout
import ua.acclorite.book_story.domain.library.display.LibrarySortOrder
import ua.acclorite.book_story.domain.library.display.LibraryTitlePosition
import ua.acclorite.book_story.domain.util.BottomSheet
import ua.acclorite.book_story.domain.util.Dialog
import ua.acclorite.book_story.ui.library.LibraryEvent
import ua.acclorite.book_story.ui.main.MainEvent
import ua.acclorite.book_story.ui.settings.SettingsEvent

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
    categoriesSort: List<CategorySort>,
    sortOrder: LibrarySortOrder,
    sortOrderDescending: Boolean,
    layout: LibraryLayout,
    gridSize: Int,
    autoGridSize: Boolean,
    refreshState: PullRefreshState,
    dialog: Dialog?,
    bottomSheet: BottomSheet?,
    updateCategorySort: (SettingsEvent.OnUpdateCategorySort) -> Unit,
    changeLibrarySortOrder: (MainEvent.OnChangeLibrarySortOrder) -> Unit,
    changeLibrarySortOrderDescending: (MainEvent.OnChangeLibrarySortOrderDescending) -> Unit,
    selectBook: (LibraryEvent.OnSelectBook) -> Unit,
    searchVisibility: (LibraryEvent.OnSearchVisibility) -> Unit,
    requestFocus: (LibraryEvent.OnRequestFocus) -> Unit,
    searchQueryChange: (LibraryEvent.OnSearchQueryChange) -> Unit,
    search: (LibraryEvent.OnSearch) -> Unit,
    clearSelectedBooks: (LibraryEvent.OnClearSelectedBooks) -> Unit,
    showMoveDialog: (LibraryEvent.OnShowMoveDialog) -> Unit,
    showDeleteDialog: (LibraryEvent.OnShowDeleteDialog) -> Unit,
    actionMoveDialog: (LibraryEvent.OnActionMoveDialog) -> Unit,
    actionDeleteDialog: (LibraryEvent.OnActionDeleteDialog) -> Unit,
    dismissDialog: (LibraryEvent.OnDismissDialog) -> Unit,
    showFilterBottomSheet: (LibraryEvent.OnShowFilterBottomSheet) -> Unit,
    dismissBottomSheet: (LibraryEvent.OnDismissBottomSheet) -> Unit,
    navigateToBrowse: () -> Unit,
    navigateToBookInfo: (id: Int) -> Unit,
    navigateToReader: (id: Int) -> Unit,
    navigateToLibrarySettings: () -> Unit
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
        categoriesSort = categoriesSort,
        sortOrder = sortOrder,
        sortOrderDescending = sortOrderDescending,
        perCategorySort = perCategorySort,
        changeLibrarySortOrder = changeLibrarySortOrder,
        changeLibrarySortOrderDescending = changeLibrarySortOrderDescending,
        updateCategorySort = updateCategorySort,
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
        categoriesSort = categoriesSort,
        sortOrder = sortOrder,
        sortOrderDescending = sortOrderDescending,
        searchVisibility = searchVisibility,
        requestFocus = requestFocus,
        searchQueryChange = searchQueryChange,
        search = search,
        selectBook = selectBook,
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
        searchVisibility = searchVisibility,
    )
}