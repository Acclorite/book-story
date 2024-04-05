package ua.acclorite.book_story.presentation.screens.browse

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.AnimatedTopAppBar
import ua.acclorite.book_story.presentation.components.CustomIconButton
import ua.acclorite.book_story.presentation.components.MoreDropDown
import ua.acclorite.book_story.presentation.components.is_messages.IsEmpty
import ua.acclorite.book_story.presentation.components.is_messages.IsError
import ua.acclorite.book_story.presentation.data.Navigator
import ua.acclorite.book_story.presentation.data.Screen
import ua.acclorite.book_story.presentation.screens.browse.components.BrowseFileItem
import ua.acclorite.book_story.presentation.screens.browse.components.BrowseStoragePermissionDialog
import ua.acclorite.book_story.presentation.screens.browse.components.adding_dialog.BrowseAddingDialog
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseEvent
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseViewModel
import ua.acclorite.book_story.presentation.screens.library.data.LibraryViewModel
import ua.acclorite.book_story.presentation.ui.DefaultTransition
import ua.acclorite.book_story.presentation.ui.Transitions

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalPermissionsApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun BrowseScreen(
    viewModel: BrowseViewModel,
    libraryViewModel: LibraryViewModel,
    navigator: Navigator
) {
    val context = LocalContext.current
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
    var showErrorMessage by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.onEvent(BrowseEvent.OnUpdateScrollOffset)
        viewModel.onEvent(
            BrowseEvent.OnPermissionCheck(
                permissionState,
                hideErrorMessage = { showErrorMessage = false }
            )
        )
    }

    if (state.requestPermissionDialog) {
        BrowseStoragePermissionDialog(viewModel, permissionState) {
            showErrorMessage = it
        }
    }
    if (state.showAddingDialog) {
        BrowseAddingDialog(
            viewModel = viewModel,
            navigator = navigator,
            libraryViewModel = libraryViewModel
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
                isTopBarScrolled = state.hasSelectedItems || state.listState.canScrollBackward,

                content1Visibility = !state.hasSelectedItems && !state.showSearch,
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
                        viewModel.onEvent(BrowseEvent.OnSearchShowHide)
                    }
                    MoreDropDown(navigator = navigator)
                },

                content2Visibility = state.hasSelectedItems,
                content2NavigationIcon = {
                    CustomIconButton(
                        icon = Icons.Default.Clear,
                        contentDescription = R.string.clear_selected_items_content_desc,
                        disableOnClick = true
                    ) {
                        viewModel.onEvent(BrowseEvent.OnClearSelectedFiles)
                    }
                },
                content2Title = {
                    Text(
                        stringResource(
                            id = R.string.selected_items_count_query,
                            state.selectedItemsCount.coerceAtLeast(1)
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
                        enabled = !state.showAddingDialog
                    ) {
                        viewModel.onEvent(BrowseEvent.OnAddingDialogRequest)
                    }
                },

                content3Visibility = state.showSearch && !state.hasSelectedItems,
                content3NavigationIcon = {
                    CustomIconButton(
                        icon = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = R.string.exit_search_content_desc,
                        disableOnClick = true
                    ) {
                        viewModel.onEvent(BrowseEvent.OnSearchShowHide)
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
            DefaultTransition(visible = !state.isLoading) {
                LazyColumn(
                    state = state.listState,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(
                        state.selectableFiles,
                        key = { it.first.path }
                    ) { selectableFile ->
                        BrowseFileItem(
                            file = selectableFile,
                            modifier = Modifier.animateItem(),
                            hasSelectedFiles = state.selectableFiles.any { it.second },
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
                                viewModel.onEvent(BrowseEvent.OnSelectFile(selectableFile))
                            }
                        )
                    }
                }
            }

            AnimatedVisibility(
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
                    viewModel.onEvent(
                        BrowseEvent.OnPermissionCheck(
                            permissionState,
                            hideErrorMessage = { showErrorMessage = false }
                        )
                    )
                }
            }

            AnimatedVisibility(
                visible = !state.isLoading && state.selectableFiles.isEmpty()
                        && !showErrorMessage && !state.requestPermissionDialog
                        && !state.isRefreshing,
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
                state.isRefreshing,
                refreshState,
                Modifier.align(Alignment.TopCenter),
                backgroundColor = MaterialTheme.colorScheme.inverseSurface,
                contentColor = MaterialTheme.colorScheme.inverseOnSurface
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

        navigator.navigate(Screen.LIBRARY, false)
    }
}









