@file:OptIn(ExperimentalPermissionsApi::class)

package ua.acclorite.book_story.ui.settings

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import ua.acclorite.book_story.domain.util.ID

@Immutable
sealed class SettingsEvent {

    data class OnChangeCheckForUpdates(
        val enable: Boolean,
        val activity: ComponentActivity,
        val notificationsPermissionState: PermissionState,
        val onChangeCheckForUpdates: (Boolean) -> Unit
    ) : SettingsEvent()

    data class OnSelectColorPreset(
        val id: ID
    ) : SettingsEvent()

    data class OnSelectPreviousPreset(
        val context: Context
    ) : SettingsEvent()

    data class OnSelectNextPreset(
        val context: Context
    ) : SettingsEvent()

    data class OnDeleteColorPreset(
        val id: ID
    ) : SettingsEvent()

    data class OnUpdateColorPresetTitle(
        val id: ID,
        val title: String
    ) : SettingsEvent()

    data class OnShuffleColorPreset(
        val id: ID
    ) : SettingsEvent()

    data class OnAddColorPreset(
        val backgroundColor: Color,
        val fontColor: Color
    ) : SettingsEvent()

    data class OnReorderColorPresets(
        val from: Int,
        val to: Int
    ) : SettingsEvent()

    data class OnUpdateColorPresetColor(
        val id: ID,
        val backgroundColor: Color?,
        val fontColor: Color?
    ) : SettingsEvent()

    data object OnConfirmReorderColorPresets : SettingsEvent()

    data class OnScrollToColorPreset(
        val index: Int
    ) : SettingsEvent()
}