package ua.acclorite.book_story.presentation.screens.reader.data

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.util.Constants
import ua.acclorite.book_story.domain.util.UIText

@Immutable
data class ReaderState(
    val book: Book = Constants.EMPTY_BOOK,
    val text: List<String> = emptyList(),
    val words: Int = 0,
    val letters: Int = 0,
    val listState: LazyListState = LazyListState(),

    val errorMessage: UIText? = null,
    val loading: Boolean = true,

    val showMenu: Boolean = false,
    val lockMenu: Boolean = false,

    val showSettingsBottomSheet: Boolean = false,
    val currentPage: Int = 0,
)