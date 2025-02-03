/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.repository

import androidx.activity.ComponentActivity
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState

interface PermissionRepository {

    @OptIn(ExperimentalPermissionsApi::class)
    suspend fun grantStoragePermission(
        activity: ComponentActivity,
        storagePermissionState: PermissionState
    ): Boolean

    @OptIn(ExperimentalPermissionsApi::class)
    suspend fun grantNotificationsPermission(
        activity: ComponentActivity,
        notificationsPermissionState: PermissionState
    ): Boolean
}