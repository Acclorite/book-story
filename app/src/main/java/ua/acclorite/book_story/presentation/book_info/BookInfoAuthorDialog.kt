package ua.acclorite.book_story.presentation.book_info

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.library.book.Book
import ua.acclorite.book_story.domain.ui.UIText
import ua.acclorite.book_story.presentation.core.components.dialog.DialogWithTextField
import ua.acclorite.book_story.ui.book_info.BookInfoEvent

@Composable
fun BookInfoAuthorDialog(
    book: Book,
    actionAuthorDialog: (BookInfoEvent.OnActionAuthorDialog) -> Unit,
    dismissDialog: (BookInfoEvent.OnDismissDialog) -> Unit
) {
    val context = LocalContext.current
    DialogWithTextField(
        initialValue = book.author.getAsString() ?: "",
        lengthLimit = 100,
        onDismiss = {
            dismissDialog(BookInfoEvent.OnDismissDialog)
        },
        onAction = {
            actionAuthorDialog(
                BookInfoEvent.OnActionAuthorDialog(
                    author = if (it.isBlank()) UIText.StringResource(R.string.unknown_author)
                    else UIText.StringValue(it.trim().replace("\n", "")),
                    context = context
                )
            )
        }
    )
}