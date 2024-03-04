@file:OptIn(ExperimentalFoundationApi::class)

package ua.acclorite.book_story.presentation.screens.reader.data

import androidx.activity.ComponentActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.PagerState
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.presentation.Navigator

sealed class ReaderEvent {
    data class OnFileNotFound(val onLoaded: () -> Unit) : ReaderEvent()
    data class OnLoadText(
        val scrollState: LazyListState,
        val navigator: Navigator,
        val refreshList: (Book) -> Unit,
        val onLoaded: () -> Unit
    ) : ReaderEvent()

    data class OnShowHideMenu(val show: Boolean? = null, val context: ComponentActivity) :
        ReaderEvent()

    data class OnShowSystemBars(val context: ComponentActivity) : ReaderEvent()
    data class OnChangeProgress(
        val progress: Float,
        val navigator: Navigator,
        val refreshList: (Book) -> Unit
    ) : ReaderEvent()

    data class OnScroll(val scrollState: LazyListState, val progress: Float) : ReaderEvent()
    data object OnShowHideSettingsBottomSheet : ReaderEvent()
    data class OnScrollToSettingsPage(val page: Int, val pagerState: PagerState) : ReaderEvent()
    data class OnMoveBookToAlreadyRead(
        val context: ComponentActivity,
        val refreshList: () -> Unit,
        val updatePage: (Int) -> Unit,
        val navigator: Navigator
    ) : ReaderEvent()
}