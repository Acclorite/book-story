package ua.acclorite.book_story.presentation.reader

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Upgrade
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.dialog.Dialog
import ua.acclorite.book_story.presentation.core.util.LocalActivity
import ua.acclorite.book_story.ui.reader.ReaderEvent

@Composable
fun ReaderUpdateDialog(
    updateText: (ReaderEvent.OnUpdateText) -> Unit,
    dismissDialog: (ReaderEvent.OnDismissDialog) -> Unit,
    navigateToBookInfo: (startUpdate: Boolean) -> Unit
) {
    val activity = LocalActivity.current

    Dialog(
        title = stringResource(id = R.string.update_book),
        imageVectorIcon = Icons.Rounded.Upgrade,
        description = stringResource(
            id = R.string.update_book_description
        ),
        actionText = stringResource(id = R.string.proceed),
        actionEnabled = true,
        onDismiss = { dismissDialog(ReaderEvent.OnDismissDialog) },
        onAction = {
            updateText(
                ReaderEvent.OnUpdateText(
                    activity = activity,
                    navigateToBookInfo = {
                        navigateToBookInfo(true)
                    }
                )
            )
        },
        withDivider = false
    )
}