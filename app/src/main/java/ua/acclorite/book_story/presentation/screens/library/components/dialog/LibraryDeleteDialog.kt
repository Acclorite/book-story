package ua.acclorite.book_story.presentation.screens.library.components.dialog

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.custom_dialog.CustomDialogWithContent
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
fun LibraryDeleteDialog(
    viewModel: LibraryViewModel,
    browseViewModel: BrowseViewModel,
    historyViewModel: HistoryViewModel
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    CustomDialogWithContent(
        title = stringResource(id = R.string.delete_books),
        imageVectorIcon = Icons.Outlined.DeleteOutline,
        description = stringResource(
            id = R.string.delete_books_description,
            state.books.filter { it.second }.size
        ),
        actionText = stringResource(id = R.string.delete),
        onDismiss = { viewModel.onEvent(LibraryEvent.OnShowHideDeleteDialog) },
        withDivider = false,
        isActionEnabled = true,
        onAction = {
            viewModel.onEvent(
                LibraryEvent.OnDeleteBooks {
                    browseViewModel.onEvent(BrowseEvent.OnLoadList)
                    historyViewModel.onEvent(HistoryEvent.OnLoadList)
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