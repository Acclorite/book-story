package ua.acclorite.book_story.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class LanguageHistory(
    val languageCode: String,
    val order: Int
)
