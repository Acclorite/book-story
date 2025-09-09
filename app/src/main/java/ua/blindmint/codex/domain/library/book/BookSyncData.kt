/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.domain.library.book

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class BookSyncData(
    val title: String,
    val author: String?,
    val fileSize: Long,
    val fileHash: String,
    val progress: Float,
    val lastUpdated: String
)