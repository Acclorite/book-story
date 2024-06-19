package ua.acclorite.book_story.presentation.screens.book_info.components.dialog

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.custom_dialog.CustomDialogWithContent
import ua.acclorite.book_story.presentation.data.LocalNavigator
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoEvent
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseEvent
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent

/**
 * Delete dialog. Deletes current book.
 *
 * @param onEvent [BookInfoEvent] callback.
 * @param onLibraryLoadEvent [LibraryEvent] callback.
 * @param onHistoryLoadEvent [HistoryEvent] callback.
 * @param onBrowseLoadEvent [BrowseEvent] callback.
 */
@Composable
fun BookInfoDeleteDialog(
    onEvent: (BookInfoEvent) -> Unit,
    onLibraryLoadEvent: (LibraryEvent.OnLoadList) -> Unit,
    onHistoryLoadEvent: (HistoryEvent.OnLoadList) -> Unit,
    onBrowseLoadEvent: (BrowseEvent.OnLoadList) -> Unit
) {
    val navigator = LocalNavigator.current
    val context = LocalContext.current

    CustomDialogWithContent(
        title = stringResource(id = R.string.delete_book),
        imageVectorIcon = Icons.Outlined.DeleteOutline,
        description = stringResource(
            id = R.string.delete_book_description
        ),
        actionText = stringResource(id = R.string.delete),
        onDismiss = { onEvent(BookInfoEvent.OnShowHideDeleteDialog) },
        withDivider = false,
        isActionEnabled = true,
        onAction = {
            onEvent(
                BookInfoEvent.OnDeleteBook(
                    onNavigate = { navigator.it() },
                    refreshList = {
                        onLibraryLoadEvent(LibraryEvent.OnLoadList)
                        onBrowseLoadEvent(BrowseEvent.OnLoadList)
                        onHistoryLoadEvent(HistoryEvent.OnLoadList)
                    }
                )
            )
            Toast.makeText(
                context,
                context.getString(R.string.book_deleted),
                Toast.LENGTH_LONG
            ).show()
        }
    )
}