package ua.acclorite.book_story.presentation.screens.book_info.data

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.model.Category
import ua.acclorite.book_story.domain.util.Constants

@Immutable
data class BookInfoState(
    val book: Book = Constants.EMPTY_BOOK,

    val isLoadingUpdate: Boolean = false,
    val isRefreshing: Boolean = false,
    val showConfirmUpdateDialog: Boolean = false,
    val updatedBook: Book? = null,
    val authorChanged: Boolean = false,
    val descriptionChanged: Boolean = false,
    val textChanged: Boolean = false,

    val editTitle: Boolean = false,
    val hasFocused: Boolean = false,
    val titleValue: String = "",

    val showChangeCoverBottomSheet: Boolean = false,
    val showDetailsBottomSheet: Boolean = false,

    val showDeleteDialog: Boolean = false,
    val showMoveDialog: Boolean = false,
    val selectedCategory: Category = Category.READING,
)
