package ua.acclorite.book_story.presentation.components.translator_language.data

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.model.Language

@Immutable
data class TranslatorLanguageState(
    val selectedLanguage: String = "",
    val unselectedLanguage: String = "",
    val languageToSelect: String = "",
    val translateFromSelecting: Boolean = false,
    val showDownloadLanguageDialog: Boolean = false,

    val unfilteredLanguages: List<Language> = emptyList(),
    val languages: List<Language> = emptyList(),

    val showSearch: Boolean = false,
    val searchQuery: String = "",
    val hasFocused: Boolean = false,

    )
