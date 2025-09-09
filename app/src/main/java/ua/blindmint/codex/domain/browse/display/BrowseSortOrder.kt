/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.domain.browse.display

import androidx.compose.runtime.Immutable

@Immutable
enum class BrowseSortOrder {
    NAME,
    FILE_FORMAT,
    LAST_MODIFIED,
    FILE_SIZE,
}

fun String.toBrowseSortOrder(): BrowseSortOrder {
    return try {
        BrowseSortOrder.valueOf(this)
    } catch (_: IllegalArgumentException) {
        BrowseSortOrder.LAST_MODIFIED
    }
}