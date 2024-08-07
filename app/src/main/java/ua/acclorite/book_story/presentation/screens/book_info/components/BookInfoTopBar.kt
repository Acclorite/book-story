package ua.acclorite.book_story.presentation.screens.book_info.components

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.util.OnNavigate
import ua.acclorite.book_story.presentation.components.AnimatedTopAppBar
import ua.acclorite.book_story.presentation.components.CustomAnimatedVisibility
import ua.acclorite.book_story.presentation.components.CustomIconButton
import ua.acclorite.book_story.presentation.components.GoBackButton
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoEvent
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoState
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.ui.DefaultTransition
import ua.acclorite.book_story.presentation.ui.Transitions

/**
 * Book Info Top Bar.
 *
 * @param state [BookInfoState].
 * @param onEvent [BookInfoEvent] callback.
 * @param onLibraryEvent [LibraryEvent] callback.
 * @param onHistoryEvent [HistoryEvent] callback.
 * @param onNavigate Navigator callback.
 * @param listState [LazyListState].
 * @param snackbarState [SnackbarHostState].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookInfoTopBar(
    state: State<BookInfoState>,
    onEvent: (BookInfoEvent) -> Unit,
    onLibraryEvent: (LibraryEvent) -> Unit,
    onHistoryEvent: (HistoryEvent) -> Unit,
    onNavigate: OnNavigate,
    listState: LazyListState,
    snackbarState: SnackbarHostState
) {
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
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
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
                icon = Icons.Default.Refresh,
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
                    BookInfoMoreDropDown(
                        state = state,
                        onEvent = onEvent,
                        snackbarState = snackbarState
                    )
                }
                CustomAnimatedVisibility(
                    visible = state.value.editTitle ||
                            state.value.editAuthor ||
                            state.value.editDescription,
                    enter = Transitions.DefaultTransitionIn,
                    exit = fadeOut(tween(200))
                ) {
                    CustomIconButton(
                        icon = Icons.Default.Done,
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
                            ))
                    }
                }
            }
        }
    )
}