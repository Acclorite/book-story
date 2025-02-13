/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

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
import ua.acclorite.book_story.domain.use_case.permission.GrantStoragePermission
import javax.inject.Inject

@HiltViewModel
class StartModel @Inject constructor(
    private val grantStoragePermission: GrantStoragePermission
) : ViewModel() {

    private val mutex = Mutex()

    private val _state = MutableStateFlow(StartState())
    val state = _state.asStateFlow()

    private var storagePermissionJob: Job? = null

    @OptIn(ExperimentalPermissionsApi::class)
    fun onEvent(event: StartEvent) {
        when (event) {
            is StartEvent.OnCheckPermissions -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val legacyStoragePermission = Build.VERSION.SDK_INT < Build.VERSION_CODES.R
                    val storagePermissionGranted = if (!legacyStoragePermission) {
                        Environment.isExternalStorageManager()
                    } else event.storagePermissionState.status.isGranted

                    _state.update {
                        it.copy(
                            storagePermissionGranted = storagePermissionGranted
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
        }
    }

    fun resetScreen() {
        viewModelScope.launch {
            _state.update {
                storagePermissionJob?.cancel()
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