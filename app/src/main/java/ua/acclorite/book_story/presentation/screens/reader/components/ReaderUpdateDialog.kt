package ua.acclorite.book_story.presentation.screens.reader.components

import androidx.activity.ComponentActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Upgrade
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.LocalReaderViewModel
import ua.acclorite.book_story.presentation.core.components.custom_dialog.CustomDialogWithContent
import ua.acclorite.book_story.presentation.core.navigation.LocalNavigator
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderEvent

/**
 * Reader Update Dialog.
 * Navigates user to BookInfo and starts book update.
 */
@Composable
fun ReaderUpdateDialog() {
    val onEvent = LocalReaderViewModel.current.onEvent
    val onNavigate = LocalNavigator.current
    val context = LocalContext.current

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
            onEvent(
                ReaderEvent.OnUpdateText(
                    activity = context as ComponentActivity,
                    onNavigate = onNavigate
                )
            )
        },
        withDivider = false
    )
}