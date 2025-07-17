/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.theme.model

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

enum class DarkTheme {
    FOLLOW_SYSTEM,
    OFF,
    ON;

    @Composable
    fun isDark(): Boolean {
        return when (this) {
            FOLLOW_SYSTEM -> isSystemInDarkTheme()
            ON -> true
            OFF -> false
        }
    }
}