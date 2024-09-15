package ua.acclorite.book_story.presentation.screens.reader.data

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.model.Chapter
import ua.acclorite.book_story.domain.util.UIText
import ua.acclorite.book_story.presentation.core.constants.Constants

@Immutable
data class ReaderState(
    val book: Book = Constants.EMPTY_BOOK,
    val text: List<String> = emptyList(),
    val listState: LazyListState = LazyListState(),

    val currentChapter: Chapter? = null,
    val currentChapterProgress: Float = 0f,
    val showChaptersDrawer: Boolean = false,

    val errorMessage: UIText? = null,
    val loading: Boolean = true,

    val showMenu: Boolean = false,
    val checkpoint: Pair<Int, Int> = 0 to 0,
    val lockMenu: Boolean = false,

    val showSettingsBottomSheet: Boolean = false,
    val currentPage: Int = 0,
)