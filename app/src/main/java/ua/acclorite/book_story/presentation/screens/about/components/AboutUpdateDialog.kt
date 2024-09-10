package ua.acclorite.book_story.presentation.screens.about.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Update
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.LocalAboutViewModel
import ua.acclorite.book_story.presentation.core.components.custom_dialog.CustomDialogWithContent
import ua.acclorite.book_story.presentation.core.constants.Constants
import ua.acclorite.book_story.presentation.core.util.showToast
import ua.acclorite.book_story.presentation.screens.about.data.AboutEvent

/**
 * Update dialog. If there is app update transfers to the download page.
 */
@Composable
fun AboutUpdateDialog() {
    val state = LocalAboutViewModel.current.state
    val onEvent = LocalAboutViewModel.current.onEvent
    val context = LocalContext.current

    val update = remember(state.value.showUpdateDialog) {
        state.value.updateInfo!!
    }

    CustomDialogWithContent(
        title = stringResource(id = R.string.update_query, update.tagName),
        imageVectorIcon = Icons.Default.Update,
        description = stringResource(
            id = R.string.update_app_description
        ),
        actionText = stringResource(id = R.string.download),
        isActionEnabled = true,
        disableOnClick = false,
        onDismiss = { onEvent(AboutEvent.OnDismissUpdateDialog) },
        onAction = {
            onEvent(
                AboutEvent.OnNavigateToBrowserPage(
                    page = Constants.RELEASES_PAGE,
                    context = context,
                    noAppsFound = {
                        context.getString(R.string.error_no_browser).showToast(
                            context = context,
                            longToast = false
                        )
                    }
                )
            )
            onEvent(AboutEvent.OnDismissUpdateDialog)
        },
        withDivider = false,
    )
}