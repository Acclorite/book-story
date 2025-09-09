/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.domain.use_case.permission

import android.net.Uri
import ua.blindmint.codex.domain.repository.PermissionRepository
import javax.inject.Inject

class GrantPersistableUriPermission @Inject constructor(
    private val repository: PermissionRepository
) {

    suspend fun execute(uri: Uri) {
        repository.grantPersistableUriPermission(uri)
    }
}