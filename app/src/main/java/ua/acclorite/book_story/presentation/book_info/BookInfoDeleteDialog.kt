package ua.acclorite.book_story.presentation.book_info

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.dialog.Dialog
import ua.acclorite.book_story.ui.book_info.BookInfoEvent

@Composable
fun BookInfoDeleteDialog(
    actionDeleteDialog: (BookInfoEvent.OnActionDeleteDialog) -> Unit,
    dismissDialog: (BookInfoEvent.OnDismissDialog) -> Unit,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current

    Dialog(
        title = stringResource(id = R.string.delete_book),
        imageVectorIcon = Icons.Outlined.DeleteOutline,
        description = stringResource(
            id = R.string.delete_book_description
        ),
        actionText = stringResource(id = R.string.delete),
        onDismiss = { dismissDialog(BookInfoEvent.OnDismissDialog) },
        withDivider = false,
        actionEnabled = true,
        onAction = {
            actionDeleteDialog(
                BookInfoEvent.OnActionDeleteDialog(
                    context = context,
                    navigateBack = navigateBack
                )
            )
        }
    )
}