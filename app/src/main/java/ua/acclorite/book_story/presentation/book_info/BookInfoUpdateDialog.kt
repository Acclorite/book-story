package ua.acclorite.book_story.presentation.book_info

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.dialog.Dialog
import ua.acclorite.book_story.ui.book_info.BookInfoEvent

@Composable
fun BookInfoUpdateDialog(
    snackbarState: SnackbarHostState,
    actionUpdateDialog: (BookInfoEvent.OnActionUpdateDialog) -> Unit,
    dismissUpdateDialog: (BookInfoEvent.OnDismissUpdateDialog) -> Unit
) {
    val context = LocalContext.current

    Dialog(
        title = stringResource(id = R.string.confirm_update),
        imageVectorIcon = Icons.Default.Update,
        description = stringResource(
            id = R.string.confirm_update_description
        ),
        actionText = stringResource(id = R.string.confirm),
        actionEnabled = true,
        onDismiss = { dismissUpdateDialog(BookInfoEvent.OnDismissUpdateDialog) },
        onAction = {
            actionUpdateDialog(
                BookInfoEvent.OnActionUpdateDialog(
                    snackbarState = snackbarState,
                    context = context
                )
            )
        },
        withDivider = false
    )
}