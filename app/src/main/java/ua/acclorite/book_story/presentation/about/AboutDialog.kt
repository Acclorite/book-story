package ua.acclorite.book_story.presentation.about

import androidx.compose.runtime.Composable
import ua.acclorite.book_story.data.remote.dto.LatestReleaseInfo
import ua.acclorite.book_story.domain.util.Dialog
import ua.acclorite.book_story.ui.about.AboutEvent
import ua.acclorite.book_story.ui.about.AboutScreen

@Composable
fun AboutDialog(
    dialog: Dialog? = null,
    updateInfo: LatestReleaseInfo?,
    navigateToBrowserPage: (AboutEvent.OnNavigateToBrowserPage) -> Unit,
    dismissDialog: (AboutEvent.OnDismissDialog) -> Unit
) {
    when (dialog) {
        AboutScreen.UPDATE_DIALOG -> {
            AboutUpdateDialog(
                updateInfo = updateInfo,
                navigateToBrowserPage = navigateToBrowserPage,
                dismissDialog = dismissDialog
            )
        }
    }
}