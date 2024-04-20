@file:OptIn(ExperimentalPermissionsApi::class)

package ua.acclorite.book_story.presentation.screens.settings.data

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Immutable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState

@Immutable
sealed class SettingsEvent {
    data class OnGeneralChangeCheckForUpdates(
        val enable: Boolean,
        val activity: ComponentActivity,
        val notificationsPermissionState: PermissionState,
        val onChangeCheckForUpdates: (Boolean) -> Unit
    ) : SettingsEvent()
}