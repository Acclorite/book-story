/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.browse

import android.os.Parcelable
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import ua.acclorite.book_story.presentation.browse.model.BrowseLayout
import ua.acclorite.book_story.presentation.navigator.Screen
import ua.acclorite.book_story.ui.browse.BrowseContent
import ua.acclorite.book_story.ui.browse.BrowseEffects
import ua.acclorite.book_story.ui.common.helpers.LocalSettings

@Parcelize
object BrowseScreen : Screen, Parcelable {

    @IgnoredOnParcel
    const val ADD_DIALOG = "add_dialog"

    @IgnoredOnParcel
    const val FILTER_BOTTOM_SHEET = "filter_bottom_sheet"

    @IgnoredOnParcel
    val refreshListChannel: Channel<Unit> = Channel(Channel.CONFLATED)

    @IgnoredOnParcel
    val resetScrollPositionCompositionChannel: Channel<Unit> = Channel(Channel.CONFLATED)

    @IgnoredOnParcel
    private var initialListIndex = 0

    @IgnoredOnParcel
    private var initialListOffset = 0

    @IgnoredOnParcel
    private var initialGridIndex = 0

    @IgnoredOnParcel
    private var initialGridOffset = 0

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val screenModel = hiltViewModel<BrowseModel>()
        val settings = LocalSettings.current

        val state = screenModel.state.collectAsStateWithLifecycle()

        val listState = rememberLazyListState(initialListIndex, initialListOffset)
        val gridState = rememberLazyGridState(initialGridIndex, initialGridOffset)

        val focusRequester = remember { FocusRequester() }
        val refreshState = rememberPullRefreshState(
            refreshing = state.value.isRefreshing,
            onRefresh = {
                screenModel.onEvent(
                    BrowseEvent.OnRefreshList(
                        loading = false,
                        hideSearch = true
                    )
                )
            }
        )

        LaunchedEffect(
            settings.browseIncludedFilterItems.value,
            settings.browseSortOrderDescending.value,
            settings.browseSortOrder.value
        ) {
            screenModel.onEvent(
                BrowseEvent.OnRefreshList(
                    loading = false,
                    hideSearch = false
                )
            )
        }

        LaunchedEffect(Unit) {
            resetScrollPositionCompositionChannel.receiveAsFlow().collectLatest {
                initialListIndex = 0
                initialListOffset = 0
                initialGridIndex = 0
                initialGridOffset = 0
            }
        }

        DisposableEffect(Unit) {
            onDispose {
                initialListIndex = 0
                initialListOffset = 0
                initialGridIndex = 0
                initialGridOffset = 0

                when (settings.browseLayout.lastValue) {
                    BrowseLayout.LIST -> {
                        initialListIndex = listState.firstVisibleItemIndex
                        initialListOffset = listState.firstVisibleItemScrollOffset
                    }

                    BrowseLayout.GRID -> {
                        initialGridIndex = gridState.firstVisibleItemIndex
                        initialGridOffset = gridState.firstVisibleItemScrollOffset
                    }
                }
            }
        }

        BrowseEffects(
            effects = screenModel.effects,
            focusRequester = focusRequester
        )

        BrowseContent(
            files = state.value.files,
            selectedBooksAddDialog = state.value.selectedBooksAddDialog,
            refreshState = refreshState,
            loadingAddDialog = state.value.loadingAddDialog,
            dialog = state.value.dialog,
            bottomSheet = state.value.bottomSheet,
            listState = listState,
            gridState = gridState,
            layout = settings.browseLayout.value,
            gridSize = settings.browseGridSize.value,
            autoGridSize = settings.browseAutoGridSize.value,
            includedFilterItems = settings.browseIncludedFilterItems.value,
            pinnedPaths = settings.browsePinnedPaths.value,
            canScrollBackList = listState.canScrollBackward,
            canScrollBackGrid = gridState.canScrollBackward,
            hasSelectedItems = state.value.hasSelectedItems,
            selectedItemsCount = state.value.selectedItemsCount,
            isRefreshing = state.value.isRefreshing,
            isLoading = state.value.isLoading,
            dialogHidden = state.value.dialog == null,
            filesEmpty = state.value.files.isEmpty(),
            showSearch = state.value.showSearch,
            searchQuery = state.value.searchQuery,
            focusRequester = focusRequester,
            searchVisibility = screenModel::onEvent,
            searchQueryChange = screenModel::onEvent,
            search = screenModel::onEvent,
            requestFocus = screenModel::onEvent,
            clearSelectedFiles = screenModel::onEvent,
            selectFiles = screenModel::onEvent,
            showFilterBottomSheet = screenModel::onEvent,
            dismissBottomSheet = screenModel::onEvent,
            showAddDialog = screenModel::onEvent,
            dismissAddDialog = screenModel::onEvent,
            selectAddDialog = screenModel::onEvent,
            actionAddDialog = screenModel::onEvent,
            updatePinnedPaths = screenModel::onEvent,
            navigateToLibrary = screenModel::onEvent,
            navigateToBrowseSettings = screenModel::onEvent
        )
    }
}