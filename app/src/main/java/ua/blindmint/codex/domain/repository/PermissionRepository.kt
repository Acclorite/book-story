/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.domain.repository

import android.net.Uri

interface PermissionRepository {

    suspend fun grantPersistableUriPermission(
        uri: Uri
    )

    suspend fun releasePersistableUriPermission(
        uri: Uri
    )
}