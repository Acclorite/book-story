/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.ui

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