package ua.acclorite.book_story.presentation.screens.browse

import android.Manifest
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.AnimatedTopAppBar
import ua.acclorite.book_story.presentation.components.CustomAnimatedVisibility
import ua.acclorite.book_story.presentation.components.CustomIconButton
import ua.acclorite.book_story.presentation.components.CustomSearchTextField
import ua.acclorite.book_story.presentation.components.MoreDropDown
import ua.acclorite.book_story.presentation.components.customItems
import ua.acclorite.book_story.presentation.components.is_messages.IsEmpty
import ua.acclorite.book_story.presentation.components.is_messages.IsError
import ua.acclorite.book_story.presentation.data.LocalNavigator
import ua.acclorite.book_story.presentation.data.Navigator
import ua.acclorite.book_story.presentation.data.Screen
import ua.acclorite.book_story.presentation.screens.browse.components.BrowseFileItem
import ua.acclorite.book_story.presentation.screens.browse.components.BrowseStoragePermissionDialog
import ua.acclorite.book_story.presentation.screens.browse.components.adding_dialog.BrowseAddingDialog
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseEvent
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseState
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseViewModel
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryViewModel
import ua.acclorite.book_story.presentation.ui.DefaultTransition
import ua.acclorite.book_story.presentation.ui.Transitions

@Composable
fun BrowseScreenRoot() {
    val navigator = LocalNavigator.current
    val viewModel: BrowseViewModel = hiltViewModel()
    val libraryViewModel: LibraryViewModel = hiltViewModel()

    val state = viewModel.state.collectAsState()

    BrowseScreen(
        state = state,
        navigator = navigator,
        onEvent = viewModel::onEvent,
        onLibraryEvent = libraryViewModel::onEvent
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalPermissionsApi::class,
    ExperimentalMaterialApi::class
)
@Composable
private fun BrowseScreen(
    state: State<BrowseState>,
    navigator: Navigator,
    onEvent: (BrowseEvent) -> Unit,
    onLibraryEvent: (LibraryEvent) -> Unit
) {
    val context = LocalContext.current
    val permissionState = rememberPermissionState(
        permission = Manifest.permission.READ_EXTERNAL_STORAGE
    )
    val refreshState = rememberPullRefreshState(
        refreshing = state.value.isRefreshing,
        onRefresh = {
            onEvent(BrowseEvent.OnRefreshList)
        }
    )
    val focusRequester = remember { FocusRequester() }
    var showErrorMessage by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        onEvent(BrowseEvent.OnUpdateScrollOffset)
        onEvent(
            BrowseEvent.OnPermissionCheck(
                permissionState,
                hideErrorMessage = { showErrorMessage = false }
            )
        )
    }

    if (state.value.requestPermissionDialog) {
        BrowseStoragePermissionDialog(onEvent, permissionState) {
            showErrorMessage = it
        }
    }
    if (state.value.showAddingDialog) {
        BrowseAddingDialog(
            state = state,
            onEvent = onEvent,
            onLibraryEvent = onLibraryEvent
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(refreshState),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            AnimatedTopAppBar(
                scrollBehavior = null,
                isTopBarScrolled = state.value.hasSelectedItems || state.value.listState.canScrollBackward,

                content1Visibility = !state.value.hasSelectedItems && !state.value.showSearch,
                content1NavigationIcon = {},
                content1Title = {
                    Text(stringResource(id = R.string.browse_screen))
                },
                content1Actions = {
                    CustomIconButton(
                        icon = Icons.Default.Search,
                        contentDescription = R.string.search_content_desc,
                        disableOnClick = true
                    ) {
                        onEvent(BrowseEvent.OnSearchShowHide)
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
                        onEvent(BrowseEvent.OnClearSelectedFiles)
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
                        icon = Icons.Default.Check,
                        contentDescription = R.string.add_files_content_desc,
                        disableOnClick = false,
                        enabled = !state.value.showAddingDialog
                    ) {
                        onEvent(BrowseEvent.OnAddingDialogRequest)
                    }
                },

                content3Visibility = state.value.showSearch && !state.value.hasSelectedItems,
                content3NavigationIcon = {
                    CustomIconButton(
                        icon = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = R.string.exit_search_content_desc,
                        disableOnClick = true
                    ) {
                        onEvent(BrowseEvent.OnSearchShowHide)
                    }
                },
                content3Title = {
                    CustomSearchTextField(
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .onGloballyPositioned {
                                onEvent(BrowseEvent.OnRequestFocus(focusRequester))
                            },
                        query = state.value.searchQuery,
                        onQueryChange = {
                            onEvent(BrowseEvent.OnSearchQueryChange(it))
                        },
                        onSearch = {
                            onEvent(BrowseEvent.OnSearch)
                        },
                        placeholder = stringResource(
                            id = R.string.search_query,
                            stringResource(id = R.string.files)
                        )
                    )
                },
                content3Actions = {
                    MoreDropDown()
                }
            )
        }
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding())
        ) {
            DefaultTransition(visible = !state.value.isLoading) {
                LazyColumn(
                    state = state.value.listState,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    customItems(
                        state.value.selectableFiles,
                        key = { it.first.path }
                    ) { selectableFile ->
                        BrowseFileItem(
                            file = selectableFile,
                            modifier = Modifier.animateItem(),
                            hasSelectedFiles = state.value.selectableFiles.any { it.second },
                            onLongClick = {
                                Toast.makeText(
                                    context,
                                    context.getString(
                                        R.string.file_path_query,
                                        selectableFile.first.path
                                    ),
                                    Toast.LENGTH_LONG
                                ).show()
                            },
                            onClick = {
                                onEvent(BrowseEvent.OnSelectFile(selectableFile))
                            }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            CustomAnimatedVisibility(
                visible = showErrorMessage,
                modifier = Modifier.align(Alignment.Center),
                enter = Transitions.DefaultTransitionIn,
                exit = fadeOut(tween(0))
            ) {
                IsError(
                    modifier = Modifier.align(Alignment.Center),
                    errorMessage = stringResource(id = R.string.error_permission),
                    icon = painterResource(id = R.drawable.error),
                    actionTitle = stringResource(id = R.string.grant_permission)
                ) {
                    onEvent(
                        BrowseEvent.OnPermissionCheck(
                            permissionState,
                            hideErrorMessage = { showErrorMessage = false }
                        )
                    )
                }
            }

            CustomAnimatedVisibility(
                visible = !state.value.isLoading && state.value.selectableFiles.isEmpty()
                        && !showErrorMessage && !state.value.requestPermissionDialog
                        && !state.value.isRefreshing,
                modifier = Modifier.align(Alignment.Center),
                enter = Transitions.DefaultTransitionIn,
                exit = fadeOut(tween(0))
            ) {
                IsEmpty(
                    message = stringResource(id = R.string.browse_empty),
                    icon = painterResource(id = R.drawable.empty_browse),
                    actionTitle = stringResource(id = R.string.get_help),
                    action = { navigator.navigate(Screen.HELP, false) }
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
        if (state.value.hasSelectedItems) {
            onEvent(BrowseEvent.OnClearSelectedFiles)
            return@BackHandler
        }

        if (state.value.showSearch) {
            onEvent(BrowseEvent.OnSearchShowHide)
            return@BackHandler
        }

        navigator.navigate(Screen.LIBRARY, false)
    }
}









