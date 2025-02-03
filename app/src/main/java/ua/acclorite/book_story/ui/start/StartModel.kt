package ua.acclorite.book_story.ui.start

import android.os.Build
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.yield
import ua.acclorite.book_story.domain.use_case.permission.GrantNotificationsPermission
import ua.acclorite.book_story.domain.use_case.permission.GrantStoragePermission
import javax.inject.Inject

@HiltViewModel
class StartModel @Inject constructor(
    private val grantStoragePermission: GrantStoragePermission,
    private val grantNotificationsPermission: GrantNotificationsPermission
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
                storagePermissionJob?.cancel()
                storagePermissionJob = viewModelScope.launch(Dispatchers.IO) {
                    grantStoragePermission.execute(
                        activity = event.activity,
                        storagePermissionState = event.storagePermissionState
                    ).apply {
                        if (this) {
                            _state.update {
                                it.copy(
                                    storagePermissionGranted = true
                                )
                            }
                        }
                    }
                }
            }

            is StartEvent.OnNotificationsPermissionRequest -> {
                notificationsPermissionJob?.cancel()
                notificationsPermissionJob = viewModelScope.launch(Dispatchers.IO) {
                    grantNotificationsPermission.execute(
                        activity = event.activity,
                        notificationsPermissionState = event.notificationsPermissionState
                    ).apply {
                        if (this) {
                            _state.update {
                                it.copy(
                                    notificationsPermissionGranted = true
                                )
                            }
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