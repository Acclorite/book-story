package ua.acclorite.book_story.presentation.screens.book_info.components.confirm_update_dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.custom_dialog.CustomDialogWithLazyColumn
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoEvent
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoState
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent

/**
 * Confirm update dialog. Updates book if action clicked.
 */
@Composable
fun BookInfoConfirmUpdateDialog(
    state: State<BookInfoState>,
    snackbarHostState: SnackbarHostState,
    onEvent: (BookInfoEvent) -> Unit,
    onLibraryUpdateEvent: (LibraryEvent.OnUpdateBook) -> Unit,
    onHistoryUpdateEvent: (HistoryEvent.OnUpdateBook) -> Unit,
) {
    val context = LocalContext.current

    CustomDialogWithLazyColumn(
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
                        onLibraryUpdateEvent(LibraryEvent.OnUpdateBook(it))
                        onHistoryUpdateEvent(HistoryEvent.OnUpdateBook(it))
                    }
                )
            )
        },
        withDivider = false,
        items = {
            if (state.value.authorChanged) {
                item {
                    BookInfoConfirmUpdateDialogItem(title = stringResource(id = R.string.author))
                }
            }
            if (state.value.descriptionChanged) {
                item {
                    BookInfoConfirmUpdateDialogItem(title = stringResource(id = R.string.description))
                }
            }
            if (state.value.textChanged) {
                item {
                    BookInfoConfirmUpdateDialogItem(title = stringResource(id = R.string.text))
                }
            }
        }
    )
}








