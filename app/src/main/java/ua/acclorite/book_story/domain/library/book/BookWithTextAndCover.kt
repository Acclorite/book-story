package ua.acclorite.book_story.domain.library.book

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.util.CoverImage

@Immutable
data class BookWithTextAndCover(
    val book: Book,
    val coverImage: CoverImage?,
    val text: List<String>
)