/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.repository

import android.net.Uri

interface PermissionRepository {

    suspend fun grantPersistableUriPermission(
        uri: Uri
    )

    suspend fun releasePersistableUriPermission(
        uri: Uri
    )
}