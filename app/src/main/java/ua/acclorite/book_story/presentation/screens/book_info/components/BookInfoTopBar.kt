package ua.acclorite.book_story.presentation.screens.book_info.components

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.AnimatedTopAppBar
import ua.acclorite.book_story.presentation.core.components.CustomAnimatedVisibility
import ua.acclorite.book_story.presentation.core.components.CustomIconButton
import ua.acclorite.book_story.presentation.core.components.GoBackButton
import ua.acclorite.book_story.presentation.core.components.LocalBookInfoViewModel
import ua.acclorite.book_story.presentation.core.components.LocalHistoryViewModel
import ua.acclorite.book_story.presentation.core.components.LocalLibraryViewModel
import ua.acclorite.book_story.presentation.core.navigation.LocalOnNavigate
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoEvent
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.ui.DefaultTransition
import ua.acclorite.book_story.presentation.ui.Transitions

/**
 * Book Info Top Bar.
 *
 * @param listState [LazyListState].
 * @param snackbarState [SnackbarHostState].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookInfoTopBar(
    listState: LazyListState,
    snackbarState: SnackbarHostState
) {
    val state = LocalBookInfoViewModel.current.state
    val onEvent = LocalBookInfoViewModel.current.onEvent
    val onLibraryEvent = LocalLibraryViewModel.current.onEvent
    val onHistoryEvent = LocalHistoryViewModel.current.onEvent
    val onNavigate = LocalOnNavigate.current
    val context = LocalContext.current

    val firstVisibleItemIndex by remember { derivedStateOf { listState.firstVisibleItemIndex } }

    AnimatedTopAppBar(
        containerColor = MaterialTheme.colorScheme.surface.copy(0f),
        scrollBehavior = null,
        isTopBarScrolled = listState.canScrollBackward,

        content1NavigationIcon = {
            if (
                state.value.editTitle ||
                state.value.editAuthor ||
                state.value.editDescription
            ) {
                CustomIconButton(
                    icon = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = R.string.exit_editing_content_desc,
                    disableOnClick = true
                ) {
                    if (state.value.editTitle) {
                        onEvent(BookInfoEvent.OnShowHideEditTitle)
                    }

                    if (state.value.editAuthor) {
                        onEvent(BookInfoEvent.OnShowHideEditAuthor)
                    }

                    if (state.value.editDescription) {
                        onEvent(BookInfoEvent.OnShowHideEditDescription)
                    }
                }
            } else {
                GoBackButton(onNavigate = onNavigate, enabled = !state.value.isRefreshing) {
                    onEvent(BookInfoEvent.OnCancelUpdate)
                }
            }
        },
        content1Title = {
            DefaultTransition(
                firstVisibleItemIndex > 0 && !state.value.editTitle
            ) {
                Text(
                    state.value.book.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        },
        content1Actions = {
            CustomIconButton(
                icon = Icons.Outlined.Refresh,
                contentDescription = R.string.refresh_book_content_desc,
                disableOnClick = false,
                enabled = !state.value.isRefreshing && !state.value.isLoadingUpdate
            ) {
                onEvent(
                    BookInfoEvent.OnLoadUpdate(
                        snackbarState,
                        context
                    )
                )
            }

            Box {
                DefaultTransition(
                    visible = !state.value.editTitle &&
                            !state.value.editAuthor &&
                            !state.value.editDescription
                ) {
                    BookInfoMoreDropDown(snackbarState = snackbarState)
                }
                CustomAnimatedVisibility(
                    visible = state.value.editTitle ||
                            state.value.editAuthor ||
                            state.value.editDescription,
                    enter = Transitions.DefaultTransitionIn,
                    exit = fadeOut(tween(200))
                ) {
                    CustomIconButton(
                        icon = Icons.Outlined.Done,
                        contentDescription = R.string.apply_changes_content_desc,
                        disableOnClick = true,
                        enabled = !state.value.isRefreshing &&
                                (state.value.titleValue.isNotBlank() &&
                                        state.value.titleValue.trim() !=
                                        state.value.book.title.trim()) ||

                                (state.value.authorValue.isNotBlank() &&
                                        state.value.authorValue.trim() !=
                                        state.value.book.author.getAsString()?.trim()) ||

                                (state.value.descriptionValue.isNotBlank() &&
                                        state.value.descriptionValue.trim() !=
                                        state.value.book.description?.trim()),

                        color = if ((state.value.titleValue.isNotBlank() &&
                                    state.value.titleValue.trim() !=
                                    state.value.book.title.trim()) ||

                            (state.value.authorValue.isNotBlank() &&
                                    state.value.authorValue.trim() !=
                                    state.value.book.author.getAsString()?.trim()) ||

                            (state.value.descriptionValue.isNotBlank() &&
                                    state.value.descriptionValue.trim() !=
                                    state.value.book.description?.trim())
                        ) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    ) {
                        onEvent(
                            BookInfoEvent.OnUpdateData(
                                refreshList = {
                                    onLibraryEvent(LibraryEvent.OnUpdateBook(it))
                                    onHistoryEvent(HistoryEvent.OnUpdateBook(it))
                                }
                            )
                        )
                    }
                }
            }
        }
    )
}