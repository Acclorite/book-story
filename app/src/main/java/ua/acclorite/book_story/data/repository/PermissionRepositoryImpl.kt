/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.repository

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield
import ua.acclorite.book_story.domain.repository.PermissionRepository
import ua.acclorite.book_story.presentation.core.util.launchActivity
import javax.inject.Inject
import javax.inject.Singleton

private const val STORAGE_PERMISSION = "STORAGE PERM, REPO"

@Singleton
class PermissionRepositoryImpl @Inject constructor() : PermissionRepository {

    @SuppressLint("InlinedApi")
    @OptIn(ExperimentalPermissionsApi::class)
    override suspend fun grantStoragePermission(
        activity: ComponentActivity,
        storagePermissionState: PermissionState
    ): Boolean {
        Log.i(STORAGE_PERMISSION, "Requested storage permission")
        val legacyStoragePermission = Build.VERSION.SDK_INT < Build.VERSION_CODES.R

        val permissionGranted = if (legacyStoragePermission) {
            storagePermissionState.status.isGranted
        } else Environment.isExternalStorageManager()

        if (permissionGranted) {
            Log.i(STORAGE_PERMISSION, "Granted: Storage Permission is already granted")
            return true
        }

        when (legacyStoragePermission) {
            true -> {
                if (!storagePermissionState.status.shouldShowRationale) {
                    storagePermissionState.launchPermissionRequest()
                } else {
                    val uri = Uri.parse("package:${activity.packageName}")
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri)

                    intent.launchActivity(activity) {
                        Log.e(
                            STORAGE_PERMISSION,
                            "Could not launch \"ACTION_APPLICATION_DETAILS_SETTINGS\" activity"
                        )
                        return false
                    }
                }
            }

            false -> {
                val uri = Uri.parse("package:${activity.packageName}")
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri)

                intent.launchActivity(activity) {
                    Log.e(
                        STORAGE_PERMISSION,
                        "Could not launch \"ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION\" activity"
                    )
                    return false
                }
            }
        }

        for (i in 1..30) {
            val granted = if (legacyStoragePermission) {
                storagePermissionState.status.isGranted
            } else Environment.isExternalStorageManager()

            if (!granted) {
                delay(1000)
                yield()
                continue
            }

            yield()

            Log.i(STORAGE_PERMISSION, "Successfully granted")
            return true
            break
        }

        Log.e(STORAGE_PERMISSION, "Not granted: Timeout")
        return false
    }
}