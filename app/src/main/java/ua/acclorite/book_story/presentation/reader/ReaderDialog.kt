package ua.acclorite.book_story.presentation.reader

import androidx.compose.runtime.Composable
import ua.acclorite.book_story.domain.util.Dialog
import ua.acclorite.book_story.ui.reader.ReaderEvent
import ua.acclorite.book_story.ui.reader.ReaderScreen

@Composable
fun ReaderDialog(
    dialog: Dialog?,
    updateText: (ReaderEvent.OnUpdateText) -> Unit,
    dismissDialog: (ReaderEvent.OnDismissDialog) -> Unit,
    navigateToBookInfo: (startUpdate: Boolean) -> Unit
) {
    when (dialog) {
        ReaderScreen.UPDATE_DIALOG -> {
            ReaderUpdateDialog(
                updateText = updateText,
                dismissDialog = dismissDialog,
                navigateToBookInfo = navigateToBookInfo
            )
        }
    }
}