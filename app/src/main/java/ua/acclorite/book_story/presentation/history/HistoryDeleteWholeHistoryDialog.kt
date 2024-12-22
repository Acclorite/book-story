package ua.acclorite.book_story.presentation.history

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.dialog.Dialog
import ua.acclorite.book_story.ui.history.HistoryEvent

@Composable
fun HistoryDeleteWholeHistoryDialog(
    actionDeleteWholeHistoryDialog: (HistoryEvent.OnActionDeleteWholeHistoryDialog) -> Unit,
    dismissDialog: (HistoryEvent.OnDismissDialog) -> Unit
) {
    val context = LocalContext.current
    Dialog(
        title = stringResource(id = R.string.delete_history),
        imageVectorIcon = Icons.Outlined.DeleteOutline,
        description = stringResource(id = R.string.delete_history_description),
        actionText = stringResource(id = R.string.delete),
        actionEnabled = true,
        onDismiss = {
            dismissDialog(HistoryEvent.OnDismissDialog)
        },
        onAction = {
            actionDeleteWholeHistoryDialog(
                HistoryEvent.OnActionDeleteWholeHistoryDialog(
                    context = context
                )
            )
        },
        withDivider = false
    )
}