package ua.acclorite.book_story.presentation.screens.start.data

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import ua.acclorite.book_story.presentation.data.Screen
import ua.acclorite.book_story.presentation.data.launchActivity
import javax.inject.Inject

@OptIn(ExperimentalPermissionsApi::class)
@HiltViewModel
class StartViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(StartState())
    val state = _state.asStateFlow()

    private var storagePermissionJob: Job? = null
    private var notificationsPermissionJob: Job? = null

    fun onEvent(event: StartEvent) {
        when (event) {
            is StartEvent.OnGoBack -> {
                storagePermissionJob?.cancel()
                notificationsPermissionJob?.cancel()

                if (_state.value.currentScreen == StartNavigationScreen.entries.first()) {
                    event.onQuit()
                    return
                }

                _state.update {
                    it.copy(
                        useBackAnimation = true
                    )
                }

                if (_state.value.isDone) {
                    _state.update {
                        it.copy(
                            isDone = false,
                            currentScreen = StartNavigationScreen.entries.last()
                        )
                    }
                    return
                }

                _state.update {
                    val previousScreenIndex = StartNavigationScreen.entries.indexOf(
                        it.currentScreen
                    ) - 1

                    if (previousScreenIndex == -1) {
                        return
                    }

                    it.copy(
                        currentScreen = StartNavigationScreen.entries[previousScreenIndex]
                    )
                }
            }

            is StartEvent.OnGoForward -> {
                storagePermissionJob?.cancel()
                notificationsPermissionJob?.cancel()

                _state.update {
                    it.copy(
                        useBackAnimation = false
                    )
                }

                if (_state.value.currentScreen == StartNavigationScreen.entries.last()) {
                    _state.update {
                        it.copy(
                            isDone = true
                        )
                    }
                    return
                }

                _state.update {
                    val nextScreenIndex = StartNavigationScreen.entries.indexOf(
                        it.currentScreen
                    ) + 1

                    if (nextScreenIndex == 0) {
                        return
                    }

                    it.copy(
                        currentScreen = StartNavigationScreen.entries[nextScreenIndex]
                    )
                }
            }

            is StartEvent.OnStoragePermissionRequest -> {
                val legacyStoragePermission = Build.VERSION.SDK_INT < Build.VERSION_CODES.R

                val isPermissionGranted = if (legacyStoragePermission) {
                    event.legacyStoragePermissionState.status.isGranted
                } else {
                    Environment.isExternalStorageManager()
                }

                if (isPermissionGranted) {
                    _state.update {
                        it.copy(
                            storagePermissionGranted = true
                        )
                    }
                    return
                }

                if (legacyStoragePermission) {
                    if (!event.legacyStoragePermissionState.status.shouldShowRationale) {
                        event.legacyStoragePermissionState.launchPermissionRequest()
                    } else {
                        val uri = Uri.parse("package:${event.activity.packageName}")
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri)

                        var failure = false
                        intent.launchActivity(event.activity) {
                            failure = true
                        }
                        if (failure) {
                            return
                        }
                    }
                }

                if (!legacyStoragePermission) {
                    val uri = Uri.parse("package:${event.activity.packageName}")
                    val intent = Intent(
                        Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                        uri
                    )

                    var failure = false
                    intent.launchActivity(event.activity) {
                        failure = true
                    }
                    if (failure) {
                        return
                    }
                }

                storagePermissionJob?.cancel()
                storagePermissionJob = viewModelScope.launch {
                    while (true) {
                        val granted = if (legacyStoragePermission) {
                            event.legacyStoragePermissionState.status.isGranted
                        } else {
                            Environment.isExternalStorageManager()
                        }

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

            is StartEvent.OnNotificationsPermissionRequest -> {
                if (event.notificationsPermissionState.status.isGranted) {
                    _state.update {
                        it.copy(
                            notificationsPermissionGranted = true
                        )
                    }
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

            is StartEvent.OnGoToBrowse -> {
                event.onNavigate {
                    navigate(Screen.Browse, saveInBackStack = false)
                }
                event.onCompletedStartGuide()
            }

            is StartEvent.OnGoToHelp -> {
                event.onNavigate {
                    navigate(Screen.Help(true), saveInBackStack = false)
                }
            }

            is StartEvent.OnResetStartScreen -> {
                _state.update {
                    StartState()
                }
            }
        }
    }

    fun checkPermissions(
        storagePermissionState: PermissionState,
        notificationPermissionState: PermissionState
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val legacyStoragePermission = Build.VERSION.SDK_INT < Build.VERSION_CODES.R
            val legacyNotificationPermission = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU

            val storagePermissionGranted = if (!legacyStoragePermission) {
                Environment.isExternalStorageManager()
            } else {
                storagePermissionState.status.isGranted
            }
            val notificationPermissionGranted = if (!legacyNotificationPermission) {
                notificationPermissionState.status.isGranted
            } else {
                true
            }

            _state.update {
                it.copy(
                    storagePermissionGranted = storagePermissionGranted,
                    notificationsPermissionGranted = notificationPermissionGranted
                )
            }
        }
    }
}










