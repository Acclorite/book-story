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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.AnimatedTopAppBar
import ua.acclorite.book_story.presentation.components.CategoryTitle
import ua.acclorite.book_story.presentation.components.CustomIconButton
import ua.acclorite.book_story.presentation.components.CustomSnackbar
import ua.acclorite.book_story.presentation.components.MoreDropDown
import ua.acclorite.book_story.presentation.components.is_messages.IsEmpty
import ua.acclorite.book_story.presentation.data.Argument
import ua.acclorite.book_story.presentation.data.Navigator
import ua.acclorite.book_story.presentation.data.Screen
import ua.acclorite.book_story.presentation.screens.history.components.HistoryDeleteWholeHistoryDialog
import ua.acclorite.book_story.presentation.screens.history.components.HistoryItem
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.history.data.HistoryViewModel
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryViewModel
import ua.acclorite.book_story.presentation.ui.DefaultTransition
import ua.acclorite.book_story.presentation.ui.Transitions
import ua.acclorite.book_story.presentation.ui.elevation
import java.util.UUID

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class, ExperimentalFoundationApi::class
)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    libraryViewModel: LibraryViewModel,
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

    if (state.showDeleteWholeHistoryDialog) {
        HistoryDeleteWholeHistoryDialog(viewModel = viewModel, libraryViewModel = libraryViewModel)
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
                isTopBarScrolled = state.listState.canScrollBackward,

                content1Visibility = !state.showSearch,
                content1NavigationIcon = {},
                content1Title = {
                    Text(
                        stringResource(id = R.string.history_screen)
                    )
                },
                content1Actions = {
                    CustomIconButton(
                        icon = Icons.Default.Search,
                        contentDescription = stringResource(id = R.string.search_content_desc),
                        disableOnClick = true,
                    ) {
                        viewModel.onEvent(HistoryEvent.OnSearchShowHide)
                    }
                    CustomIconButton(
                        icon = Icons.Outlined.DeleteSweep,
                        contentDescription = stringResource(
                            id = R.string.delete_whole_history_content_desc
                        ),
                        disableOnClick = false,
                        enabled = !state.isLoading
                                && !state.isRefreshing
                                && state.history.isNotEmpty()
                                && !state.showDeleteWholeHistoryDialog
                    ) {
                        viewModel.onEvent(HistoryEvent.OnShowHideDeleteWholeHistoryDialog)
                    }
                    MoreDropDown(navigator = navigator)
                },

                content2Visibility = state.showSearch,
                content2NavigationIcon = {
                    CustomIconButton(
                        icon = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = stringResource(
                            id = R.string.exit_search_content_desc
                        ),
                        disableOnClick = true
                    ) {
                        viewModel.onEvent(HistoryEvent.OnSearchShowHide)
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
            DefaultTransition(visible = !state.isLoading) {
                LazyColumn(
                    Modifier
                        .fillMaxSize(),
                    state = state.listState,
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    state.history.forEachIndexed { index, groupedHistory ->
                        item(key = groupedHistory.title) {
                            if (index > 0) {
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            CategoryTitle(
                                modifier = Modifier.animateItemPlacement(),
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
                                modifier = Modifier.animateItemPlacement(),
                                history = it,
                                isOnClickEnabled = !state.isRefreshing,
                                onBodyClick = {
                                    navigator.navigate(
                                        Screen.BOOK_INFO,
                                        false,
                                        Argument("book", it.book)
                                    )
                                },
                                onTitleClick = {
                                    viewModel.onEvent(
                                        HistoryEvent.OnNavigateToReaderScreen(
                                            navigator,
                                            it.book
                                        )
                                    )
                                },
                                isDeleteEnabled = !state.isRefreshing,
                                onDeleteClick = {
                                    viewModel.onEvent(
                                        HistoryEvent.OnDeleteHistoryElement(
                                            historyToDelete = it,
                                            snackbarState = snackbarState,
                                            context = context,
                                            refreshList = {
                                                libraryViewModel.onEvent(
                                                    LibraryEvent.OnLoadList
                                                )
                                            }
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = !state.isLoading
                        && state.history.isEmpty()
                        && !state.isRefreshing,
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
                state.isRefreshing,
                refreshState,
                Modifier.align(Alignment.TopCenter),
                backgroundColor = MaterialTheme.colorScheme.inverseSurface,
                contentColor = MaterialTheme.colorScheme.inverseOnSurface
            )
        }
    }

    BackHandler {
        if (state.showSearch) {
            viewModel.onEvent(HistoryEvent.OnSearchShowHide)
            return@BackHandler
        }

        navigator.navigate(Screen.LIBRARY, false)
    }
}












