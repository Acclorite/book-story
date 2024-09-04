package ua.acclorite.book_story.presentation.screens.library.components.dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.LocalBrowseViewModel
import ua.acclorite.book_story.presentation.core.components.LocalHistoryViewModel
import ua.acclorite.book_story.presentation.core.components.LocalLibraryViewModel
import ua.acclorite.book_story.presentation.core.components.custom_dialog.CustomDialogWithContent
import ua.acclorite.book_story.presentation.core.util.showToast
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseEvent
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent

/**
 * Delete dialog. Deletes all selected books.
 */
@Composable
fun LibraryDeleteDialog() {
    val context = LocalContext.current
    val state = LocalLibraryViewModel.current.state
    val onEvent = LocalLibraryViewModel.current.onEvent
    val onBrowseEvent = LocalBrowseViewModel.current.onEvent
    val onHistoryEvent = LocalHistoryViewModel.current.onEvent

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
                    onBrowseEvent(BrowseEvent.OnLoadList)
                    onHistoryEvent(HistoryEvent.OnLoadList)
                }
            )
            context.getString(R.string.books_deleted)
                .showToast(context = context)
        }
    )
}