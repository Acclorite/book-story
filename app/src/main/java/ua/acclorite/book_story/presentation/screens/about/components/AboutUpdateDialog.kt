package ua.acclorite.book_story.presentation.screens.about.components

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Update
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.custom_dialog.CustomDialogWithContent
import ua.acclorite.book_story.presentation.screens.about.data.AboutEvent
import ua.acclorite.book_story.presentation.screens.about.data.AboutState

/**
 * Update dialog. If there is app update transfers to the download page.
 */
@Composable
fun AboutUpdateDialog(
    state: State<AboutState>,
    onEvent: (AboutEvent) -> Unit
) {
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
                    page = context.getString(R.string.download_latest_release_page),
                    context = context,
                    noAppsFound = {
                        Toast.makeText(
                            context,
                            context.getString(R.string.error_no_browser),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            )
            onEvent(AboutEvent.OnDismissUpdateDialog)
        },
        withDivider = false,
    )
}