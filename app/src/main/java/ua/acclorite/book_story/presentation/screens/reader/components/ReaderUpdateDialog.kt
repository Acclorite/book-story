package ua.acclorite.book_story.presentation.screens.reader.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Upgrade
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.LocalReaderViewModel
import ua.acclorite.book_story.presentation.core.components.custom_dialog.CustomDialogWithContent
import ua.acclorite.book_story.presentation.core.navigation.LocalOnNavigate
import ua.acclorite.book_story.presentation.core.navigation.Screen
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderEvent

/**
 * Reader Update Dialog.
 * Navigates user to BookInfo and starts book update.
 */
@Composable
fun ReaderUpdateDialog() {
    val state = LocalReaderViewModel.current.state
    val onEvent = LocalReaderViewModel.current.onEvent
    val onNavigate = LocalOnNavigate.current

    CustomDialogWithContent(
        title = stringResource(id = R.string.update_book),
        imageVectorIcon = Icons.Rounded.Upgrade,
        description = stringResource(
            id = R.string.update_book_description
        ),
        actionText = stringResource(id = R.string.proceed),
        isActionEnabled = true,
        onDismiss = { onEvent(ReaderEvent.OnShowHideUpdateDialog(false)) },
        onAction = {
            onNavigate {
                navigate(
                    Screen.BookInfo(
                        bookId = state.value.book.id,
                        startUpdate = true
                    ),
                    useBackAnimation = true,
                    saveInBackStack = false
                )
            }
        },
        withDivider = false
    )
}