package ua.acclorite.book_story.presentation.browse

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusRequester
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import ua.acclorite.book_story.domain.browse.BrowseFilesStructure
import ua.acclorite.book_story.domain.browse.BrowseLayout
import ua.acclorite.book_story.domain.browse.SelectableFile
import ua.acclorite.book_story.domain.library.book.SelectableNullableBook
import ua.acclorite.book_story.domain.util.BottomSheet
import ua.acclorite.book_story.domain.util.Dialog
import ua.acclorite.book_story.ui.browse.BrowseEvent
import java.io.File

@OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class)
@Composable
fun BrowseContent(
    files: List<SelectableFile>,
    selectedBooksAddDialog: List<SelectableNullableBook>,
    refreshState: PullRefreshState,
    storagePermissionState: PermissionState,
    loadingAddDialog: Boolean,
    dialog: Dialog?,
    bottomSheet: BottomSheet?,
    listState: LazyListState,
    gridState: LazyGridState,
    layout: BrowseLayout,
    filesStructure: BrowseFilesStructure,
    gridSize: Int,
    autoGridSize: Boolean,
    includedFilterItems: List<String>,
    canScrollBackList: Boolean,
    canScrollBackGrid: Boolean,
    selectedDirectory: File,
    inNestedDirectory: Boolean,
    hasSelectedItems: Boolean,
    selectedItemsCount: Int,
    isRefreshing: Boolean,
    isLoading: Boolean,
    isError: Boolean,
    dialogHidden: Boolean,
    filesEmpty: Boolean,
    showSearch: Boolean,
    searchQuery: String,
    hasSearched: Boolean,
    focusRequester: FocusRequester,
    searchVisibility: (BrowseEvent.OnSearchVisibility) -> Unit,
    searchQueryChange: (BrowseEvent.OnSearchQueryChange) -> Unit,
    search: (BrowseEvent.OnSearch) -> Unit,
    requestFocus: (BrowseEvent.OnRequestFocus) -> Unit,
    clearSelectedFiles: (BrowseEvent.OnClearSelectedFiles) -> Unit,
    goBackDirectory: (BrowseEvent.OnGoBackDirectory) -> Unit,
    selectFiles: (BrowseEvent.OnSelectFiles) -> Unit,
    selectFile: (BrowseEvent.OnSelectFile) -> Unit,
    permissionCheck: (BrowseEvent.OnPermissionCheck) -> Unit,
    updateFavoriteDirectory: (BrowseEvent.OnUpdateFavoriteDirectory) -> Unit,
    changeDirectory: (BrowseEvent.OnChangeDirectory) -> Unit,
    dismissBottomSheet: (BrowseEvent.OnDismissBottomSheet) -> Unit,
    showFilterBottomSheet: (BrowseEvent.OnShowFilterBottomSheet) -> Unit,
    actionPermissionDialog: (BrowseEvent.OnActionPermissionDialog) -> Unit,
    dismissPermissionDialog: (BrowseEvent.OnDismissPermissionDialog) -> Unit,
    showAddDialog: (BrowseEvent.OnShowAddDialog) -> Unit,
    dismissAddDialog: (BrowseEvent.OnDismissAddDialog) -> Unit,
    actionAddDialog: (BrowseEvent.OnActionAddDialog) -> Unit,
    selectAddDialog: (BrowseEvent.OnSelectAddDialog) -> Unit,
    navigateToLibrary: () -> Unit,
    navigateToHelp: () -> Unit,
) {
    BrowseDialog(
        dialog = dialog,
        storagePermissionState = storagePermissionState,
        loadingAddDialog = loadingAddDialog,
        actionPermissionDialog = actionPermissionDialog,
        dismissPermissionDialog = dismissPermissionDialog,
        actionAddDialog = actionAddDialog,
        dismissAddDialog = dismissAddDialog,
        selectedBooksAddDialog = selectedBooksAddDialog,
        selectAddDialog = selectAddDialog,
        navigateToLibrary = navigateToLibrary
    )

    BrowseBottomSheet(
        bottomSheet = bottomSheet,
        dismissBottomSheet = dismissBottomSheet
    )

    BrowseScaffold(
        files = files,
        refreshState = refreshState,
        listState = listState,
        gridState = gridState,
        layout = layout,
        filesStructure = filesStructure,
        gridSize = gridSize,
        autoGridSize = autoGridSize,
        includedFilterItems = includedFilterItems,
        canScrollBackList = canScrollBackList,
        canScrollBackGrid = canScrollBackGrid,
        selectedDirectory = selectedDirectory,
        inNestedDirectory = inNestedDirectory,
        hasSelectedItems = hasSelectedItems,
        selectedItemsCount = selectedItemsCount,
        isRefreshing = isRefreshing,
        dialogHidden = dialogHidden,
        showSearch = showSearch,
        searchQuery = searchQuery,
        hasSearched = hasSearched,
        focusRequester = focusRequester,
        searchVisibility = searchVisibility,
        searchQueryChange = searchQueryChange,
        search = search,
        requestFocus = requestFocus,
        clearSelectedFiles = clearSelectedFiles,
        goBackDirectory = goBackDirectory,
        selectFiles = selectFiles,
        storagePermissionState = storagePermissionState,
        isLoading = isLoading,
        isError = isError,
        filesEmpty = filesEmpty,
        permissionCheck = permissionCheck,
        selectFile = selectFile,
        updateFavoriteDirectory = updateFavoriteDirectory,
        changeDirectory = changeDirectory,
        showFilterBottomSheet = showFilterBottomSheet,
        showAddDialog = showAddDialog,
        navigateToHelp = navigateToHelp
    )

    BrowseBackHandler(
        hasSelectedItems = hasSelectedItems,
        showSearch = showSearch,
        inNestedDirectory = inNestedDirectory,
        searchVisibility = searchVisibility,
        clearSelectedFiles = clearSelectedFiles,
        goBackDirectory = goBackDirectory,
        navigateToLibrary = navigateToLibrary
    )
}