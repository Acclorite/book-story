package ua.acclorite.book_story.ui.reader

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Immutable

@Immutable
sealed class ReaderEvent {

    data class OnLoadText(
        val context: Context
    ) : ReaderEvent()

    data class OnMenuVisibility(
        val show: Boolean,
        val fullscreenMode: Boolean,
        val saveCheckpoint: Boolean,
        val activity: ComponentActivity
    ) : ReaderEvent()

    data class OnChangeProgress(
        val progress: Float,
        val firstVisibleItemIndex: Int,
        val firstVisibleItemOffset: Int
    ) : ReaderEvent()

    data class OnScrollToChapter(
        val chapterStartIndex: Int
    ) : ReaderEvent()

    data class OnScroll(
        val progress: Float
    ) : ReaderEvent()

    data object OnRestoreCheckpoint : ReaderEvent()

    data class OnLeave(
        val activity: ComponentActivity,
        val navigate: () -> Unit
    ) : ReaderEvent()

    data class OnOpenTranslator(
        val textToTranslate: String,
        val translateWholeParagraph: Boolean,
        val activity: ComponentActivity
    ) : ReaderEvent()

    data class OnOpenShareApp(
        val textToShare: String,
        val activity: ComponentActivity
    ) : ReaderEvent()

    data class OnOpenWebBrowser(
        val textToSearch: String,
        val activity: ComponentActivity
    ) : ReaderEvent()

    data class OnOpenDictionary(
        val textToDefine: String,
        val activity: ComponentActivity
    ) : ReaderEvent()

    data object OnShowSettingsBottomSheet : ReaderEvent()

    data object OnDismissBottomSheet : ReaderEvent()

    data object OnShowChaptersDrawer : ReaderEvent()

    data object OnDismissDrawer : ReaderEvent()
}