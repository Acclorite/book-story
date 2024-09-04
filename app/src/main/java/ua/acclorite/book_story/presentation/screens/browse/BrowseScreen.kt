package ua.acclorite.book_story.presentation.screens.browse

import android.Manifest
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.SelectableFile
import ua.acclorite.book_story.presentation.core.components.LocalBrowseViewModel
import ua.acclorite.book_story.presentation.core.components.LocalMainViewModel
import ua.acclorite.book_story.presentation.core.navigation.LocalOnNavigate
import ua.acclorite.book_story.presentation.core.navigation.Screen
import ua.acclorite.book_story.presentation.core.util.showToast
import ua.acclorite.book_story.presentation.screens.browse.components.BrowseEmptyPlaceholder
import ua.acclorite.book_story.presentation.screens.browse.components.BrowseStoragePermissionDialog
import ua.acclorite.book_story.presentation.screens.browse.components.adding_dialog.BrowseAddingDialog
import ua.acclorite.book_story.presentation.screens.browse.components.filter_bottom_sheet.BrowseFilterBottomSheet
import ua.acclorite.book_story.presentation.screens.browse.components.layout.BrowseLayout
import ua.acclorite.book_story.presentation.screens.browse.components.top_bar.BrowseTopBar
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseEvent
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.data.BrowseFilesStructure
import ua.acclorite.book_story.presentation.ui.DefaultTransition

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BrowseScreenRoot() {
    val state = LocalBrowseViewModel.current.state
    val mainState = LocalMainViewModel.current.state
    val onEvent = LocalBrowseViewModel.current.onEvent
    val viewModel = LocalBrowseViewModel.current.viewModel

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
        onEvent(BrowseEvent.OnUpdateScrollOffset)
        onEvent(BrowseEvent.OnPermissionCheck(permissionState))
    }


    BrowseScreen(
        permissionState = permissionState,
        filteredFiles = filteredFiles.value,
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
    permissionState: PermissionState,
    filteredFiles: List<SelectableFile>
) {
    val state = LocalBrowseViewModel.current.state
    val mainState = LocalMainViewModel.current.state
    val onEvent = LocalBrowseViewModel.current.onEvent
    val onNavigate = LocalOnNavigate.current
    val context = LocalContext.current

    val refreshState = rememberPullRefreshState(
        refreshing = state.value.isRefreshing,
        onRefresh = {
            onEvent(BrowseEvent.OnRefreshList)
        }
    )

    if (state.value.requestPermissionDialog) {
        BrowseStoragePermissionDialog(permissionState)
    }
    if (state.value.showAddingDialog) {
        BrowseAddingDialog()
    }
    if (state.value.showFilterBottomSheet) {
        BrowseFilterBottomSheet()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(refreshState),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            BrowseTopBar(filteredFiles = filteredFiles)
        }
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding())
        ) {
            DefaultTransition(visible = !state.value.isLoading) {
                BrowseLayout(
                    filteredFiles = filteredFiles,
                    onLongItemClick = { selectableFile ->
                        when (selectableFile.isDirectory) {
                            false -> {
                                context.getString(
                                    R.string.file_path_query,
                                    selectableFile.fileOrDirectory.path
                                ).showToast(context = context)
                            }

                            true -> {
                                onEvent(
                                    BrowseEvent.OnSelectFile(
                                        includedFileFormats = mainState.value.browseIncludedFilterItems,
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
                                        includedFileFormats = mainState.value.browseIncludedFilterItems,
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
                                            includedFileFormats = mainState.value.browseIncludedFilterItems,
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
                isFilesEmpty = filteredFiles.isEmpty(),
                storagePermissionState = permissionState,
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
            onEvent(BrowseEvent.OnGoBackDirectory)
            return@BackHandler
        }

        onNavigate {
            navigate(Screen.Library)
        }
    }
}