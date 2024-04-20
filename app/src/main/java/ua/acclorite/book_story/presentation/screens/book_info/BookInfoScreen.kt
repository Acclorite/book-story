package ua.acclorite.book_story.presentation.screens.book_info

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.AnimatedTopAppBar
import ua.acclorite.book_story.presentation.components.CustomAnimatedVisibility
import ua.acclorite.book_story.presentation.components.CustomIconButton
import ua.acclorite.book_story.presentation.components.CustomSnackbar
import ua.acclorite.book_story.presentation.components.GoBackButton
import ua.acclorite.book_story.presentation.data.LocalNavigator
import ua.acclorite.book_story.presentation.data.Navigator
import ua.acclorite.book_story.presentation.screens.book_info.components.BookInfoBackground
import ua.acclorite.book_story.presentation.screens.book_info.components.BookInfoDescriptionSection
import ua.acclorite.book_story.presentation.screens.book_info.components.BookInfoInfoSection
import ua.acclorite.book_story.presentation.screens.book_info.components.BookInfoMoreDropDown
import ua.acclorite.book_story.presentation.screens.book_info.components.BookInfoStatisticSection
import ua.acclorite.book_story.presentation.screens.book_info.components.change_cover_bottom_sheet.BookInfoChangeCoverBottomSheet
import ua.acclorite.book_story.presentation.screens.book_info.components.confirm_update_dialog.BookInfoConfirmUpdateDialog
import ua.acclorite.book_story.presentation.screens.book_info.components.details_bottom_sheet.BookInfoDetailsBottomSheet
import ua.acclorite.book_story.presentation.screens.book_info.components.dialog.BookInfoDeleteDialog
import ua.acclorite.book_story.presentation.screens.book_info.components.dialog.BookInfoMoveDialog
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoEvent
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoState
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoViewModel
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseEvent
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseViewModel
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.history.data.HistoryViewModel
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryViewModel
import ua.acclorite.book_story.presentation.ui.DefaultTransition
import ua.acclorite.book_story.presentation.ui.Transitions

@Composable
fun BookInfoScreenRoot() {
    val navigator = LocalNavigator.current

    val viewModel: BookInfoViewModel = hiltViewModel()
    val libraryViewModel: LibraryViewModel = hiltViewModel()
    val historyViewModel: HistoryViewModel = hiltViewModel()
    val browseViewModel: BrowseViewModel = hiltViewModel()

    val state = viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.init(
            navigator
        )
    }

    BookInfoScreen(
        state = state,
        navigator = navigator,
        onEvent = viewModel::onEvent,
        onLibraryEvent = libraryViewModel::onEvent,
        onBrowseEvent = browseViewModel::onEvent,
        onHistoryEvent = historyViewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun BookInfoScreen(
    state: State<BookInfoState>,
    navigator: Navigator,
    onEvent: (BookInfoEvent) -> Unit,
    onLibraryEvent: (LibraryEvent) -> Unit,
    onBrowseEvent: (BrowseEvent) -> Unit,
    onHistoryEvent: (HistoryEvent) -> Unit
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val listState = rememberLazyListState()
    val snackbarState = remember { SnackbarHostState() }
    val refreshState = rememberPullRefreshState(
        refreshing = state.value.isRefreshing || state.value.isLoadingUpdate,
        onRefresh = {
            onEvent(
                BookInfoEvent.OnLoadUpdate(
                    snackbarState,
                    context
                )
            )
        }
    )
    val firstVisibleItemIndex by remember { derivedStateOf { listState.firstVisibleItemIndex } }

    if (state.value.showChangeCoverBottomSheet) {
        BookInfoChangeCoverBottomSheet(
            state = state,
            onEvent = onEvent,
            onLibraryUpdateEvent = onLibraryEvent,
            onHistoryUpdateEvent = onHistoryEvent
        )
    }
    if (state.value.showDetailsBottomSheet) {
        BookInfoDetailsBottomSheet(
            state = state,
            onEvent = onEvent
        )
    }
    if (state.value.showDeleteDialog) {
        BookInfoDeleteDialog(
            onEvent = onEvent,
            onLibraryLoadEvent = onLibraryEvent,
            onHistoryLoadEvent = onHistoryEvent,
            onBrowseLoadEvent = onBrowseEvent
        )
    }
    if (state.value.showMoveDialog) {
        BookInfoMoveDialog(
            state = state,
            onEvent = onEvent,
            onLibraryEvent = onLibraryEvent,
            onHistoryUpdateEvent = onHistoryEvent
        )
    }
    if (state.value.showConfirmUpdateDialog) {
        BookInfoConfirmUpdateDialog(
            state = state,
            snackbarHostState = snackbarState,
            onEvent = onEvent,
            onLibraryUpdateEvent = onLibraryEvent,
            onHistoryUpdateEvent = onHistoryEvent
        )
    }

    Scaffold(
        Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .pullRefresh(refreshState)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            AnimatedTopAppBar(
                containerColor = Color.Transparent,
                scrollBehavior = scrollBehavior,
                isTopBarScrolled = null,

                content1NavigationIcon = {
                    if (state.value.editTitle) {
                        CustomIconButton(
                            icon = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = R.string.hide_edit_title_content_desc,
                            disableOnClick = true
                        ) {
                            onEvent(BookInfoEvent.OnShowHideEditTitle)
                        }
                    } else {
                        GoBackButton(navigator = navigator, enabled = !state.value.isRefreshing) {
                            onEvent(BookInfoEvent.OnCancelUpdate)
                        }
                    }
                },
                content1Title = {
                    DefaultTransition(visible = firstVisibleItemIndex > 0) {
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
                            visible = !state.value.editTitle
                        ) {
                            BookInfoMoreDropDown(
                                state = state,
                                onEvent = onEvent,
                                snackbarState = snackbarState
                            )
                        }
                        CustomAnimatedVisibility(
                            visible = state.value.editTitle,
                            enter = Transitions.DefaultTransitionIn,
                            exit = fadeOut(tween(200))
                        ) {
                            CustomIconButton(
                                icon = Icons.Default.Done,
                                contentDescription = R.string.apply_changes_content_desc,
                                disableOnClick = true,
                                enabled = !state.value.isRefreshing &&
                                        state.value.titleValue.isNotBlank() &&
                                        state.value.titleValue.trim() !=
                                        state.value.book.title.trim(),
                                color = if (state.value.titleValue.isNotBlank()
                                    && state.value.titleValue != state.value.book.title
                                ) MaterialTheme.colorScheme.onSurface
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            ) {
                                onEvent(
                                    BookInfoEvent.OnUpdateTitle(
                                        refreshList = {
                                            onLibraryEvent(LibraryEvent.OnUpdateBook(it))
                                            onHistoryEvent(HistoryEvent.OnUpdateBook(it))
                                        }
                                    ))
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.title_changed),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
            )
        },
        bottomBar = {
            CustomSnackbar(
                modifier = Modifier.padding(bottom = 70.dp),
                snackbarState = snackbarState
            )
        }
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
        ) {
            LazyColumn(
                Modifier
                    .fillMaxSize(),
                state = listState
            ) {
                item {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        if (state.value.book.coverImage != null) {
                            BookInfoBackground(
                                height = paddingValues.calculateTopPadding() + 12.dp + 195.dp,
                                image = state.value.book.coverImage!!
                            )
                        }

                        Column(Modifier.fillMaxWidth()) {
                            Spacer(modifier = Modifier.height(paddingValues.calculateTopPadding() + 12.dp))
                            // Info
                            BookInfoInfoSection(state = state, onEvent = onEvent)
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    // Statistic
                    BookInfoStatisticSection(state = state)
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    // Description
                    BookInfoDescriptionSection(state = state)
                }
            }

            FloatingActionButton(
                onClick = {
                    if (!state.value.isRefreshing) {
                        onEvent(BookInfoEvent.OnNavigateToReaderScreen(navigator))
                    }
                },
                shape = MaterialTheme.shapes.large,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                elevation = FloatingActionButtonDefaults.elevation(),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                content = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(modifier = Modifier.width(13.dp))
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = stringResource(id = R.string.continue_reading_content_desc),
                            Modifier.size(24.dp)
                        )
                        CustomAnimatedVisibility(
                            visible = !listState.canScrollBackward,
                            enter = fadeIn() + expandHorizontally(),
                            exit = fadeOut() + shrinkHorizontally()
                        ) {
                            Text(
                                text = stringResource(
                                    if (state.value.book.progress == 0f) R.string.start
                                    else R.string.continue_read
                                ),
                                style = MaterialTheme.typography.labelLarge,
                                maxLines = 1,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }
            )

            PullRefreshIndicator(
                state.value.isRefreshing || state.value.isLoadingUpdate,
                refreshState,
                Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = paddingValues.calculateTopPadding()),
                backgroundColor = MaterialTheme.colorScheme.inverseSurface,
                contentColor = MaterialTheme.colorScheme.inverseOnSurface
            )
        }
    }

    BackHandler {
        if (state.value.editTitle) {
            onEvent(BookInfoEvent.OnShowHideEditTitle)
            return@BackHandler
        }

        if (!state.value.isRefreshing) {
            onEvent(BookInfoEvent.OnCancelUpdate)
            navigator.navigateBack()
        }
    }
}