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
import androidx.compose.runtime.State
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
import ua.acclorite.book_story.presentation.components.AnimatedTopAppBar
import ua.acclorite.book_story.presentation.components.CustomIconButton
import ua.acclorite.book_story.presentation.components.CustomSearchTextField
import ua.acclorite.book_story.presentation.components.MoreDropDown
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryState

/**
 * Library Top Bar.
 * Contains [AnimatedTopAppBar] and [LibraryTabRow].
 *
 * @param state [LibraryState].
 * @param pagerState [PagerState].
 * @param onEvent [LibraryEvent] callback.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryTopBar(
    state: State<LibraryState>,
    pagerState: PagerState,
    onEvent: (LibraryEvent) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    AnimatedTopAppBar(
        scrollBehavior = null,
        isTopBarScrolled = state.value.hasSelectedItems,

        content1Visibility = !state.value.hasSelectedItems && !state.value.showSearch,
        content1NavigationIcon = {},
        content1Title = {
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
        content1Actions = {
            CustomIconButton(
                icon = Icons.Default.Search,
                contentDescription = R.string.search_content_desc,
                disableOnClick = true,
            ) {
                onEvent(LibraryEvent.OnSearchShowHide)
            }
            MoreDropDown()
        },

        content2Visibility = state.value.hasSelectedItems,
        content2NavigationIcon = {
            CustomIconButton(
                icon = Icons.Default.Clear,
                contentDescription = R.string.clear_selected_items_content_desc,
                disableOnClick = true
            ) {
                onEvent(LibraryEvent.OnClearSelectedBooks)
            }
        },
        content2Title = {
            Text(
                stringResource(
                    id = R.string.selected_items_count_query,
                    state.value.selectedItemsCount.coerceAtLeast(1)
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        },
        content2Actions = {
            CustomIconButton(
                icon = Icons.AutoMirrored.Outlined.DriveFileMove,
                contentDescription = R.string.move_books_content_desc,
                enabled = !state.value.isLoading
                        && !state.value.isRefreshing
                        && !state.value.showMoveDialog,
                disableOnClick = false,

                ) {
                onEvent(LibraryEvent.OnShowHideMoveDialog)
            }
            CustomIconButton(
                icon = Icons.Outlined.Delete,
                contentDescription = R.string.delete_books_content_desc,
                enabled = !state.value.isLoading
                        && !state.value.isRefreshing
                        && !state.value.showDeleteDialog,
                disableOnClick = false
            ) {
                onEvent(LibraryEvent.OnShowHideDeleteDialog)
            }
        },

        content3Visibility = state.value.showSearch && !state.value.hasSelectedItems,
        content3NavigationIcon = {
            CustomIconButton(
                icon = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = R.string.exit_search_content_desc,
                disableOnClick = true
            ) {
                onEvent(
                    LibraryEvent.OnSearchShowHide
                )
            }
        },
        content3Title = {
            CustomSearchTextField(
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
                },
                placeholder = stringResource(
                    id = R.string.search_query,
                    stringResource(id = R.string.books)
                )
            )
        },
        content3Actions = {
            MoreDropDown()
        },
        customContent = {
            LibraryTabRow(
                onEvent = onEvent,
                books = state.value.categorizedBooks,
                itemCountBackgroundColor = animateColorAsState(
                    if (state.value.hasSelectedItems) MaterialTheme.colorScheme.surfaceContainerHighest
                    else MaterialTheme.colorScheme.surfaceContainer,
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                    label = stringResource(id = R.string.top_app_bar_anim_content_desc)
                ).value,
                pagerState = pagerState
            )
        }
    )
}