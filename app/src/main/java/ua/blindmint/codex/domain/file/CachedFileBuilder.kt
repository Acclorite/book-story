/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.domain.file

import androidx.compose.runtime.Immutable

@Immutable
data class CachedFileBuilder(
    val name: String?,
    val path: String?,
    val size: Long? = null,
    val lastModified: Long? = null,
    val isDirectory: Boolean? = null
)