package ua.acclorite.book_story.presentation.history

import androidx.compose.runtime.Composable
import ua.acclorite.book_story.domain.util.Dialog
import ua.acclorite.book_story.ui.history.HistoryEvent
import ua.acclorite.book_story.ui.history.HistoryScreen

@Composable
fun HistoryDialog(
    dialog: Dialog?,
    actionDeleteWholeHistoryDialog: (HistoryEvent.OnActionDeleteWholeHistoryDialog) -> Unit,
    dismissDialog: (HistoryEvent.OnDismissDialog) -> Unit
) {
    when (dialog) {
        HistoryScreen.DELETE_WHOLE_HISTORY_DIALOG -> {
            HistoryDeleteWholeHistoryDialog(
                actionDeleteWholeHistoryDialog = actionDeleteWholeHistoryDialog,
                dismissDialog = dismissDialog
            )
        }
    }
}