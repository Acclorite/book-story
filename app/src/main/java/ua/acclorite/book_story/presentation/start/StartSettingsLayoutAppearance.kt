/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

@file:Suppress("FunctionName")

package ua.acclorite.book_story.presentation.start

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.settings.appearance.theme_preferences.ThemePreferencesSubcategory

fun LazyListScope.StartSettingsLayoutAppearance() {
    ThemePreferencesSubcategory(
        title = { stringResource(id = R.string.start_theme_preferences) },
        showDivider = false
    )
}