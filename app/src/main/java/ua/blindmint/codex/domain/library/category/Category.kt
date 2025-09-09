/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.domain.library.category

import androidx.compose.runtime.Immutable

@Immutable
enum class Category {
    READING,
    ALREADY_READ,
    PLANNING,
    DROPPED
}