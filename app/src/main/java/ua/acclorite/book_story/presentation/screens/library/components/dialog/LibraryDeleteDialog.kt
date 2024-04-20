package ua.acclorite.book_story.presentation.screens.library.components.dialog

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.custom_dialog.CustomDialogWithContent
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseEvent
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryState

/**
 * Delete dialog. Deletes all selected books.
 */
@Composable
fun LibraryDeleteDialog(
    state: State<LibraryState>,
    onEvent: (LibraryEvent) -> Unit,
    onBrowseLoadEvent: (BrowseEvent.OnLoadList) -> Unit,
    onHistoryLoadEvent: (HistoryEvent.OnLoadList) -> Unit
) {
    val context = LocalContext.current

    CustomDialogWithContent(
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
                    onBrowseLoadEvent(BrowseEvent.OnLoadList)
                    onHistoryLoadEvent(HistoryEvent.OnLoadList)
                }
            )
            Toast.makeText(
                context,
                context.getString(R.string.books_deleted),
                Toast.LENGTH_LONG
            ).show()
        }
    )
}