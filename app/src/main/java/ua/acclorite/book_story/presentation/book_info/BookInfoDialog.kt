package ua.acclorite.book_story.presentation.book_info

import androidx.compose.runtime.Composable
import ua.acclorite.book_story.domain.library.book.Book
import ua.acclorite.book_story.domain.util.Dialog
import ua.acclorite.book_story.ui.book_info.BookInfoEvent
import ua.acclorite.book_story.ui.book_info.BookInfoScreen

@Composable
fun BookInfoDialog(
    dialog: Dialog?,
    book: Book,
    actionDeleteDialog: (BookInfoEvent.OnActionDeleteDialog) -> Unit,
    actionMoveDialog: (BookInfoEvent.OnActionMoveDialog) -> Unit,
    dismissDialog: (BookInfoEvent.OnDismissDialog) -> Unit,
    navigateBack: () -> Unit,
    navigateToLibrary: () -> Unit
) {
    when (dialog) {
        BookInfoScreen.DELETE_DIALOG -> {
            BookInfoDeleteDialog(
                actionDeleteDialog = actionDeleteDialog,
                dismissDialog = dismissDialog,
                navigateBack = navigateBack
            )
        }

        BookInfoScreen.MOVE_DIALOG -> {
            BookInfoMoveDialog(
                book = book,
                actionMoveDialog = actionMoveDialog,
                dismissDialog = dismissDialog,
                navigateToLibrary = navigateToLibrary
            )
        }
    }
}