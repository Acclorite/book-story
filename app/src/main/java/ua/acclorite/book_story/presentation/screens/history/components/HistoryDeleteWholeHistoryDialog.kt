package ua.acclorite.book_story.presentation.screens.history.components

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.CustomDialogWithContent
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.history.data.HistoryViewModel

@Composable
fun HistoryDeleteWholeHistoryDialog(
    viewModel: HistoryViewModel
) {
    val context = LocalContext.current

    CustomDialogWithContent(
        title = stringResource(id = R.string.delete_history),
        imageVectorIcon = Icons.Outlined.DeleteOutline,
        description = stringResource(
            id = R.string.delete_history_description
        ),
        actionText = stringResource(id = R.string.delete),
        onDismiss = { viewModel.onEvent(HistoryEvent.OnShowHideDeleteWholeHistoryDialog) },
        withDivider = false,
        isActionEnabled = true,
        onAction = {
            viewModel.onEvent(
                HistoryEvent.OnDeleteWholeHistory
            )
            Toast.makeText(
                context,
                context.getString(R.string.history_deleted),
                Toast.LENGTH_LONG
            ).show()
        }
    )
}