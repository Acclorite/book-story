package ua.acclorite.book_story.presentation.settings.general.components

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.dialog.Dialog
import ua.acclorite.book_story.presentation.core.components.settings.SwitchWithTitle
import ua.acclorite.book_story.presentation.core.util.LocalActivity
import ua.acclorite.book_story.ui.main.MainEvent
import ua.acclorite.book_story.ui.main.MainModel
import ua.acclorite.book_story.ui.settings.SettingsEvent
import ua.acclorite.book_story.ui.settings.SettingsModel

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("InlinedApi")
@Composable
fun CheckForUpdatesOption() {
    val mainModel = hiltViewModel<MainModel>()
    val settingsModel = hiltViewModel<SettingsModel>()

    val state = mainModel.state.collectAsStateWithLifecycle()
    val activity = LocalActivity.current
    val notificationsPermissionState = rememberPermissionState(
        permission = Manifest.permission.POST_NOTIFICATIONS
    )
    val showConfirmation = remember { mutableStateOf(false) }

    if (showConfirmation.value) {
        Dialog(
            title = stringResource(id = R.string.enable_check_for_updates),
            description = stringResource(id = R.string.enable_check_for_updates_description),
            actionText = stringResource(id = R.string.enable),
            imageVectorIcon = Icons.Default.Security,
            actionEnabled = true,
            onDismiss = { showConfirmation.value = false },
            onAction = {
                mainModel.onEvent(MainEvent.OnChangeCheckForUpdates(true))
                showConfirmation.value = false
            },
            withDivider = false
        )
    }

    SwitchWithTitle(
        selected = state.value.checkForUpdates,
        title = stringResource(id = R.string.check_for_updates_option),
        description = stringResource(id = R.string.check_for_updates_option_desc)
    ) {
        settingsModel.onEvent(
            SettingsEvent.OnChangeCheckForUpdates(
                enable = !state.value.checkForUpdates,
                activity = activity,
                notificationsPermissionState = notificationsPermissionState,
                onChangeCheckForUpdates = {
                    when (it) {
                        true -> showConfirmation.value = true
                        false -> mainModel.onEvent(MainEvent.OnChangeCheckForUpdates(false))
                    }
                }
            )
        )
    }
}