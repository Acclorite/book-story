package ua.acclorite.book_story.domain.model

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.util.ID
import ua.acclorite.book_story.domain.util.Selected
import ua.acclorite.book_story.domain.util.UIText

@Immutable
data class Language(
    val languageCode: String,
    val displayLanguage: String,
    val isDownloading: Boolean,
    val isCanceled: Boolean,
    val isDownloaded: Boolean,
    val isSelected: Selected,
    val historyOrder: Int?
)

@Immutable
data class SelectableLanguage(
    val languageCode: String,
    val displayLanguage: String,
    val isDownloading: Boolean,
    val isCanceled: Boolean,
    val isDownloaded: Boolean,
    val isError: Boolean,
    val errorMessage: UIText? = null,
    val isSelected: Selected,
    val canUnselect: Boolean,
    val occurrences: List<ID>?
)
