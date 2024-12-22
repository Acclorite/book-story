package ua.acclorite.book_story.ui.reader

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import ua.acclorite.book_story.domain.library.book.Book
import ua.acclorite.book_story.domain.reader.Chapter
import ua.acclorite.book_story.domain.reader.Checkpoint
import ua.acclorite.book_story.domain.ui.UIText
import ua.acclorite.book_story.domain.util.BottomSheet
import ua.acclorite.book_story.domain.util.Dialog
import ua.acclorite.book_story.domain.util.Drawer
import ua.acclorite.book_story.presentation.core.constants.Constants
import ua.acclorite.book_story.presentation.core.constants.provideEmptyBook

@Immutable
data class ReaderState(
    val book: Book = Constants.provideEmptyBook(),
    val text: List<AnnotatedString> = emptyList(),
    val listState: LazyListState = LazyListState(),

    val currentChapter: Chapter? = null,
    val currentChapterProgress: Float = 0f,

    val errorMessage: UIText? = null,
    val isLoading: Boolean = true,

    val showMenu: Boolean = false,
    val checkpoint: Checkpoint = Checkpoint(0, 0),
    val lockMenu: Boolean = false,

    val checkingForUpdate: Boolean = false,
    val updateFound: Boolean = false,

    val dialog: Dialog? = null,
    val bottomSheet: BottomSheet? = null,
    val drawer: Drawer? = null
)