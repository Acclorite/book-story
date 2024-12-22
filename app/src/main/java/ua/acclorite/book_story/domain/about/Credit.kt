package ua.acclorite.book_story.domain.about

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.ui.UIText

@Immutable
data class Credit(
    val name: String,
    val source: String?,
    val credits: List<UIText>,
    val website: String?
)