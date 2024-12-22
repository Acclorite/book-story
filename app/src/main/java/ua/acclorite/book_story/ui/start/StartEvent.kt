@file:OptIn(ExperimentalPermissionsApi::class)

package ua.acclorite.book_story.ui.start

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Immutable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState

@Immutable
sealed class StartEvent {
    data class OnCheckPermissions(
        val storagePermissionState: PermissionState,
        val notificationsPermissionState: PermissionState
    ) : StartEvent()

    data class OnStoragePermissionRequest(
        val activity: ComponentActivity,
        val storagePermissionState: PermissionState
    ) : StartEvent()

    data class OnNotificationsPermissionRequest(
        val activity: ComponentActivity,
        val notificationsPermissionState: PermissionState
    ) : StartEvent()
}