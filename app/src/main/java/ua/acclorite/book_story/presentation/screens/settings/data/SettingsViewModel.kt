package ua.acclorite.book_story.presentation.screens.settings.data

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import ua.acclorite.book_story.presentation.data.launchActivity
import javax.inject.Inject

@OptIn(ExperimentalPermissionsApi::class)
@HiltViewModel
class SettingsViewModel @Inject constructor(

) : ViewModel() {

    private var notificationsPermissionJob: Job? = null

    fun onEvent(event: SettingsEvent) {
        when (event) {

            is SettingsEvent.OnGeneralChangeCheckForUpdates -> {
                if (!event.enable) {
                    event.onChangeCheckForUpdates(false)
                    return
                }

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    event.onChangeCheckForUpdates(true)
                    return
                }

                if (event.notificationsPermissionState.status.isGranted) {
                    event.onChangeCheckForUpdates(true)
                    return
                }

                if (!event.notificationsPermissionState.status.shouldShowRationale) {
                    event.notificationsPermissionState.launchPermissionRequest()
                } else {
                    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, event.activity.packageName)

                    var failure = false
                    intent.launchActivity(event.activity) {
                        failure = true
                    }

                    if (failure) {
                        return
                    }
                }

                notificationsPermissionJob?.cancel()
                notificationsPermissionJob = viewModelScope.launch {
                    for (i in 1..10) {
                        if (!event.notificationsPermissionState.status.isGranted) {
                            delay(1000)
                            yield()
                            continue
                        }

                        yield()

                        event.onChangeCheckForUpdates(true)
                        break
                    }
                }
            }
        }
    }
}









