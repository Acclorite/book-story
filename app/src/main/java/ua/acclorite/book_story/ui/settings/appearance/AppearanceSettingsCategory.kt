/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

@file:Suppress("FunctionName")

package ua.acclorite.book_story.ui.settings.appearance

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ua.acclorite.book_story.ui.settings.appearance.colors.ColorsSubcategory
import ua.acclorite.book_story.ui.settings.appearance.theme_preferences.ThemePreferencesSubcategory

fun LazyListScope.AppearanceSettingsCategory(
    titleColor: @Composable () -> Color = { MaterialTheme.colorScheme.primary }
) {
    ThemePreferencesSubcategory(
        titleColor = titleColor
    )
    ColorsSubcategory(
        titleColor = titleColor,
        showDivider = false
    )
}