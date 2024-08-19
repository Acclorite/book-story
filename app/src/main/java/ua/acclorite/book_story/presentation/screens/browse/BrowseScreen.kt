package ua.acclorite.book_story.presentation.screens.browse

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.SelectableFile
import ua.acclorite.book_story.domain.util.OnNavigate
import ua.acclorite.book_story.presentation.data.LocalNavigator
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainState
import ua.acclorite.book_story.presentation.data.MainViewModel
import ua.acclorite.book_story.presentation.data.Screen
import ua.acclorite.book_story.presentation.screens.browse.components.BrowseEmptyPlaceholder
import ua.acclorite.book_story.presentation.screens.browse.components.BrowseStoragePermissionDialog
import ua.acclorite.book_story.presentation.screens.browse.components.adding_dialog.BrowseAddingDialog
import ua.acclorite.book_story.presentation.screens.browse.components.filter_bottom_sheet.BrowseFilterBottomSheet
import ua.acclorite.book_story.presentation.screens.browse.components.layout.BrowseLayout
import ua.acclorite.book_story.presentation.screens.browse.components.top_bar.BrowseTopBar
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseEvent
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseState
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseViewModel
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryViewModel
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.data.BrowseFilesStructure
import ua.acclorite.book_story.presentation.ui.DefaultTransition

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BrowseScreenRoot() {
    val navigator = LocalNavigator.current
    val viewModel: BrowseViewModel = hiltViewModel()
    val libraryViewModel: LibraryViewModel = hiltViewModel()
    val mainViewModel: MainViewModel = hiltViewModel()

    val state = viewModel.state.collectAsState()
    val mainState = mainViewModel.state.collectAsState()

    val permissionState = rememberPermissionState(
        permission = Manifest.permission.READ_EXTERNAL_STORAGE
    )
    val filteredFiles = remember(
        state.value.selectableFiles,
        state.value.selectedDirectory
    ) {
        derivedStateOf {
            viewModel.filterList(mainState.value)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onEvent(BrowseEvent.OnUpdateScrollOffset)
        viewModel.onEvent(
            BrowseEvent.OnPermissionCheck(permissionState)
        )
    }


    BrowseScreen(
        state = state,
        mainState = mainState,
        permissionState = permissionState,
        filteredFiles = filteredFiles.value,
        onNavigate = { navigator.it() },
        onEvent = viewModel::onEvent,
        onMainEvent = mainViewModel::onEvent,
        onLibraryEvent = libraryViewModel::onEvent
    )

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearViewModel()
        }
    }
}

@OptIn(
    ExperimentalPermissionsApi::class,
    ExperimentalMaterialApi::class
)
@Composable
private fun BrowseScreen(
    state: State<BrowseState>,
    mainState: State<MainState>,
    permissionState: PermissionState,
    filteredFiles: List<SelectableFile>,
    onNavigate: OnNavigate,
    onEvent: (BrowseEvent) -> Unit,
    onMainEvent: (MainEvent) -> Unit,
    onLibraryEvent: (LibraryEvent) -> Unit
) {
    val context = LocalContext.current
    val refreshState = rememberPullRefreshState(
        refreshing = state.value.isRefreshing,
        onRefresh = {
            onEvent(BrowseEvent.OnRefreshList)
        }
    )

    if (state.value.requestPermissionDialog) {
        BrowseStoragePermissionDialog(onEvent, permissionState)
    }
    if (state.value.showAddingDialog) {
        BrowseAddingDialog(
            state = state,
            onEvent = onEvent,
            onLibraryEvent = onLibraryEvent
        )
    }
    if (state.value.showFilterBottomSheet) {
        BrowseFilterBottomSheet(
            state = state,
            mainState = mainState,
            onMainEvent = onMainEvent,
            onEvent = onEvent
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(refreshState),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            BrowseTopBar(
                state = state,
                mainState = mainState,
                filteredFiles = filteredFiles,
                onEvent = onEvent
            )
        }
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding())
        ) {
            DefaultTransition(visible = !state.value.isLoading) {
                BrowseLayout(
                    state = state,
                    mainState = mainState,
                    filteredFiles = filteredFiles,
                    onLongItemClick = { selectableFile ->
                        when (selectableFile.isDirectory) {
                            false -> {
                                Toast.makeText(
                                    context,
                                    context.getString(
                                        R.string.file_path_query,
                                        selectableFile.fileOrDirectory.path
                                    ),
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            true -> {
                                onEvent(
                                    BrowseEvent.OnSelectFile(
                                        includedFileFormats = mainState.value.browseIncludedFilterItems!!,
                                        file = selectableFile
                                    )
                                )
                            }
                        }
                    },
                    onFavoriteItemClick = {
                        onEvent(
                            BrowseEvent.OnUpdateFavoriteDirectory(
                                it.fileOrDirectory.path
                            )
                        )
                    },
                    onItemClick = { selectableFile ->
                        when (selectableFile.isDirectory) {
                            false -> {
                                onEvent(
                                    BrowseEvent.OnSelectFile(
                                        includedFileFormats = mainState.value.browseIncludedFilterItems!!,
                                        file = selectableFile
                                    )
                                )
                            }

                            true -> {
                                if (!state.value.hasSelectedItems) {
                                    onEvent(
                                        BrowseEvent.OnChangeDirectory(
                                            selectableFile.fileOrDirectory,
                                            savePreviousDirectory = true
                                        )
                                    )
                                } else {
                                    onEvent(
                                        BrowseEvent.OnSelectFile(
                                            includedFileFormats = mainState.value.browseIncludedFilterItems!!,
                                            file = selectableFile
                                        )
                                    )
                                }
                            }
                        }
                    }
                )
            }

            BrowseEmptyPlaceholder(
                state = state,
                isFilesEmpty = filteredFiles.isEmpty(),
                storagePermissionState = permissionState,
                onNavigate = onNavigate,
                onEvent = onEvent
            )

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

        if (
            state.value.inNestedDirectory
            && mainState.value.browseFilesStructure != BrowseFilesStructure.ALL_FILES
        ) {
            onEvent(
                BrowseEvent.OnGoBackDirectory
            )
            return@BackHandler
        }

        onNavigate {
            navigate(Screen.Library)
        }
    }
}