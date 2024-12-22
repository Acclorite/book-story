package ua.acclorite.book_story.presentation.library

import androidx.compose.runtime.Composable
import ua.acclorite.book_story.domain.library.book.SelectableBook
import ua.acclorite.book_story.domain.library.category.CategoryWithBooks
import ua.acclorite.book_story.domain.util.Dialog
import ua.acclorite.book_story.ui.library.LibraryEvent
import ua.acclorite.book_story.ui.library.LibraryScreen

@Composable
fun LibraryDialog(
    dialog: Dialog?,
    books: List<SelectableBook>,
    categories: List<CategoryWithBooks>,
    selectedItemsCount: Int,
    actionMoveDialog: (LibraryEvent.OnActionMoveDialog) -> Unit,
    actionDeleteDialog: (LibraryEvent.OnActionDeleteDialog) -> Unit,
    dismissDialog: (LibraryEvent.OnDismissDialog) -> Unit
) {
    when (dialog) {
        LibraryScreen.MOVE_DIALOG -> {
            LibraryMoveDialog(
                books = books,
                categories = categories,
                selectedItemsCount = selectedItemsCount,
                actionMoveDialog = actionMoveDialog,
                dismissDialog = dismissDialog
            )
        }

        LibraryScreen.DELETE_DIALOG -> {
            LibraryDeleteDialog(
                selectedItemsCount = selectedItemsCount,
                actionDeleteDialog = actionDeleteDialog,
                dismissDialog = dismissDialog
            )
        }
    }
}