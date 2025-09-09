/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.domain.reader

import androidx.compose.runtime.Immutable

@Immutable
enum class ReaderProgressCount {
    PERCENTAGE,
    QUANTITY,
    PAGE
}

fun String.toProgressCount(): ReaderProgressCount {
    return ReaderProgressCount.valueOf(this)
}