/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.repository

interface PermissionRepository {
    suspend fun grantPersistableUriPermission(
        uri: String
    ): Result<Unit>

    suspend fun releasePersistableUriPermission(
        uri: String
    ): Result<Unit>
}