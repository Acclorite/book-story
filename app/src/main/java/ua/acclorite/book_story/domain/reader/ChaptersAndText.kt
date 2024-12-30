package ua.acclorite.book_story.domain.reader

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString

@Immutable
data class ChaptersAndText(
    val chapters: List<Chapter>,
    val text: List<AnnotatedString>
)