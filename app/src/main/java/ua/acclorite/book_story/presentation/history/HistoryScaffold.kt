package ua.acclorite.book_story.presentation.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import ua.acclorite.book_story.domain.history.GroupedHistory
import ua.acclorite.book_story.presentation.core.components.common.Snackbar
import ua.acclorite.book_story.ui.history.HistoryEvent

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HistoryScaffold(
    refreshState: PullRefreshState,
    snackbarState: SnackbarHostState,
    history: List<GroupedHistory>,
    listState: LazyListState,
    canScrollBackward: Boolean,
    showSearch: Boolean,
    isLoading: Boolean,
    isRefreshing: Boolean,
    historyIsEmpty: Boolean,
    focusRequester: FocusRequester,
    searchQuery: String,
    searchVisibility: (HistoryEvent.OnSearchVisibility) -> Unit,
    requestFocus: (HistoryEvent.OnRequestFocus) -> Unit,
    searchQueryChange: (HistoryEvent.OnSearchQueryChange) -> Unit,
    deleteHistoryEntry: (HistoryEvent.OnDeleteHistoryEntry) -> Unit,
    showDeleteWholeHistoryDialog: (HistoryEvent.OnShowDeleteWholeHistoryDialog) -> Unit,
    search: (HistoryEvent.OnSearch) -> Unit,
    navigateToBookInfo: (Int) -> Unit,
    navigateToReader: (Int) -> Unit
) {
    Scaffold(
        Modifier
            .fillMaxSize()
            .pullRefresh(refreshState),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            HistoryTopBar(
                canScrollBackward = canScrollBackward,
                showSearch = showSearch,
                isLoading = isLoading,
                isRefreshing = isRefreshing,
                historyIsNotEmpty = !historyIsEmpty,
                focusRequester = focusRequester,
                searchQuery = searchQuery,
                searchVisibility = searchVisibility,
                requestFocus = requestFocus,
                searchQueryChange = searchQueryChange,
                showDeleteWholeHistoryDialog = showDeleteWholeHistoryDialog,
                search = search
            )
        },
        bottomBar = {
            Snackbar(snackbarState = snackbarState)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            HistoryLayout(
                listState = listState,
                history = history,
                snackbarState = snackbarState,
                isLoading = isLoading,
                isRefreshing = isRefreshing,
                deleteHistoryEntry = deleteHistoryEntry,
                navigateToBookInfo = navigateToBookInfo,
                navigateToReader = navigateToReader
            )

            HistoryEmptyPlaceholder(
                isLoading = isLoading,
                isRefreshing = isRefreshing,
                isHistoryEmpty = historyIsEmpty
            )

            HistoryRefreshIndicator(
                isRefreshing = isRefreshing,
                refreshState = refreshState
            )
        }
    }
}