package ua.acclorite.book_story.presentation.screens.book_info.components.dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.LocalBookInfoViewModel
import ua.acclorite.book_story.presentation.core.components.LocalBrowseViewModel
import ua.acclorite.book_story.presentation.core.components.LocalHistoryViewModel
import ua.acclorite.book_story.presentation.core.components.LocalLibraryViewModel
import ua.acclorite.book_story.presentation.core.components.custom_dialog.CustomDialogWithContent
import ua.acclorite.book_story.presentation.core.navigation.LocalOnNavigate
import ua.acclorite.book_story.presentation.core.util.showToast
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoEvent
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseEvent
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent

/**
 * Delete dialog. Deletes current book.
 */
@Composable
fun BookInfoDeleteDialog() {
    val onEvent = LocalBookInfoViewModel.current.onEvent
    val onLibraryEvent = LocalLibraryViewModel.current.onEvent
    val onHistoryEvent = LocalHistoryViewModel.current.onEvent
    val onBrowseEvent = LocalBrowseViewModel.current.onEvent
    val onNavigate = LocalOnNavigate.current
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
                    onNavigate = onNavigate,
                    refreshList = {
                        onLibraryEvent(LibraryEvent.OnLoadList)
                        onBrowseEvent(BrowseEvent.OnLoadList)
                        onHistoryEvent(HistoryEvent.OnLoadList)
                    }
                )
            )
            context.getString(R.string.book_deleted)
                .showToast(context = context)
        }
    )
}