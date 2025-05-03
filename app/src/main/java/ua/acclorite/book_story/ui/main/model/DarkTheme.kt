/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.main.model

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

@Immutable
enum class DarkTheme {
    FOLLOW_SYSTEM,
    OFF,
    ON
}

@Composable
fun DarkTheme.isDark(): Boolean {
    return when (this) {
        DarkTheme.FOLLOW_SYSTEM -> isSystemInDarkTheme()
        DarkTheme.ON -> true
        DarkTheme.OFF -> false
    }
}