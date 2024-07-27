@file:OptIn(ExperimentalPermissionsApi::class)

package ua.acclorite.book_story.presentation.screens.start.data

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Immutable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import ua.acclorite.book_story.domain.util.OnNavigate

@Immutable
sealed class StartEvent {
    data class OnGoBack(val onQuit: () -> Unit) : StartEvent()
    data object OnGoForward : StartEvent()
    data class OnStoragePermissionRequest(
        val activity: ComponentActivity,
        val legacyStoragePermissionState: PermissionState
    ) : StartEvent()

    data class OnNotificationsPermissionRequest(
        val activity: ComponentActivity,
        val notificationsPermissionState: PermissionState
    ) : StartEvent()

    data class OnGoToBrowse(val onNavigate: OnNavigate, val onCompletedStartGuide: () -> Unit) :
        StartEvent()

    data class OnGoToHelp(val onNavigate: OnNavigate) : StartEvent()

    data object OnResetStartScreen : StartEvent()
}







