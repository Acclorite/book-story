/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.library

import android.os.Parcelable
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import ua.acclorite.book_story.presentation.book_info.BookInfoScreen
import ua.acclorite.book_story.presentation.browse.BrowseScreen
import ua.acclorite.book_story.presentation.history.HistoryScreen
import ua.acclorite.book_story.presentation.main.MainModel
import ua.acclorite.book_story.presentation.navigator.Screen
import ua.acclorite.book_story.presentation.reader.ReaderScreen
import ua.acclorite.book_story.presentation.settings.LibrarySettingsScreen
import ua.acclorite.book_story.presentation.settings.SettingsModel
import ua.acclorite.book_story.ui.library.LibraryContent
import ua.acclorite.book_story.ui.navigator.LocalNavigator

@Parcelize
object LibraryScreen : Screen, Parcelable {

    @IgnoredOnParcel
    const val MOVE_DIALOG = "move_dialog"

    @IgnoredOnParcel
    const val DELETE_DIALOG = "delete_dialog"

    @IgnoredOnParcel
    const val FILTER_BOTTOM_SHEET = "filter_bottom_sheet"

    @IgnoredOnParcel
    val refreshListChannel: Channel<Long> = Channel(Channel.CONFLATED)

    @IgnoredOnParcel
    val scrollToPageCompositionChannel: Channel<Int> = Channel(Channel.CONFLATED)

    @IgnoredOnParcel
    private var initialPage = 0

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        val screenModel = hiltViewModel<LibraryModel>()
        val mainModel = hiltViewModel<MainModel>()
        val settingsModel = hiltViewModel<SettingsModel>()

        val state = screenModel.state.collectAsStateWithLifecycle()
        val mainState = mainModel.state.collectAsStateWithLifecycle()
        val settingsState = settingsModel.state.collectAsStateWithLifecycle()

        val showDefaultCategory = remember(state.value.books, settingsState.value.categories) {
            derivedStateOf {
                val categoryIds = settingsState.value.categories.map { it.id }.toSet()
                state.value.books.any { book ->
                    book.data.categories.none { category -> category in categoryIds }
                } || settingsState.value.categories.isEmpty()
                        || mainState.value.libraryAlwaysShowDefaultTab
            }
        }

        val focusRequester = remember { FocusRequester() }
        val refreshState = rememberPullRefreshState(
            refreshing = state.value.isRefreshing,
            onRefresh = {
                screenModel.onEvent(
                    LibraryEvent.OnRefreshList(
                        loading = false,
                        hideSearch = true
                    )
                )
            }
        )

        val pagerState = rememberPagerState(
            initialPage = initialPage
        ) {
            settingsState.value.categories.count().plus(if (showDefaultCategory.value) 1 else 0)
        }
        DisposableEffect(Unit) { onDispose { initialPage = pagerState.currentPage } }

        LaunchedEffect(Unit) {
            scrollToPageCompositionChannel.receiveAsFlow().collectLatest {
                pagerState.scrollToPage(it)
            }
        }

        LibraryContent(
            books = state.value.books,
            selectedItemsCount = state.value.selectedItemsCount,
            hasSelectedItems = state.value.hasSelectedItems,
            titlePosition = mainState.value.libraryTitlePosition,
            readButton = mainState.value.libraryReadButton,
            showProgress = mainState.value.libraryShowProgress,
            showCategoryTabs = mainState.value.libraryShowCategoryTabs,
            showBookCount = mainState.value.libraryShowBookCount,
            showSearch = state.value.showSearch,
            searchQuery = state.value.searchQuery,
            bookCount = state.value.books.count(),
            focusRequester = focusRequester,
            pagerState = pagerState,
            isLoading = state.value.isLoading,
            isRefreshing = state.value.isRefreshing,
            doublePressExit = mainState.value.doublePressExit,
            layout = mainState.value.libraryLayout,
            gridSize = mainState.value.libraryGridSize,
            autoGridSize = mainState.value.libraryAutoGridSize,
            categories = settingsState.value.categories,
            showDefaultCategory = showDefaultCategory.value,
            categoriesSort = settingsState.value.categoriesSort,
            sortOrder = mainState.value.librarySortOrder,
            sortOrderDescending = mainState.value.librarySortOrderDescending,
            perCategorySort = mainState.value.libraryPerCategorySort,
            refreshState = refreshState,
            dialog = state.value.dialog,
            bottomSheet = state.value.bottomSheet,
            updateCategorySort = settingsModel::onEvent,
            changeLibrarySortOrder = mainModel::onEvent,
            changeLibrarySortOrderDescending = mainModel::onEvent,
            selectBook = screenModel::onEvent,
            searchVisibility = screenModel::onEvent,
            requestFocus = screenModel::onEvent,
            searchQueryChange = screenModel::onEvent,
            search = screenModel::onEvent,
            clearSelectedBooks = screenModel::onEvent,
            showMoveDialog = screenModel::onEvent,
            actionMoveDialog = screenModel::onEvent,
            actionDeleteDialog = screenModel::onEvent,
            showDeleteDialog = screenModel::onEvent,
            showFilterBottomSheet = screenModel::onEvent,
            dismissBottomSheet = screenModel::onEvent,
            dismissDialog = screenModel::onEvent,
            navigateToBrowse = {
                navigator.push(BrowseScreen)
            },
            navigateToReader = {
                HistoryScreen.insertHistoryChannel.trySend(it)
                navigator.push(ReaderScreen(it))
            },
            navigateToBookInfo = {
                navigator.push(BookInfoScreen(bookId = it))
            },
            navigateToLibrarySettings = {
                navigator.push(LibrarySettingsScreen)
            }
        )
    }
}