package ua.acclorite.book_story.ui.start

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.yield
import ua.acclorite.book_story.presentation.core.util.launchActivity
import javax.inject.Inject

@HiltViewModel
class StartModel @Inject constructor(

) : ViewModel() {

    private val mutex = Mutex()

    private val _state = MutableStateFlow(StartState())
    val state = _state.asStateFlow()

    private var storagePermissionJob: Job? = null
    private var notificationsPermissionJob: Job? = null

    @OptIn(ExperimentalPermissionsApi::class)
    fun onEvent(event: StartEvent) {
        when (event) {
            is StartEvent.OnCheckPermissions -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val legacyStoragePermission = Build.VERSION.SDK_INT < Build.VERSION_CODES.R
                    val legacyNotificationPermission =
                        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU

                    val storagePermissionGranted = if (!legacyStoragePermission) {
                        Environment.isExternalStorageManager()
                    } else event.storagePermissionState.status.isGranted
                    val notificationPermissionGranted = if (!legacyNotificationPermission) {
                        event.notificationsPermissionState.status.isGranted
                    } else true

                    _state.update {
                        it.copy(
                            storagePermissionGranted = storagePermissionGranted,
                            notificationsPermissionGranted = notificationPermissionGranted
                        )
                    }
                }
            }

            is StartEvent.OnStoragePermissionRequest -> {
                viewModelScope.launch {
                    val legacyStoragePermission = Build.VERSION.SDK_INT < Build.VERSION_CODES.R

                    val isPermissionGranted = if (legacyStoragePermission) {
                        event.storagePermissionState.status.isGranted
                    } else Environment.isExternalStorageManager()

                    if (isPermissionGranted) {
                        _state.update {
                            it.copy(
                                storagePermissionGranted = true
                            )
                        }
                        return@launch
                    }

                    if (legacyStoragePermission) {
                        if (!event.storagePermissionState.status.shouldShowRationale) {
                            event.storagePermissionState.launchPermissionRequest()
                        } else {
                            val uri = Uri.parse("package:${event.activity.packageName}")
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri)

                            intent.launchActivity(event.activity) {
                                return@launch
                            }
                        }
                    }

                    if (!legacyStoragePermission) {
                        val uri = Uri.parse("package:${event.activity.packageName}")
                        val intent = Intent(
                            Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                            uri
                        )

                        intent.launchActivity(event.activity) {
                            return@launch
                        }
                    }

                    storagePermissionJob?.cancel()
                    storagePermissionJob = viewModelScope.launch {
                        while (true) {
                            val granted = if (legacyStoragePermission) {
                                event.storagePermissionState.status.isGranted
                            } else Environment.isExternalStorageManager()

                            if (!granted) {
                                delay(1000)
                                yield()
                                continue
                            }

                            yield()

                            _state.update {
                                it.copy(
                                    storagePermissionGranted = true
                                )
                            }
                            break
                        }
                    }
                }
            }

            is StartEvent.OnNotificationsPermissionRequest -> {
                viewModelScope.launch {
                    if (event.notificationsPermissionState.status.isGranted) {
                        _state.update {
                            it.copy(
                                notificationsPermissionGranted = true
                            )
                        }
                        return@launch
                    }

                    if (!event.notificationsPermissionState.status.shouldShowRationale) {
                        event.notificationsPermissionState.launchPermissionRequest()
                    } else {
                        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                        intent.putExtra(Settings.EXTRA_APP_PACKAGE, event.activity.packageName)

                        intent.launchActivity(event.activity) {
                            return@launch
                        }
                    }

                    notificationsPermissionJob?.cancel()
                    notificationsPermissionJob = viewModelScope.launch {
                        while (true) {
                            if (!event.notificationsPermissionState.status.isGranted) {
                                delay(1000)
                                yield()
                                continue
                            }

                            yield()

                            _state.update {
                                it.copy(
                                    notificationsPermissionGranted = true
                                )
                            }

                            break
                        }
                    }
                }
            }
        }
    }

    fun resetScreen() {
        viewModelScope.launch {
            _state.update {
                storagePermissionJob?.cancel()
                notificationsPermissionJob?.cancel()

                StartState()
            }
        }
    }

    private suspend inline fun <T> MutableStateFlow<T>.update(function: (T) -> T) {
        mutex.withLock {
            yield()
            this.value = function(this.value)
        }
    }
}