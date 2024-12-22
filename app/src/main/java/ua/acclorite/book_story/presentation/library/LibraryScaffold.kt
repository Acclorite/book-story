package ua.acclorite.book_story.presentation.library

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
import ua.acclorite.book_story.domain.library.category.CategoryWithBooks
import ua.acclorite.book_story.ui.library.LibraryEvent

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LibraryScaffold(
    selectedItemsCount: Int,
    hasSelectedItems: Boolean,
    showSearch: Boolean,
    searchQuery: String,
    bookCount: Int,
    focusRequester: FocusRequester,
    pagerState: PagerState,
    isLoading: Boolean,
    isRefreshing: Boolean,
    refreshState: PullRefreshState,
    categories: List<CategoryWithBooks>,
    searchVisibility: (LibraryEvent.OnSearchVisibility) -> Unit,
    selectBook: (LibraryEvent.OnSelectBook) -> Unit,
    requestFocus: (LibraryEvent.OnRequestFocus) -> Unit,
    searchQueryChange: (LibraryEvent.OnSearchQueryChange) -> Unit,
    search: (LibraryEvent.OnSearch) -> Unit,
    clearSelectedBooks: (LibraryEvent.OnClearSelectedBooks) -> Unit,
    showMoveDialog: (LibraryEvent.OnShowMoveDialog) -> Unit,
    showDeleteDialog: (LibraryEvent.OnShowDeleteDialog) -> Unit,
    navigateToBrowse: () -> Unit,
    navigateToBookInfo: (id: Int) -> Unit,
    navigateToReader: (id: Int) -> Unit,
) {
    Scaffold(
        Modifier
            .fillMaxSize()
            .pullRefresh(refreshState),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            LibraryTopBar(
                selectedItemsCount = selectedItemsCount,
                hasSelectedItems = hasSelectedItems,
                showSearch = showSearch,
                searchQuery = searchQuery,
                bookCount = bookCount,
                focusRequester = focusRequester,
                pagerState = pagerState,
                isLoading = isLoading,
                isRefreshing = isRefreshing,
                categories = categories,
                searchVisibility = searchVisibility,
                requestFocus = requestFocus,
                searchQueryChange = searchQueryChange,
                search = search,
                clearSelectedBooks = clearSelectedBooks,
                showMoveDialog = showMoveDialog,
                showDeleteDialog = showDeleteDialog
            )
        }
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            LibraryPager(
                pagerState = pagerState,
                categories = categories,
                hasSelectedItems = hasSelectedItems,
                isLoading = isLoading,
                isRefreshing = isRefreshing,
                selectBook = selectBook,
                navigateToBrowse = navigateToBrowse,
                navigateToReader = navigateToReader,
                navigateToBookInfo = navigateToBookInfo,
            )

            LibraryRefreshIndicator(
                isRefreshing = isRefreshing,
                refreshState = refreshState
            )
        }
    }
}