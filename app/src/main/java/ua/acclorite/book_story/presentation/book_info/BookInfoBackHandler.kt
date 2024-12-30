package ua.acclorite.book_story.presentation.book_info

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import ua.acclorite.book_story.ui.book_info.BookInfoEvent

@Composable
fun BookInfoBackHandler(
    editTitle: Boolean,
    editAuthor: Boolean,
    editDescription: Boolean,
    editTitleMode: (BookInfoEvent.OnEditTitleMode) -> Unit,
    editAuthorMode: (BookInfoEvent.OnEditAuthorMode) -> Unit,
    editDescriptionMode: (BookInfoEvent.OnEditDescriptionMode) -> Unit,
    navigateBack: () -> Unit
) {
    BackHandler {
        var exitedEditMode = false

        if (editTitle) {
            editTitleMode(BookInfoEvent.OnEditTitleMode(false))
            exitedEditMode = true
        }

        if (editAuthor) {
            editAuthorMode(BookInfoEvent.OnEditAuthorMode(false))
            exitedEditMode = true
        }

        if (editDescription) {
            editDescriptionMode(BookInfoEvent.OnEditDescriptionMode(false))
            exitedEditMode = true
        }

        if (exitedEditMode) {
            return@BackHandler
        }

        navigateBack()
    }
}