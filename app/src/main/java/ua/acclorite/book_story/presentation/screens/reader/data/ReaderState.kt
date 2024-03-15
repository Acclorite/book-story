package ua.acclorite.book_story.presentation.screens.reader.data

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.util.UIText

@Immutable
data class ReaderState(
    val book: Book,
    val letters: Int = 0,
    val words: Int = 0,

    val errorMessage: UIText? = null,

    val showMenu: Boolean = false,
    val showSettingsBottomSheet: Boolean = false,
    val currentPage: Int = 0,
)