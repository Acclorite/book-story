package ua.acclorite.book_story.presentation.screens.settings.nested.general.components.settings

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainState
import ua.acclorite.book_story.presentation.screens.settings.components.SwitchWithTitle
import ua.acclorite.book_story.presentation.screens.settings.data.SettingsEvent

/**
 * Check For Updates setting.
 * If true, checks for the update on app start.
 */
@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("InlinedApi")
@Composable
fun LazyItemScope.CheckForUpdatesSetting(
    state: State<MainState>,
    onMainEvent: (MainEvent) -> Unit,
    onSettingsEvent: (SettingsEvent) -> Unit
) {
    val activity = LocalContext.current as ComponentActivity
    val notificationsPermissionState = rememberPermissionState(
        permission = Manifest.permission.POST_NOTIFICATIONS
    )

    SwitchWithTitle(
        selected = state.value.checkForUpdates!!,
        modifier = Modifier.animateItem(),
        title = stringResource(id = R.string.check_for_updates_option),
        description = stringResource(id = R.string.check_for_updates_option_desc)
    ) {
        onSettingsEvent(
            SettingsEvent.OnGeneralChangeCheckForUpdates(
                enable = !state.value.checkForUpdates!!,
                activity = activity,
                notificationsPermissionState = notificationsPermissionState,
                onChangeCheckForUpdates = {
                    onMainEvent(
                        MainEvent.OnChangeCheckForUpdates(
                            it
                        )
                    )
                }
            )
        )
    }
}