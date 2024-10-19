package ua.acclorite.book_story.presentation.screens.library.components

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
import androidx.compose.material.icons.automirrored.outlined.DriveFileMove
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.common.IconButton
import ua.acclorite.book_story.presentation.core.components.common.SearchTextField
import ua.acclorite.book_story.presentation.core.components.top_bar.TopAppBar
import ua.acclorite.book_story.presentation.core.components.top_bar.TopAppBarData
import ua.acclorite.book_story.presentation.core.navigation.NavigationIconButton
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryViewModel

/**
 * Library Top Bar.
 * Contains [TopAppBar] and [LibraryTabRow].
 *
 * @param pagerState [PagerState].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryTopBar(pagerState: PagerState) {
    val state = LibraryViewModel.getState()
    val onEvent = LibraryViewModel.getEvent()
    val focusRequester = remember { FocusRequester() }

    TopAppBar(
        scrollBehavior = null,
        isTopBarScrolled = state.value.hasSelectedItems,

        shownTopBar = when {
            state.value.hasSelectedItems -> 2
            state.value.showSearch -> 1
            else -> 0
        },
        topBars = listOf(
            TopAppBarData(
                contentID = 0,
                contentNavigationIcon = {},
                contentTitle = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(stringResource(id = R.string.library_screen))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = state.value.books.size.toString(),
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.surfaceContainer,
                                    RoundedCornerShape(14.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 3.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 16.sp
                        )
                    }
                },
                contentActions = {
                    IconButton(
                        icon = Icons.Default.Search,
                        contentDescription = R.string.search_content_desc,
                        disableOnClick = true,
                    ) {
                        onEvent(LibraryEvent.OnSearchShowHide)
                    }
                    NavigationIconButton()
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
                        onEvent(
                            LibraryEvent.OnSearchShowHide
                        )
                    }
                },
                contentTitle = {
                    SearchTextField(
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .onGloballyPositioned {
                                onEvent(LibraryEvent.OnRequestFocus(focusRequester))
                            },
                        query = state.value.searchQuery,
                        onQueryChange = {
                            onEvent(LibraryEvent.OnSearchQueryChange(it))
                        },
                        onSearch = {
                            onEvent(LibraryEvent.OnSearch)
                        }
                    )
                },
                contentActions = {
                    NavigationIconButton()
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
                        onEvent(LibraryEvent.OnClearSelectedBooks)
                    }
                },
                contentTitle = {
                    Text(
                        stringResource(
                            id = R.string.selected_items_count_query,
                            state.value.selectedItemsCount.coerceAtLeast(1)
                        ),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                },
                contentActions = {
                    IconButton(
                        icon = Icons.AutoMirrored.Outlined.DriveFileMove,
                        contentDescription = R.string.move_books_content_desc,
                        enabled = !state.value.isLoading
                                && !state.value.isRefreshing
                                && !state.value.showMoveDialog,
                        disableOnClick = false,

                        ) {
                        onEvent(LibraryEvent.OnShowHideMoveDialog)
                    }
                    IconButton(
                        icon = Icons.Outlined.Delete,
                        contentDescription = R.string.delete_books_content_desc,
                        enabled = !state.value.isLoading
                                && !state.value.isRefreshing
                                && !state.value.showDeleteDialog,
                        disableOnClick = false
                    ) {
                        onEvent(LibraryEvent.OnShowHideDeleteDialog)
                    }
                }
            ),
        ),
        customContent = {
            LibraryTabRow(
                onEvent = onEvent,
                books = state.value.categorizedBooks,
                itemCountBackgroundColor = animateColorAsState(
                    if (state.value.hasSelectedItems) MaterialTheme.colorScheme.surfaceContainerHighest
                    else MaterialTheme.colorScheme.surfaceContainer,
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                ).value,
                pagerState = pagerState
            )
        }
    )
}