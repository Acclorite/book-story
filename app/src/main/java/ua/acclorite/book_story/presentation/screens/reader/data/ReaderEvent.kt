package ua.acclorite.book_story.presentation.screens.reader.data

import androidx.activity.ComponentActivity
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.model.LineWithTranslation
import ua.acclorite.book_story.domain.model.SelectableLanguage
import ua.acclorite.book_story.domain.util.ID
import ua.acclorite.book_story.domain.util.LanguageCode
import ua.acclorite.book_story.domain.util.OnNavigate
import ua.acclorite.book_story.domain.util.UIText

@Immutable
sealed class ReaderEvent {
    data object OnTextIsEmpty : ReaderEvent()
    data class OnLoadText(
        val refreshList: (Book) -> Unit,
        val onError: (UIText) -> Unit,
        val onTextIsEmpty: () -> Unit
    ) : ReaderEvent()

    data class OnShowHideMenu(
        val show: Boolean? = null,
        val context: ComponentActivity
    ) : ReaderEvent()

    data class OnShowHideTranslatorBottomSheet(val show: Boolean? = null) : ReaderEvent()

    data class OnChangeTranslatorSettings(
        val enableTranslator: Boolean? = null,
        val translateFrom: String? = null,
        val translateTo: String? = null,
        val doubleClickTranslation: Boolean? = null,
        val translateWhenOpen: Boolean? = null
    ) : ReaderEvent()

    data class OnShowHideLanguageBottomSheet(
        val show: Boolean? = null,
        val translateFrom: Boolean? = null
    ) : ReaderEvent()

    data class OnTranslateText(
        val keys: Set<ID>?,
        val error: suspend (UIText) -> Unit
    ) : ReaderEvent()

    data object OnDismissDownloadLanguageDialog : ReaderEvent()

    data object OnCancelTranslation : ReaderEvent()

    data class OnUndoTranslation(val id: ID?) : ReaderEvent()

    data class OnDownloadLanguages(
        val onSuccess: suspend (Map<ID, LineWithTranslation>) -> Unit,
        val error: suspend (UIText) -> Unit
    ) : ReaderEvent()

    data class OnSelectLanguage(val language: SelectableLanguage) : ReaderEvent()

    data class OnUpdateLanguage(
        val languageCode: LanguageCode,
        val calculation: (SelectableLanguage) -> SelectableLanguage
    ) : ReaderEvent()

    data class OnGoBack(
        val context: ComponentActivity,
        val navigate: () -> Unit,
        val refreshList: (Book) -> Unit
    ) : ReaderEvent()

    data class OnChangeProgress(
        val progress: Float,
        val firstVisibleItemIndex: Int,
        val firstVisibleItemOffset: Int,
        val refreshList: (Book) -> Unit
    ) : ReaderEvent()

    data class OnScroll(val progress: Float) : ReaderEvent()
    data object OnShowHideSettingsBottomSheet : ReaderEvent()
    data class OnScrollToSettingsPage(val page: Int, val pagerState: PagerState) : ReaderEvent()
    data class OnMoveBookToAlreadyRead(
        val context: ComponentActivity,
        val onUpdateCategories: (Book) -> Unit,
        val updatePage: (Int) -> Unit,
        val onNavigate: OnNavigate
    ) : ReaderEvent()

    data class OnOpenTranslator(
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