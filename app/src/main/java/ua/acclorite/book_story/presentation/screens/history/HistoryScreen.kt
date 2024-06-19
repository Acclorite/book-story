package ua.acclorite.book_story.presentation.screens.history

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.util.OnNavigate
import ua.acclorite.book_story.presentation.components.AnimatedTopAppBar
import ua.acclorite.book_story.presentation.components.CategoryTitle
import ua.acclorite.book_story.presentation.components.CustomAnimatedVisibility
import ua.acclorite.book_story.presentation.components.CustomIconButton
import ua.acclorite.book_story.presentation.components.CustomSearchTextField
import ua.acclorite.book_story.presentation.components.CustomSnackbar
import ua.acclorite.book_story.presentation.components.MoreDropDown
import ua.acclorite.book_story.presentation.components.customItems
import ua.acclorite.book_story.presentation.components.is_messages.IsEmpty
import ua.acclorite.book_story.presentation.data.LocalNavigator
import ua.acclorite.book_story.presentation.data.Screen
import ua.acclorite.book_story.presentation.screens.history.components.HistoryDeleteWholeHistoryDialog
import ua.acclorite.book_story.presentation.screens.history.components.HistoryItem
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.history.data.HistoryState
import ua.acclorite.book_story.presentation.screens.history.data.HistoryViewModel
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryViewModel
import ua.acclorite.book_story.presentation.ui.DefaultTransition
import ua.acclorite.book_story.presentation.ui.Transitions

@Composable
fun HistoryScreenRoot() {
    val navigator = LocalNavigator.current
    val viewModel: HistoryViewModel = hiltViewModel()
    val libraryViewModel: LibraryViewModel = hiltViewModel()

    val state = viewModel.state.collectAsState()

    HistoryScreen(
        state = state,
        onNavigate = { navigator.it() },
        onEvent = viewModel::onEvent,
        onLibraryEvent = libraryViewModel::onEvent
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@Composable
private fun HistoryScreen(
    state: State<HistoryState>,
    onNavigate: OnNavigate,
    onEvent: (HistoryEvent) -> Unit,
    onLibraryEvent: (LibraryEvent) -> Unit
) {
    val context = LocalContext.current
    val refreshState = rememberPullRefreshState(
        refreshing = state.value.isRefreshing,
        onRefresh = {
            onEvent(HistoryEvent.OnRefreshList)
        }
    )
    val focusRequester = remember { FocusRequester() }
    val snackbarState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        onEvent(HistoryEvent.OnUpdateScrollOffset)
    }

    if (state.value.showDeleteWholeHistoryDialog) {
        HistoryDeleteWholeHistoryDialog(
            onEvent = onEvent,
            onLibraryLoadEvent = onLibraryEvent
        )
    }

    Scaffold(
        Modifier
            .fillMaxSize()
            .pullRefresh(refreshState),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            AnimatedTopAppBar(
                scrollBehavior = null,
                isTopBarScrolled = state.value.listState.canScrollBackward,

                content1Visibility = !state.value.showSearch,
                content1NavigationIcon = {},
                content1Title = {
                    Text(
                        stringResource(id = R.string.history_screen)
                    )
                },
                content1Actions = {
                    CustomIconButton(
                        icon = Icons.Default.Search,
                        contentDescription = R.string.search_content_desc,
                        disableOnClick = true,
                    ) {
                        onEvent(HistoryEvent.OnSearchShowHide)
                    }
                    CustomIconButton(
                        icon = Icons.Outlined.DeleteSweep,
                        contentDescription = R.string.delete_whole_history_content_desc,
                        disableOnClick = false,
                        enabled = !state.value.isLoading
                                && !state.value.isRefreshing
                                && state.value.history.isNotEmpty()
                                && !state.value.showDeleteWholeHistoryDialog
                    ) {
                        onEvent(HistoryEvent.OnShowHideDeleteWholeHistoryDialog)
                    }
                    MoreDropDown()
                },

                content2Visibility = state.value.showSearch,
                content2NavigationIcon = {
                    CustomIconButton(
                        icon = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = R.string.exit_search_content_desc,
                        disableOnClick = true
                    ) {
                        onEvent(HistoryEvent.OnSearchShowHide)
                    }
                },
                content2Title = {
                    CustomSearchTextField(
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .onGloballyPositioned {
                                onEvent(HistoryEvent.OnRequestFocus(focusRequester))
                            },
                        query = state.value.searchQuery,
                        onQueryChange = {
                            onEvent(HistoryEvent.OnSearchQueryChange(it))
                        },
                        onSearch = {
                            onEvent(HistoryEvent.OnSearch)
                        },
                        placeholder = stringResource(
                            id = R.string.search_query,
                            stringResource(id = R.string.history)
                        )
                    )
                },
                content2Actions = {
                    MoreDropDown()
                },
                content3Visibility = false
            )
        },
        bottomBar = {
            CustomSnackbar(snackbarState = snackbarState)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            DefaultTransition(visible = !state.value.isLoading) {
                LazyColumn(
                    Modifier
                        .fillMaxSize(),
                    state = state.value.listState,
                ) {
                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    state.value.history.forEachIndexed { index, groupedHistory ->
                        item(key = groupedHistory.title) {
                            if (index > 0) {
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            CategoryTitle(
                                modifier = Modifier.animateItem(),
                                title = when (groupedHistory.title) {
                                    "today" -> stringResource(id = R.string.today)
                                    "yesterday" -> stringResource(id = R.string.yesterday)
                                    else -> groupedHistory.title
                                },
                                padding = 16.dp
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        customItems(
                            groupedHistory.history, key = { it.id }
                        ) {
                            HistoryItem(
                                modifier = Modifier.animateItem(),
                                history = it,
                                isOnClickEnabled = !state.value.isRefreshing,
                                onBodyClick = {
                                    onNavigate {
                                        navigate(
                                            Screen.BookInfo(it.bookId)
                                        )
                                    }
                                },
                                onTitleClick = {
                                    onEvent(
                                        HistoryEvent.OnNavigateToReaderScreen(
                                            onNavigate = onNavigate,
                                            book = it.book!!
                                        )
                                    )
                                },
                                isDeleteEnabled = !state.value.isRefreshing,
                                onDeleteClick = {
                                    onEvent(
                                        HistoryEvent.OnDeleteHistoryElement(
                                            historyToDelete = it,
                                            snackbarState = snackbarState,
                                            context = context,
                                            refreshList = {
                                                onLibraryEvent(
                                                    LibraryEvent.OnLoadList
                                                )
                                            }
                                        )
                                    )
                                }
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            CustomAnimatedVisibility(
                visible = !state.value.isLoading
                        && state.value.history.isEmpty()
                        && !state.value.isRefreshing,
                modifier = Modifier.align(Alignment.Center),
                enter = Transitions.DefaultTransitionIn,
                exit = fadeOut(tween(0))
            ) {
                IsEmpty(
                    modifier = Modifier.align(Alignment.Center),
                    message = stringResource(id = R.string.history_empty),
                    icon = painterResource(id = R.drawable.empty_history)
                )
            }

            PullRefreshIndicator(
                state.value.isRefreshing,
                refreshState,
                Modifier.align(Alignment.TopCenter),
                backgroundColor = MaterialTheme.colorScheme.inverseSurface,
                contentColor = MaterialTheme.colorScheme.inverseOnSurface
            )
        }
    }

    BackHandler {
        if (state.value.showSearch) {
            onEvent(HistoryEvent.OnSearchShowHide)
            return@BackHandler
        }

        onNavigate {
            navigate(Screen.Library)
        }
    }
}












