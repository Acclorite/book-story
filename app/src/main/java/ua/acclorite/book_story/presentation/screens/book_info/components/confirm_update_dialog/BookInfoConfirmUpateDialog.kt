package ua.acclorite.book_story.presentation.screens.book_info.components.confirm_update_dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.LocalBookInfoViewModel
import ua.acclorite.book_story.presentation.core.components.LocalHistoryViewModel
import ua.acclorite.book_story.presentation.core.components.LocalLibraryViewModel
import ua.acclorite.book_story.presentation.core.components.custom_dialog.CustomDialogWithContent
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoEvent
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent

/**
 * Confirm update dialog.
 * Updates book if action clicked.
 *
 * @param snackbarHostState [SnackbarHostState].
 */
@Composable
fun BookInfoConfirmUpdateDialog(snackbarHostState: SnackbarHostState) {
    val onEvent = LocalBookInfoViewModel.current.onEvent
    val onLibraryEvent = LocalLibraryViewModel.current.onEvent
    val onHistoryEvent = LocalHistoryViewModel.current.onEvent
    val context = LocalContext.current

    CustomDialogWithContent(
        title = stringResource(id = R.string.confirm_update),
        imageVectorIcon = Icons.Default.Update,
        description = stringResource(
            id = R.string.confirm_update_description
        ),
        actionText = stringResource(id = R.string.confirm),
        isActionEnabled = true,
        onDismiss = { onEvent(BookInfoEvent.OnDismissConfirmUpdateDialog) },
        onAction = {
            onEvent(
                BookInfoEvent.OnConfirmUpdate(
                    snackbarState = snackbarHostState,
                    context = context,
                    refreshList = {
                        onLibraryEvent(LibraryEvent.OnUpdateBook(it))
                        onHistoryEvent(HistoryEvent.OnUpdateBook(it))
                    }
                )
            )
        },
        withDivider = false
    )
}