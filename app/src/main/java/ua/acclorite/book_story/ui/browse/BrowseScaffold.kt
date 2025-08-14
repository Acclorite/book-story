/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.browse

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import ua.acclorite.book_story.presentation.browse.BrowseEvent
import ua.acclorite.book_story.presentation.browse.model.BrowseLayout
import ua.acclorite.book_story.presentation.browse.model.SelectableFile
import ua.acclorite.book_story.ui.theme.DefaultTransition

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BrowseScaffold(
    files: List<SelectableFile>,
    refreshState: PullRefreshState,
    listState: LazyListState,
    gridState: LazyGridState,
    layout: BrowseLayout,
    gridSize: Int,
    autoGridSize: Boolean,
    includedFilterItems: List<String>,
    pinnedPaths: List<String>,
    canScrollBackList: Boolean,
    canScrollBackGrid: Boolean,
    hasSelectedItems: Boolean,
    selectedItemsCount: Int,
    isRefreshing: Boolean,
    isLoading: Boolean,
    dialogHidden: Boolean,
    filesEmpty: Boolean,
    showSearch: Boolean,
    searchQuery: String,
    focusRequester: FocusRequester,
    searchVisibility: (BrowseEvent.OnSearchVisibility) -> Unit,
    searchQueryChange: (BrowseEvent.OnSearchQueryChange) -> Unit,
    search: (BrowseEvent.OnSearch) -> Unit,
    requestFocus: (BrowseEvent.OnRequestFocus) -> Unit,
    clearSelectedFiles: (BrowseEvent.OnClearSelectedFiles) -> Unit,
    selectFiles: (BrowseEvent.OnSelectFiles) -> Unit,
    showFilterBottomSheet: (BrowseEvent.OnShowFilterBottomSheet) -> Unit,
    showAddDialog: (BrowseEvent.OnShowAddDialog) -> Unit,
    updatePinnedPaths: (BrowseEvent.OnUpdatePinnedPaths) -> Unit,
    navigateToBrowseSettings: (BrowseEvent.OnNavigateToBrowseSettings) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(refreshState),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            BrowseTopBar(
                files = files,
                layout = layout,
                includedFilterItems = includedFilterItems,
                canScrollBackList = canScrollBackList,
                canScrollBackGrid = canScrollBackGrid,
                hasSelectedItems = hasSelectedItems,
                selectedItemsCount = selectedItemsCount,
                showSearch = showSearch,
                searchQuery = searchQuery,
                focusRequester = focusRequester,
                searchVisibility = searchVisibility,
                searchQueryChange = searchQueryChange,
                search = search,
                requestFocus = requestFocus,
                clearSelectedFiles = clearSelectedFiles,
                selectFiles = selectFiles,
                showFilterBottomSheet = showFilterBottomSheet,
                showAddDialog = showAddDialog
            )
        }
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding())
        ) {
            DefaultTransition(visible = !isLoading) {
                BrowseLayout(
                    files = files,
                    pinnedPaths = pinnedPaths,
                    layout = layout,
                    gridSize = gridSize,
                    autoGridSize = autoGridSize,
                    listState = listState,
                    gridState = gridState,
                    headerContent = { header, pinned ->
                        BrowseLayoutHeader(
                            header = header,
                            pinned = pinned,
                            pin = {
                                updatePinnedPaths(
                                    BrowseEvent.OnUpdatePinnedPaths(
                                        path = header
                                    )
                                )
                            }
                        )
                    },
                    itemContent = { file, groupFiles ->
                        BrowseItem(
                            file = file,
                            layout = layout,
                            hasSelectedItems = hasSelectedItems,
                            onLongClick = {
                                selectFiles(
                                    BrowseEvent.OnSelectFiles(
                                        files = groupFiles
                                    )
                                )
                            },
                            onClick = {
                                selectFiles(
                                    BrowseEvent.OnSelectFiles(
                                        files = listOf(file)
                                    )
                                )
                            }
                        )
                    }
                )
            }

            BrowseEmptyPlaceholder(
                filesEmpty = filesEmpty,
                dialogHidden = dialogHidden,
                isLoading = isLoading,
                isRefreshing = isRefreshing,
                navigateToBrowseSettings = navigateToBrowseSettings
            )

            BrowseRefreshIndicator(
                isRefreshing = isRefreshing,
                refreshState = refreshState
            )
        }
    }
}