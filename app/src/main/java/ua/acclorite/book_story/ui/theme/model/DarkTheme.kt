/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.theme.model

import androidx.annotation.StringRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import ua.acclorite.book_story.R

enum class DarkTheme(@StringRes val title: Int) {
    FOLLOW_SYSTEM(R.string.dark_theme_follow_system),
    OFF(R.string.dark_theme_off),
    ON(R.string.dark_theme_on);

    @Composable
    fun isDark(): Boolean {
        return when (this) {
            FOLLOW_SYSTEM -> isSystemInDarkTheme()
            ON -> true
            OFF -> false
        }
    }
}