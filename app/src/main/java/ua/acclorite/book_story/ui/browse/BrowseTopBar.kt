/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.browse

import androidx.compose.animation.animateColorAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.browse.BrowseEvent
import ua.acclorite.book_story.presentation.browse.model.BrowseLayout
import ua.acclorite.book_story.presentation.browse.model.SelectableFile
import ua.acclorite.book_story.ui.common.components.common.IconButton
import ua.acclorite.book_story.ui.common.components.common.SearchTextField
import ua.acclorite.book_story.ui.common.components.common.StyledText
import ua.acclorite.book_story.ui.common.components.top_bar.TopAppBar
import ua.acclorite.book_story.ui.common.components.top_bar.TopAppBarData
import ua.acclorite.book_story.ui.navigator.NavigatorIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseTopBar(
    files: List<SelectableFile>,
    layout: BrowseLayout,
    includedFilterItems: List<String>,
    canScrollBackList: Boolean,
    canScrollBackGrid: Boolean,
    hasSelectedItems: Boolean,
    selectedItemsCount: Int,
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
    showAddDialog: (BrowseEvent.OnShowAddDialog) -> Unit
) {
    val isScrolled = remember(layout, canScrollBackList, canScrollBackGrid) {
        derivedStateOf {
            when (layout) {
                BrowseLayout.GRID -> canScrollBackGrid
                BrowseLayout.LIST -> canScrollBackList
            }
        }
    }

    val animatedFilterIconColor = animateColorAsState(
        if (includedFilterItems.isNotEmpty()) {
            MaterialTheme.colorScheme.primary
        } else LocalContentColor.current
    )

    TopAppBar(
        scrollBehavior = null,
        isTopBarScrolled = isScrolled.value || hasSelectedItems,

        shownTopBar = when {
            hasSelectedItems -> 2
            showSearch -> 1
            else -> 0
        },
        topBars = listOf(
            TopAppBarData(
                contentID = 0,
                contentNavigationIcon = {},
                contentTitle = {
                    StyledText(
                        text = stringResource(id = R.string.browse_screen),
                        maxLines = 1
                    )
                },
                contentActions = {
                    IconButton(
                        icon = Icons.Default.Search,
                        contentDescription = R.string.search_content_desc,
                        disableOnClick = true
                    ) {
                        searchVisibility(BrowseEvent.OnSearchVisibility(true))
                    }
                    IconButton(
                        icon = Icons.Default.FilterList,
                        contentDescription = R.string.filter_content_desc,
                        disableOnClick = false,
                        color = animatedFilterIconColor.value
                    ) {
                        showFilterBottomSheet(BrowseEvent.OnShowFilterBottomSheet)
                    }
                    NavigatorIconButton()
                }
            ),

            TopAppBarData(
                contentID = 1,
                contentNavigationIcon = {
                    IconButton(
                        icon = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = R.string.exit_search_content_desc,
                        disableOnClick = true
                    ) {
                        searchVisibility(BrowseEvent.OnSearchVisibility(false))
                    }
                },
                contentTitle = {
                    SearchTextField(
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .onGloballyPositioned {
                                requestFocus(BrowseEvent.OnRequestFocus)
                            },
                        initialQuery = searchQuery,
                        onQueryChange = {
                            searchQueryChange(BrowseEvent.OnSearchQueryChange(it))
                        },
                        onSearch = {
                            search(BrowseEvent.OnSearch)
                        }
                    )
                },
                contentActions = {
                    NavigatorIconButton()
                }
            ),

            TopAppBarData(
                contentID = 2,
                contentNavigationIcon = {
                    IconButton(
                        icon = Icons.Default.Clear,
                        contentDescription = R.string.clear_selected_items_content_desc,
                        disableOnClick = true
                    ) {
                        clearSelectedFiles(BrowseEvent.OnClearSelectedFiles)
                    }
                },
                contentTitle = {
                    StyledText(
                        text = stringResource(
                            id = R.string.selected_items_count_query,
                            selectedItemsCount.coerceAtLeast(1)
                        ),
                        maxLines = 1
                    )
                },
                contentActions = {
                    IconButton(
                        icon = Icons.Default.SelectAll,
                        contentDescription = R.string.select_all_files_content_desc,
                        disableOnClick = false,
                    ) {
                        selectFiles(
                            BrowseEvent.OnSelectFiles(
                                files = files
                            )
                        )
                    }
                    IconButton(
                        icon = Icons.Default.Check,
                        contentDescription = R.string.add_files_content_desc,
                        disableOnClick = false
                    ) {
                        showAddDialog(BrowseEvent.OnShowAddDialog)
                    }
                }
            )
        )
    )
}