package ua.acclorite.book_story.presentation.screens.book_info

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
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
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.AnimatedTopAppBar
import ua.acclorite.book_story.presentation.components.CustomIconButton
import ua.acclorite.book_story.presentation.components.CustomSnackbar
import ua.acclorite.book_story.presentation.components.GoBackButton
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
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoViewModel
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseViewModel
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.history.data.HistoryViewModel
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryViewModel
import ua.acclorite.book_story.presentation.ui.DefaultTransition
import ua.acclorite.book_story.presentation.ui.Transitions

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun BookInfoScreen(
    viewModel: BookInfoViewModel,
    libraryViewModel: LibraryViewModel,
    browseViewModel: BrowseViewModel,
    historyViewModel: HistoryViewModel,
    navigator: Navigator
) {
    val context = LocalContext.current

    val state by viewModel.state.collectAsState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val listState = rememberLazyListState()
    val snackbarState = remember { SnackbarHostState() }
    val refreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing || state.isLoadingUpdate,
        onRefresh = {
            viewModel.onEvent(
                BookInfoEvent.OnLoadUpdate(
                    snackbarState,
                    context
                )
            )
        }
    )
    val firstVisibleItemIndex by remember { derivedStateOf { listState.firstVisibleItemIndex } }

    LaunchedEffect(Unit) {
        viewModel.init(
            navigator
        )
    }

    if (state.showChangeCoverBottomSheet) {
        BookInfoChangeCoverBottomSheet(
            libraryViewModel = libraryViewModel,
            historyViewModel = historyViewModel,
            viewModel = viewModel
        )
    }
    if (state.showDetailsBottomSheet) {
        BookInfoDetailsBottomSheet(viewModel = viewModel)
    }
    if (state.showDeleteDialog) {
        BookInfoDeleteDialog(
            libraryViewModel = libraryViewModel,
            browseViewModel = browseViewModel,
            historyViewModel = historyViewModel,
            viewModel = viewModel,
            navigator = navigator
        )
    }
    if (state.showMoveDialog) {
        BookInfoMoveDialog(
            libraryViewModel = libraryViewModel,
            historyViewModel = historyViewModel,
            viewModel = viewModel,
            navigator = navigator
        )
    }
    if (state.showConfirmUpdateDialog) {
        BookInfoConfirmUpdateDialog(
            libraryViewModel = libraryViewModel,
            historyViewModel = historyViewModel,
            viewModel = viewModel,
            snackbarHostState = snackbarState
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
                    GoBackButton(navigator = navigator, enabled = !state.isRefreshing) {
                        viewModel.onEvent(BookInfoEvent.OnCancelUpdate)
                    }
                },
                content1Title = {
                    DefaultTransition(visible = firstVisibleItemIndex > 0) {
                        Text(
                            state.book.title,
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
                        enabled = !state.isRefreshing && !state.isLoadingUpdate
                    ) {
                        viewModel.onEvent(
                            BookInfoEvent.OnLoadUpdate(
                                snackbarState,
                                context
                            )
                        )
                    }

                    Box {
                        DefaultTransition(
                            visible = !state.editTitle
                        ) {
                            BookInfoMoreDropDown(
                                viewModel = viewModel,
                                snackbarState = snackbarState
                            )
                        }
                        androidx.compose.animation.AnimatedVisibility(
                            visible = state.editTitle,
                            enter = Transitions.DefaultTransitionIn,
                            exit = fadeOut(tween(200))
                        ) {
                            CustomIconButton(
                                icon = Icons.Default.Done,
                                contentDescription = R.string.apply_changes_content_desc,
                                disableOnClick = true,
                                enabled = !state.isRefreshing &&
                                        state.titleValue.isNotBlank() &&
                                        state.titleValue.trim() !=
                                        state.book.title.trim(),
                                color = if (state.titleValue.isNotBlank()
                                    && state.titleValue != state.book.title
                                ) MaterialTheme.colorScheme.onSurface
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            ) {
                                viewModel.onEvent(BookInfoEvent.OnUpdateTitle(
                                    refreshList = {
                                        libraryViewModel.onEvent(LibraryEvent.OnUpdateBook(it))
                                        historyViewModel.onEvent(HistoryEvent.OnUpdateBook(it))
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
                        if (state.book.coverImage != null) {
                            BookInfoBackground(
                                height = paddingValues.calculateTopPadding() + 12.dp + 195.dp,
                                image = state.book.coverImage!!
                            )
                        }

                        Column(Modifier.fillMaxWidth()) {
                            Spacer(modifier = Modifier.height(paddingValues.calculateTopPadding() + 12.dp))
                            // Info
                            BookInfoInfoSection(viewModel = viewModel, book = state.book)
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    // Statistic
                    BookInfoStatisticSection(book = state.book)
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    // Description
                    BookInfoDescriptionSection(book = state.book)
                }
            }

            FloatingActionButton(
                onClick = {
                    if (!state.isRefreshing) {
                        viewModel.onEvent(BookInfoEvent.OnNavigateToReaderScreen(navigator))
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
                        AnimatedVisibility(
                            visible = !listState.canScrollBackward
                        ) {
                            Text(
                                text = stringResource(
                                    if (state.book.progress == 0f) R.string.start
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
                state.isRefreshing || state.isLoadingUpdate,
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
        if (!state.isRefreshing) {
            viewModel.onEvent(BookInfoEvent.OnCancelUpdate)
            navigator.navigateBack()
        }
    }
}