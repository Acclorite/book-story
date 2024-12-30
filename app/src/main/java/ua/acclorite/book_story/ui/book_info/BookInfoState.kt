package ua.acclorite.book_story.ui.book_info

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.library.book.Book
import ua.acclorite.book_story.domain.util.BottomSheet
import ua.acclorite.book_story.domain.util.Dialog
import ua.acclorite.book_story.presentation.core.constants.Constants
import ua.acclorite.book_story.presentation.core.constants.provideEmptyBook

@Immutable
data class BookInfoState(
    val book: Book = Constants.provideEmptyBook(),

    val editTitle: Boolean = false,
    val hasTitleFocused: Boolean = false,
    val titleValue: String = "",

    val editAuthor: Boolean = false,
    val hasAuthorFocused: Boolean = false,
    val authorValue: String = "",

    val editDescription: Boolean = false,
    val hasDescriptionFocused: Boolean = false,
    val descriptionValue: String = "",

    val canResetCover: Boolean = false,

    val dialog: Dialog? = null,
    val bottomSheet: BottomSheet? = null
)