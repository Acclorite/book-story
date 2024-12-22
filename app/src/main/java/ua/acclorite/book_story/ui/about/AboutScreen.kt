package ua.acclorite.book_story.ui.about

import android.os.Parcelable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import ua.acclorite.book_story.domain.navigator.Screen
import ua.acclorite.book_story.presentation.about.AboutContent
import ua.acclorite.book_story.presentation.core.components.top_bar.collapsibleTopAppBarScrollBehavior
import ua.acclorite.book_story.presentation.navigator.LocalNavigator
import ua.acclorite.book_story.ui.credits.CreditsScreen
import ua.acclorite.book_story.ui.licenses.LicensesScreen

@Parcelize
object AboutScreen : Screen, Parcelable {

    @IgnoredOnParcel
    const val UPDATE_DIALOG = "update_dialog"

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val screenModel = hiltViewModel<AboutModel>()

        val state = screenModel.state.collectAsStateWithLifecycle()
        val (scrollBehavior, listState) = TopAppBarDefaults.collapsibleTopAppBarScrollBehavior()

        AboutContent(
            scrollBehavior = scrollBehavior,
            dialog = state.value.dialog,
            updateLoading = state.value.updateLoading,
            updateInfo = state.value.updateInfo,
            listState = listState,
            checkForUpdate = screenModel::onEvent,
            navigateToBrowserPage = screenModel::onEvent,
            dismissDialog = screenModel::onEvent,
            navigateToLicenses = {
                navigator.push(LicensesScreen)
            },
            navigateToCredits = {
                navigator.push(CreditsScreen)
            },
            navigateBack = {
                navigator.pop()
            }
        )
    }
}