package ua.acclorite.book_story.presentation.library

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
import ua.acclorite.book_story.domain.library.category.CategoryWithBooks
import ua.acclorite.book_story.presentation.core.components.common.IconButton
import ua.acclorite.book_story.presentation.core.components.common.SearchTextField
import ua.acclorite.book_story.presentation.core.components.top_bar.TopAppBar
import ua.acclorite.book_story.presentation.core.components.top_bar.TopAppBarData
import ua.acclorite.book_story.presentation.navigator.NavigatorIconButton
import ua.acclorite.book_story.ui.library.LibraryEvent

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
    showDeleteDialog: (LibraryEvent.OnShowDeleteDialog) -> Unit
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
                        Text(stringResource(id = R.string.library_screen))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = bookCount.toString(),
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
                        query = searchQuery,
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
                    Text(
                        stringResource(
                            id = R.string.selected_items_count_query,
                            selectedItemsCount.coerceAtLeast(1)
                        ),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                },
                contentActions = {
                    IconButton(
                        icon = Icons.AutoMirrored.Outlined.DriveFileMove,
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