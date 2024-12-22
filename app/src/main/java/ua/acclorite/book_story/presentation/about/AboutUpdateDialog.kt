package ua.acclorite.book_story.presentation.about

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Update
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.data.remote.dto.LatestReleaseInfo
import ua.acclorite.book_story.presentation.core.components.dialog.Dialog
import ua.acclorite.book_story.presentation.core.constants.Constants
import ua.acclorite.book_story.presentation.core.constants.provideReleasesPage
import ua.acclorite.book_story.ui.about.AboutEvent

@Composable
fun AboutUpdateDialog(
    updateInfo: LatestReleaseInfo?,
    navigateToBrowserPage: (AboutEvent.OnNavigateToBrowserPage) -> Unit,
    dismissDialog: (AboutEvent.OnDismissDialog) -> Unit
) {
    val context = LocalContext.current

    Dialog(
        title = stringResource(id = R.string.update_query, updateInfo?.tagName ?: ""),
        imageVectorIcon = Icons.Default.Update,
        description = stringResource(
            id = R.string.update_app_description
        ),
        actionText = stringResource(id = R.string.proceed),
        actionEnabled = true,
        disableOnClick = false,
        onDismiss = { dismissDialog(AboutEvent.OnDismissDialog) },
        onAction = {
            navigateToBrowserPage(
                AboutEvent.OnNavigateToBrowserPage(
                    page = Constants.provideReleasesPage(),
                    context = context
                )
            )
            dismissDialog(AboutEvent.OnDismissDialog)
        },
        withDivider = false
    )
}