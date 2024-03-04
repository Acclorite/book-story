package ua.acclorite.book_story.presentation.screens.browse

import android.Manifest
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.Navigator
import ua.acclorite.book_story.presentation.Screen
import ua.acclorite.book_story.presentation.components.AnimatedTopAppBar
import ua.acclorite.book_story.presentation.components.IsEmpty
import ua.acclorite.book_story.presentation.components.IsError
import ua.acclorite.book_story.presentation.components.MoreDropDown
import ua.acclorite.book_story.presentation.screens.browse.components.BrowseAddingDialog
import ua.acclorite.book_story.presentation.screens.browse.components.BrowseFileItem
import ua.acclorite.book_story.presentation.screens.browse.components.BrowseStoragePermissionDialog
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseEvent
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseViewModel
import ua.acclorite.book_story.ui.DefaultTransition
import ua.acclorite.book_story.ui.Transitions
import ua.acclorite.book_story.ui.elevation

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalPermissionsApi::class,
    ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class, FlowPreview::class
)
@Composable
fun BrowseScreen(
    viewModel: BrowseViewModel = hiltViewModel(),
    navigator: Navigator
) {
    val permissionState = rememberPermissionState(
        permission = Manifest.permission.READ_EXTERNAL_STORAGE
    )
    val state by viewModel.state.collectAsState()
    val refreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing,
        onRefresh = {
            viewModel.onEvent(BrowseEvent.OnRefreshList)
        }
    )
    val focusRequester = remember { FocusRequester() }
    val listState = rememberLazyGridState(state.scrollIndex, state.scrollOffset)

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex
        }
            .debounce(10L)
            .collectLatest {
                viewModel.onEvent(BrowseEvent.OnUpdateScrollIndex(it))
            }
    }
    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemScrollOffset
        }
            .debounce(10L)
            .collectLatest {
                viewModel.onEvent(
                    BrowseEvent.OnUpdateScrollOffset(it)
                )
            }
    }
    LaunchedEffect(Unit) {
        viewModel.onEvent(BrowseEvent.OnPermissionCheck(permissionState))
    }

    if (state.requestPermissionDialog) {
        BrowseStoragePermissionDialog(viewModel, permissionState)
    }
    if (state.showAddingDialog) {
        BrowseAddingDialog(viewModel = viewModel, navigator = navigator)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(refreshState),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            AnimatedTopAppBar(
                containerColor = MaterialTheme.colorScheme.surface,
                scrolledContainerColor = MaterialTheme.elevation(),

                scrollBehavior = null,
                isTopBarScrolled = state.scrollIndex > 0 || state.scrollOffset > 0 || state.hasSelectedItems,

                content1Visibility = !state.hasSelectedItems && !state.showSearch,
                content1NavigationIcon = {},
                content1Title = {
                    Text(
                        stringResource(id = R.string.browse_screen),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                },
                content1Actions = {
                    IconButton(onClick = { viewModel.onEvent(BrowseEvent.OnSearchShowHide) }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search files",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    MoreDropDown(navigator = navigator)
                },

                content2Visibility = state.hasSelectedItems,
                content2NavigationIcon = {
                    IconButton(onClick = { viewModel.onEvent(BrowseEvent.OnClearSelectedFiles) }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear selected items",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                content2Title = {
                    Text(
                        stringResource(
                            id = R.string.selected_items_count_query,
                            state.selectedItemsCount.coerceAtLeast(1)
                        ),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                },
                content2Actions = {
                    IconButton(onClick = { viewModel.onEvent(BrowseEvent.OnAddingDialogRequest) }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Add files to library",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },

                content3Visibility = state.showSearch && !state.hasSelectedItems,
                content3NavigationIcon = {
                    IconButton(onClick = { viewModel.onEvent(BrowseEvent.OnSearchShowHide) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Exit search mode",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                content3Title = {
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
                                viewModel.onEvent(BrowseEvent.OnRequestFocus(focusRequester))
                            },
                        onValueChange = {
                            viewModel.onEvent(BrowseEvent.OnSearchQueryChange(it))
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
                                        stringResource(id = R.string.files)
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
                content3Actions = {
                    MoreDropDown(navigator = navigator)
                }
            )
        }
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding())
        ) {
            LazyVerticalGrid(
                state = listState,
                modifier = Modifier
                    .fillMaxSize(),
                columns = GridCells.Adaptive(170.dp),
                contentPadding = PaddingValues(12.dp)
            ) {
                items(state.selectableFiles, key = { it.first.path }) { selectableFile ->
                    BrowseFileItem(
                        file = selectableFile,
                        modifier = Modifier
                            .animateItemPlacement(
                                animationSpec = tween(300)
                            ),
                        onClick = {
                            viewModel.onEvent(BrowseEvent.OnSelectFile(selectableFile))
                        }
                    )
                }
            }

            if (state.isLoading && !state.isRefreshing && state.selectableFiles.isEmpty()) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeCap = StrokeCap.Round,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(36.dp)
                )
            }

            DefaultTransition(visible = state.showErrorMessage, Modifier.align(Alignment.Center)) {
                IsError(
                    modifier = Modifier.align(Alignment.Center),
                    errorMessage = stringResource(id = R.string.error_permission),
                    icon = painterResource(id = R.drawable.error),
                    actionTitle = stringResource(id = R.string.grant_permission)
                ) {
                    viewModel.onEvent(BrowseEvent.OnPermissionCheck(permissionState))
                }
            }

            AnimatedVisibility(
                visible = !state.isLoading && state.selectableFiles.isEmpty()
                        && !state.showErrorMessage && !state.requestPermissionDialog
                        && !state.isRefreshing,
                modifier = Modifier.align(Alignment.Center),
                enter = Transitions.DefaultTransitionIn,
                exit = fadeOut(tween(0))
            ) {
                IsEmpty(
                    message = stringResource(id = R.string.browse_empty),
                    icon = painterResource(id = R.drawable.empty_browse),
                    actionTitle = stringResource(id = R.string.get_help),
                    action = { navigator.navigate(Screen.HELP) }
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
        if (state.hasSelectedItems) {
            viewModel.onEvent(BrowseEvent.OnClearSelectedFiles)
            return@BackHandler
        }

        if (state.showSearch) {
            viewModel.onEvent(BrowseEvent.OnSearchShowHide)
            return@BackHandler
        }

        navigator.navigate(Screen.LIBRARY)
    }
}









