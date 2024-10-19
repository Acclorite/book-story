package ua.acclorite.book_story.presentation.screens.library.components.dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.dialog.DialogWithContent
import ua.acclorite.book_story.presentation.core.util.showToast
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseEvent
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseViewModel
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.history.data.HistoryViewModel
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryViewModel

/**
 * Delete dialog. Deletes all selected books.
 */
@Composable
fun LibraryDeleteDialog() {
    val context = LocalContext.current
    val state = LibraryViewModel.getState()
    val onEvent = LibraryViewModel.getEvent()
    val onBrowseEvent = BrowseViewModel.getEvent()
    val onHistoryEvent = HistoryViewModel.getEvent()

    DialogWithContent(
        title = stringResource(id = R.string.delete_books),
        imageVectorIcon = Icons.Outlined.DeleteOutline,
        description = stringResource(
            id = R.string.delete_books_description,
            state.value.books.filter { it.second }.size
        ),
        actionText = stringResource(id = R.string.delete),
        onDismiss = { onEvent(LibraryEvent.OnShowHideDeleteDialog) },
        withDivider = false,
        isActionEnabled = true,
        onAction = {
            onEvent(
                LibraryEvent.OnDeleteBooks {
                    onBrowseEvent(BrowseEvent.OnLoadList)
                    onHistoryEvent(HistoryEvent.OnLoadList)
                }
            )
            context.getString(R.string.books_deleted)
                .showToast(context = context)
        }
    )
}