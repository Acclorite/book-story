package ua.acclorite.book_story.presentation.about

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import ua.acclorite.book_story.data.remote.dto.LatestReleaseInfo
import ua.acclorite.book_story.domain.util.Dialog
import ua.acclorite.book_story.ui.about.AboutEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutContent(
    scrollBehavior: TopAppBarScrollBehavior,
    dialog: Dialog? = null,
    updateLoading: Boolean,
    updateInfo: LatestReleaseInfo?,
    listState: LazyListState,
    checkForUpdate: (AboutEvent.OnCheckForUpdate) -> Unit,
    navigateToBrowserPage: (AboutEvent.OnNavigateToBrowserPage) -> Unit,
    dismissDialog: (AboutEvent.OnDismissDialog) -> Unit,
    navigateToLicenses: () -> Unit,
    navigateToCredits: () -> Unit,
    navigateBack: () -> Unit
) {
    AboutDialog(
        dialog = dialog,
        updateInfo = updateInfo,
        navigateToBrowserPage = navigateToBrowserPage,
        dismissDialog = dismissDialog
    )

    AboutScaffold(
        scrollBehavior = scrollBehavior,
        updateLoading = updateLoading,
        listState = listState,
        checkForUpdate = checkForUpdate,
        navigateToBrowserPage = navigateToBrowserPage,
        navigateToLicenses = navigateToLicenses,
        navigateToCredits = navigateToCredits,
        navigateBack = navigateBack
    )
}