package ua.acclorite.book_story.presentation.history

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusRequester
import ua.acclorite.book_story.domain.history.GroupedHistory
import ua.acclorite.book_story.domain.util.Dialog
import ua.acclorite.book_story.ui.history.HistoryEvent

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HistoryContent(
    refreshState: PullRefreshState,
    snackbarState: SnackbarHostState,
    history: List<GroupedHistory>,
    dialog: Dialog?,
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
    search: (HistoryEvent.OnSearch) -> Unit,
    deleteHistoryEntry: (HistoryEvent.OnDeleteHistoryEntry) -> Unit,
    showDeleteWholeHistoryDialog: (HistoryEvent.OnShowDeleteWholeHistoryDialog) -> Unit,
    actionDeleteWholeHistoryDialog: (HistoryEvent.OnActionDeleteWholeHistoryDialog) -> Unit,
    dismissDialog: (HistoryEvent.OnDismissDialog) -> Unit,
    navigateToLibrary: () -> Unit,
    navigateToBookInfo: (Int) -> Unit,
    navigateToReader: (Int) -> Unit
) {
    HistoryDialog(
        dialog = dialog,
        actionDeleteWholeHistoryDialog = actionDeleteWholeHistoryDialog,
        dismissDialog = dismissDialog
    )

    HistoryScaffold(
        refreshState = refreshState,
        snackbarState = snackbarState,
        history = history,
        listState = listState,
        canScrollBackward = canScrollBackward,
        showSearch = showSearch,
        isLoading = isLoading,
        isRefreshing = isRefreshing,
        historyIsEmpty = historyIsEmpty,
        focusRequester = focusRequester,
        searchQuery = searchQuery,
        searchVisibility = searchVisibility,
        requestFocus = requestFocus,
        searchQueryChange = searchQueryChange,
        search = search,
        deleteHistoryEntry = deleteHistoryEntry,
        showDeleteWholeHistoryDialog = showDeleteWholeHistoryDialog,
        navigateToBookInfo = navigateToBookInfo,
        navigateToReader = navigateToReader
    )

    HistoryBackHandler(
        showSearch = showSearch,
        searchVisibility = searchVisibility,
        navigateToLibrary = navigateToLibrary
    )
}