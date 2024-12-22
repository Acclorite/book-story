package ua.acclorite.book_story.presentation.start

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import ua.acclorite.book_story.domain.navigator.StackEvent
import ua.acclorite.book_story.domain.ui.ButtonItem
import ua.acclorite.book_story.ui.main.MainEvent
import ua.acclorite.book_story.ui.start.StartEvent
import ua.acclorite.book_story.ui.start.StartScreen

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun StartContent(
    currentPage: Int,
    stackEvent: StackEvent,
    storagePermissionGranted: Boolean,
    notificationsPermissionGranted: Boolean,
    storagePermissionState: PermissionState,
    notificationsPermissionState: PermissionState,
    languages: List<ButtonItem>,
    changeLanguage: (MainEvent.OnChangeLanguage) -> Unit,
    storagePermissionRequest: (StartEvent.OnStoragePermissionRequest) -> Unit,
    notificationsPermissionRequest: (StartEvent.OnNotificationsPermissionRequest) -> Unit,
    navigateForward: () -> Unit,
    navigateBack: () -> Unit,
    navigateToBrowse: () -> Unit,
    navigateToHelp: () -> Unit
) {
    StartContentTransition(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        targetValue = when {
            currentPage in 0..2 -> StartScreen.SETTINGS
            else -> StartScreen.DONE
        },
        stackEvent = stackEvent
    ) { page ->
        when (page) {
            StartScreen.SETTINGS -> {
                StartSettings(
                    currentPage = currentPage,
                    stackEvent = stackEvent,
                    storagePermissionGranted = storagePermissionGranted,
                    notificationsPermissionGranted = notificationsPermissionGranted,
                    storagePermissionState = storagePermissionState,
                    notificationsPermissionState = notificationsPermissionState,
                    languages = languages,
                    changeLanguage = changeLanguage,
                    storagePermissionRequest = storagePermissionRequest,
                    notificationsPermissionRequest = notificationsPermissionRequest,
                    navigateForward = navigateForward
                )
            }

            StartScreen.DONE -> {
                StartDone(
                    navigateToBrowse = navigateToBrowse,
                    navigateToHelp = navigateToHelp
                )
            }
        }
    }

    StartBackHandler(
        navigateBack = navigateBack
    )
}