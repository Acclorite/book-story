/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.common.helpers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import ua.acclorite.book_story.data.settings.SettingsManager

val LocalSettings = staticCompositionLocalOf<SettingsManager> {
    error("No settings provided")
}

@Composable
fun ProvideSettings(settings: SettingsManager, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalSettings provides settings) {
        content()
    }
}