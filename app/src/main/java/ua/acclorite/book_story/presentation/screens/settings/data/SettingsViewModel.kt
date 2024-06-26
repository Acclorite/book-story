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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import javax.inject.Inject

@OptIn(ExperimentalPermissionsApi::class)
@HiltViewModel
class SettingsViewModel @Inject constructor(

) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

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

                    if (intent.resolveActivity(event.activity.packageManager) != null) {
                        event.activity.startActivity(intent)
                    } else {
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

            is SettingsEvent.OnReaderShowHideTranslatorLanguageBottomSheet -> {
                _state.update {
                    it.copy(
                        showReaderTranslatorLanguageBottomSheet = event.show
                            ?: !it.showReaderTranslatorLanguageBottomSheet,
                        readerTranslatorLanguageBottomSheetTranslateFrom = event.translateFrom
                            ?: false
                    )
                }
            }
        }
    }
}









