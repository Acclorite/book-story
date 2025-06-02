/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.repository

import android.app.Application
import android.content.Intent
import androidx.core.net.toUri
import ua.acclorite.book_story.domain.repository.PermissionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionRepositoryImpl @Inject constructor(
    private val application: Application
) : PermissionRepository {

    override suspend fun grantPersistableUriPermission(uri: String): Result<Unit> = runCatching {
        application.contentResolver.takePersistableUriPermission(
            uri.toUri(),
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
    }

    override suspend fun releasePersistableUriPermission(uri: String): Result<Unit> = runCatching {
        application.contentResolver.releasePersistableUriPermission(
            uri.toUri(),
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
    }
}