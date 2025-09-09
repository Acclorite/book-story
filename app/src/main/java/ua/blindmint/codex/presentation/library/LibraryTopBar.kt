/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.library

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.MoveUp
import androidx.compose.material.icons.outlined.RestoreFromTrash
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.blindmint.codex.R
import ua.blindmint.codex.domain.library.category.CategoryWithBooks
import ua.blindmint.codex.presentation.core.components.common.IconButton
import ua.blindmint.codex.presentation.core.components.common.SearchTextField
import ua.blindmint.codex.presentation.core.components.common.StyledText
import ua.blindmint.codex.presentation.core.components.top_bar.TopAppBar
import ua.blindmint.codex.presentation.core.components.top_bar.TopAppBarData
import ua.blindmint.codex.presentation.navigator.NavigatorIconButton
import ua.blindmint.codex.ui.library.LibraryEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryTopBar(
    selectedItemsCount: Int,
    hasSelectedItems: Boolean,
    showSearch: Boolean,
    searchQuery: String,
    bookCount: Int,
    focusRequester: FocusRequester,
    pagerState: PagerState,
    isLoading: Boolean,
    isRefreshing: Boolean,
    categories: List<CategoryWithBooks>,
    searchVisibility: (LibraryEvent.OnSearchVisibility) -> Unit,
    requestFocus: (LibraryEvent.OnRequestFocus) -> Unit,
    searchQueryChange: (LibraryEvent.OnSearchQueryChange) -> Unit,
    search: (LibraryEvent.OnSearch) -> Unit,
    clearSelectedBooks: (LibraryEvent.OnClearSelectedBooks) -> Unit,
    showMoveDialog: (LibraryEvent.OnShowMoveDialog) -> Unit,
    showDeleteDialog: (LibraryEvent.OnShowDeleteDialog) -> Unit,
    showClearProgressHistoryDialog: (LibraryEvent.OnShowClearProgressHistoryDialog) -> Unit
) {
    val animatedItemCountBackgroundColor = animateColorAsState(
        if (hasSelectedItems) MaterialTheme.colorScheme.surfaceContainerHighest
        else MaterialTheme.colorScheme.surfaceContainer,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
    )

    TopAppBar(
        scrollBehavior = null,
        isTopBarScrolled = hasSelectedItems,

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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        StyledText(text = stringResource(id = R.string.library_screen))
                        Spacer(modifier = Modifier.width(6.dp))
                        StyledText(
                            text = bookCount.toString(),
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.surfaceContainer,
                                    RoundedCornerShape(14.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 3.dp),
                            style = LocalTextStyle.current.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 16.sp
                            )
                        )
                    }
                },
                contentActions = {
                    IconButton(
                        icon = Icons.Default.Search,
                        contentDescription = R.string.search_content_desc,
                        disableOnClick = true,
                    ) {
                        searchVisibility(LibraryEvent.OnSearchVisibility(true))
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
                        searchVisibility(LibraryEvent.OnSearchVisibility(false))
                    }
                },
                contentTitle = {
                    SearchTextField(
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .onGloballyPositioned {
                                requestFocus(LibraryEvent.OnRequestFocus(focusRequester))
                            },
                        initialQuery = searchQuery,
                        onQueryChange = {
                            searchQueryChange(LibraryEvent.OnSearchQueryChange(it))
                        },
                        onSearch = {
                            search(LibraryEvent.OnSearch)
                        }
                    )
                },
                contentActions = {
                    NavigatorIconButton()
                },
            ),

            TopAppBarData(
                contentID = 2,
                contentNavigationIcon = {
                    IconButton(
                        icon = Icons.Default.Clear,
                        contentDescription = R.string.clear_selected_items_content_desc,
                        disableOnClick = true
                    ) {
                        clearSelectedBooks(LibraryEvent.OnClearSelectedBooks)
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
                        icon = Icons.Outlined.MoveUp,
                        contentDescription = R.string.move_books_content_desc,
                        enabled = !isLoading && !isRefreshing,
                        disableOnClick = false,
                    ) {
                        showMoveDialog(LibraryEvent.OnShowMoveDialog)
                    }
                    IconButton(
                        icon = Icons.Outlined.Delete,
                        contentDescription = R.string.delete_books_content_desc,
                        enabled = !isLoading && !isRefreshing,
                        disableOnClick = false
                    ) {
                        showDeleteDialog(LibraryEvent.OnShowDeleteDialog)
                    }
                    IconButton(
                        icon = Icons.Outlined.RestoreFromTrash,
                        contentDescription = R.string.clear_progress_history_content_desc,
                        enabled = !isLoading && !isRefreshing,
                        disableOnClick = false
                    ) {
                        showClearProgressHistoryDialog(LibraryEvent.OnShowClearProgressHistoryDialog)
                    }
                }
            ),
        ),
        customContent = {
            LibraryTabs(
                categories = categories,
                pagerState = pagerState,
                itemCountBackgroundColor = animatedItemCountBackgroundColor.value
            )
        }
    )
}