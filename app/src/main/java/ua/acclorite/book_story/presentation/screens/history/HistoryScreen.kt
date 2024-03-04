package ua.acclorite.book_story.presentation.screens.history

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.Argument
import ua.acclorite.book_story.presentation.Navigator
import ua.acclorite.book_story.presentation.Screen
import ua.acclorite.book_story.presentation.components.AnimatedTopAppBar
import ua.acclorite.book_story.presentation.components.CategoryTitle
import ua.acclorite.book_story.presentation.components.CustomSnackbar
import ua.acclorite.book_story.presentation.components.IsEmpty
import ua.acclorite.book_story.presentation.components.MoreDropDown
import ua.acclorite.book_story.presentation.screens.history.components.HistoryDeleteWholeHistoryDialog
import ua.acclorite.book_story.presentation.screens.history.components.HistoryItem
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.history.data.HistoryViewModel
import ua.acclorite.book_story.ui.Transitions
import ua.acclorite.book_story.ui.elevation
import java.util.UUID

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class, FlowPreview::class, ExperimentalFoundationApi::class
)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel(),
    navigator: Navigator
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val refreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing,
        onRefresh = {
            viewModel.onEvent(HistoryEvent.OnRefreshList)
        }
    )
    val focusRequester = remember { FocusRequester() }
    val snackbarState = remember { SnackbarHostState() }
    val listState = rememberLazyListState(state.scrollIndex, state.scrollOffset)

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex
        }
            .debounce(10L)
            .collectLatest {
                viewModel.onEvent(HistoryEvent.OnUpdateScrollIndex(it))
            }
    }
    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemScrollOffset
        }
            .debounce(10L)
            .collectLatest {
                viewModel.onEvent(
                    HistoryEvent.OnUpdateScrollOffset(it)
                )
            }
    }

    if (state.showDeleteWholeHistoryDialog) {
        HistoryDeleteWholeHistoryDialog(viewModel = viewModel)
    }

    Scaffold(
        Modifier
            .fillMaxSize()
            .pullRefresh(refreshState),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            AnimatedTopAppBar(
                scrolledContainerColor = MaterialTheme.elevation(),
                scrollBehavior = null,
                isTopBarScrolled = (state.scrollIndex > 0 || state.scrollOffset > 0) && !state.isLoading,

                content1Visibility = !state.showSearch,
                content1NavigationIcon = {},
                content1Title = {
                    Text(
                        stringResource(id = R.string.history_screen),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                },
                content1Actions = {
                    IconButton(onClick = { viewModel.onEvent(HistoryEvent.OnSearchShowHide) }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search history",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        enabled = !state.isLoading && !state.isRefreshing && state.history.isNotEmpty(),
                        onClick = {
                            viewModel.onEvent(HistoryEvent.OnShowHideDeleteWholeHistoryDialog)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.DeleteSweep,
                            contentDescription = "Delete whole history",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    MoreDropDown(navigator = navigator)
                },

                content2Visibility = state.showSearch,
                content2NavigationIcon = {
                    IconButton(onClick = { viewModel.onEvent(HistoryEvent.OnSearchShowHide) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Exit search mode",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                content2Title = {
                    BasicTextField(
                        value = state.searchQuery,
                        singleLine = true,
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            lineHeight = MaterialTheme.typography.titleLarge.lineHeight,
                            fontFamily = MaterialTheme.typography.titleLarge.fontFamily
                        ),
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .onGloballyPositioned {
                                viewModel.onEvent(HistoryEvent.OnRequestFocus(focusRequester))
                            },
                        onValueChange = {
                            viewModel.onEvent(HistoryEvent.OnSearchQueryChange(it))
                        },
                        keyboardOptions = KeyboardOptions(KeyboardCapitalization.Words),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurfaceVariant)
                    ) { innerText ->
                        Box(
                            modifier = Modifier.fillMaxHeight(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (state.searchQuery.isEmpty()) {
                                Text(
                                    stringResource(
                                        id = R.string.search_query,
                                        stringResource(id = R.string.history)
                                    ),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.titleLarge,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                            innerText()
                        }
                    }
                },
                content2Actions = {
                    MoreDropDown(navigator = navigator)
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
            LazyColumn(
                Modifier
                    .fillMaxSize(),
                state = listState,
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                if (!state.isLoading) {
                    state.history.forEachIndexed { index, groupedHistory ->
                        item(key = groupedHistory.title) {
                            if (index > 0) {
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            CategoryTitle(
                                modifier = Modifier.animateItemPlacement(tween(300)),
                                title = when (groupedHistory.title) {
                                    "today" -> stringResource(id = R.string.today)
                                    "yesterday" -> stringResource(id = R.string.yesterday)
                                    else -> groupedHistory.title
                                },
                                padding = 16.dp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        items(
                            groupedHistory.history, key = { it.id ?: UUID.randomUUID() }
                        ) {
                            HistoryItem(
                                modifier = Modifier.animateItemPlacement(tween(300)),
                                history = it,
                                onBodyClick = {
                                    navigator.navigate(
                                        Screen.BOOK_INFO,
                                        Argument("book", it.book)
                                    )
                                },
                                onTitleClick = {
                                    navigator.navigate(
                                        Screen.READER,
                                        Argument("book", it.book)
                                    )
                                },
                                onDeleteClick = {
                                    viewModel.onEvent(
                                        HistoryEvent.OnDeleteHistoryElement(
                                            it,
                                            snackbarState,
                                            context
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = !state.isLoading && state.history.isEmpty() && !state.isRefreshing,
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
            if (state.isLoading && !state.isRefreshing) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeCap = StrokeCap.Round,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(36.dp)
                )
            }

            PullRefreshIndicator(
                state.isRefreshing,
                refreshState,
                Modifier.align(Alignment.TopCenter),
                backgroundColor = MaterialTheme.elevation(),
                contentColor = MaterialTheme.colorScheme.primary,
                scale = true
            )
        }
    }

    BackHandler {
        if (state.showSearch) {
            viewModel.onEvent(HistoryEvent.OnSearchShowHide)
            return@BackHandler
        }

        navigator.navigate(Screen.LIBRARY)
    }
}












