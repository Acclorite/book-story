package ua.acclorite.book_story.presentation.screens.history.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.LocalHistoryViewModel
import ua.acclorite.book_story.presentation.core.components.LocalLibraryViewModel
import ua.acclorite.book_story.presentation.core.components.custom_dialog.CustomDialogWithContent
import ua.acclorite.book_story.presentation.core.util.showToast
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent

/**
 * Delete whole history dialog.
 */
@Composable
fun HistoryDeleteWholeHistoryDialog() {
    val context = LocalContext.current
    val onEvent = LocalHistoryViewModel.current.onEvent
    val onLibraryEvent = LocalLibraryViewModel.current.onEvent

    CustomDialogWithContent(
        title = stringResource(id = R.string.delete_history),
        imageVectorIcon = Icons.Outlined.DeleteOutline,
        description = stringResource(
            id = R.string.delete_history_description
        ),
        actionText = stringResource(id = R.string.delete),
        onDismiss = { onEvent(HistoryEvent.OnShowHideDeleteWholeHistoryDialog) },
        withDivider = false,
        isActionEnabled = true,
        onAction = {
            onEvent(
                HistoryEvent.OnDeleteWholeHistory {
                    onLibraryEvent(LibraryEvent.OnLoadList)
                }
            )
            context.getString(R.string.history_deleted)
                .showToast(context = context)
        }
    )
}