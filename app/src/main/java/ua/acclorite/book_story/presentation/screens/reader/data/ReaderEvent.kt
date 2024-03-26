package ua.acclorite.book_story.presentation.screens.reader.data

import androidx.activity.ComponentActivity
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.PagerState
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.presentation.data.Navigator

sealed class ReaderEvent {
    data class OnTextIsEmpty(val onLoaded: () -> Unit) : ReaderEvent()
    data class OnLoadText(
        val listState: LazyListState,
        val refreshList: (Book) -> Unit,
        val onLoaded: () -> Unit,
        val onTextIsEmpty: () -> Unit
    ) : ReaderEvent()

    data class OnShowHideMenu(val show: Boolean? = null, val context: ComponentActivity) :
        ReaderEvent()

    data class OnGoBack(
        val context: ComponentActivity,
        val navigator: Navigator,
        val listState: LazyListState,
        val refreshList: (Book) -> Unit,
        val navigate: (Navigator) -> Unit
    ) : ReaderEvent()

    data class OnChangeProgress(
        val progress: Float,
        val navigator: Navigator,
        val firstVisibleItemIndex: Int,
        val firstVisibleItemOffset: Int,
        val refreshList: (Book) -> Unit
    ) : ReaderEvent()

    data class OnScroll(val listState: LazyListState, val progress: Float) : ReaderEvent()
    data object OnShowHideSettingsBottomSheet : ReaderEvent()
    data class OnScrollToSettingsPage(val page: Int, val pagerState: PagerState) : ReaderEvent()
    data class OnMoveBookToAlreadyRead(
        val context: ComponentActivity,
        val listState: LazyListState,
        val onUpdateCategories: (Book) -> Unit,
        val updatePage: (Int) -> Unit,
        val navigator: Navigator
    ) : ReaderEvent()

    data class OnTranslateText(
        val textToTranslate: String,
        val context: ComponentActivity,
        val noAppsFound: () -> Unit
    ) : ReaderEvent()

    data class OnOpenDictionary(
        val textToDefine: String,
        val context: ComponentActivity,
        val noAppsFound: () -> Unit
    ) : ReaderEvent()
}