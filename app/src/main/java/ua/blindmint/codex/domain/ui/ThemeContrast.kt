/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.domain.ui

import androidx.compose.runtime.Immutable

@Immutable
enum class ThemeContrast {
    STANDARD,
    MEDIUM,
    HIGH
}

fun String.toThemeContrast(): ThemeContrast {
    return ThemeContrast.valueOf(this)
}