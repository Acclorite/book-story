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
    val updatedText: List<String>? = null,

    val editTitle: Boolean = false,
    val hasTitleFocused: Boolean = false,
    val titleValue: String = "",

    val editAuthor: Boolean = false,
    val hasAuthorFocused: Boolean = false,
    val authorValue: String = "",

    val editDescription: Boolean = false,
    val hasDescriptionFocused: Boolean = false,
    val descriptionValue: String = "",

    val showChangeCoverBottomSheet: Boolean = false,
    val canResetCover: Boolean = false,

    val showDetailsBottomSheet: Boolean = false,

    val showDeleteDialog: Boolean = false,
    val showMoveDialog: Boolean = false,
    val selectedCategory: Category = Category.READING,
)
