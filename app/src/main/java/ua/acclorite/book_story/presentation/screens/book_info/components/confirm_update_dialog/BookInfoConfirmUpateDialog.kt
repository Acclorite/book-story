package ua.acclorite.book_story.presentation.screens.book_info.components.confirm_update_dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.custom_dialog.CustomDialogWithLazyColumn
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoEvent
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoViewModel
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.history.data.HistoryViewModel
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryViewModel

@Composable
fun BookInfoConfirmUpdateDialog(
    libraryViewModel: LibraryViewModel,
    historyViewModel: HistoryViewModel,
    viewModel: BookInfoViewModel,
    snackbarHostState: SnackbarHostState
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    CustomDialogWithLazyColumn(
        title = stringResource(id = R.string.confirm_update),
        imageVectorIcon = Icons.Default.Update,
        description = stringResource(
            id = R.string.confirm_update_description
        ),
        actionText = stringResource(id = R.string.confirm),
        isActionEnabled = true,
        onDismiss = { viewModel.onEvent(BookInfoEvent.OnDismissConfirmUpdateDialog) },
        onAction = {
            viewModel.onEvent(
                BookInfoEvent.OnConfirmUpdate(
                    snackbarState = snackbarHostState,
                    context = context,
                    refreshList = {
                        libraryViewModel.onEvent(LibraryEvent.OnUpdateBook(it))
                        historyViewModel.onEvent(HistoryEvent.OnUpdateBook(it))
                    }
                )
            )
        },
        withDivider = false,
        items = {
            if (state.authorChanged) {
                item {
                    BookInfoConfirmUpdateDialogItem(title = stringResource(id = R.string.author))
                }
            }
            if (state.descriptionChanged) {
                item {
                    BookInfoConfirmUpdateDialogItem(title = stringResource(id = R.string.description))
                }
            }
            if (state.textChanged) {
                item {
                    BookInfoConfirmUpdateDialogItem(title = stringResource(id = R.string.text))
                }
            }
        }
    )
}








