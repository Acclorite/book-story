package ua.acclorite.book_story.presentation.components.translator_language.data

import androidx.compose.runtime.Immutable
import androidx.compose.ui.focus.FocusRequester
import ua.acclorite.book_story.domain.model.Language
import ua.acclorite.book_story.domain.util.LanguageCode
import ua.acclorite.book_story.domain.util.UIText

@Immutable
sealed class TranslatorLanguageEvent {
    data class OnSelectLanguage(
        val languageCode: String,
        val onError: suspend (UIText) -> Unit,
        val onSelect: (String, String) -> Unit
    ) : TranslatorLanguageEvent()

    data class OnDownloadLanguage(
        val languageCode: String,
        val onComplete: suspend () -> Unit,
        val onFailure: (Exception) -> Unit
    ) : TranslatorLanguageEvent()

    data class OnCancelDownload(
        val languageCode: String
    ) : TranslatorLanguageEvent()

    data class OnDeleteLanguage(
        val languageCode: String
    ) : TranslatorLanguageEvent()

    data object OnSearchShowHide : TranslatorLanguageEvent()
    data class OnRequestFocus(val focusRequester: FocusRequester) : TranslatorLanguageEvent()
    data class OnSearchQueryChange(val query: String) : TranslatorLanguageEvent()
    data object OnSearch : TranslatorLanguageEvent()
    data object OnDismissDownloadDialog : TranslatorLanguageEvent()
    data class OnUpdateLanguage(
        val languageCode: String,
        val block: (Language) -> Language
    ) : TranslatorLanguageEvent()

    data class OnUpdateLanguageHistory(
        val languageToChoose: LanguageCode
    ) : TranslatorLanguageEvent()
}