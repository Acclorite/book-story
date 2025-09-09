/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

@file:Suppress("FunctionName")

package ua.blindmint.codex.presentation.settings.reader.system

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import ua.blindmint.codex.R
import ua.blindmint.codex.presentation.settings.components.SettingsSubcategory
import ua.blindmint.codex.presentation.settings.reader.system.components.CustomScreenBrightnessOption
import ua.blindmint.codex.presentation.settings.reader.system.components.ScreenBrightnessOption
import ua.blindmint.codex.presentation.settings.reader.system.components.ScreenOrientationOption

fun LazyListScope.SystemSubcategory(
    titleColor: @Composable () -> Color = { MaterialTheme.colorScheme.primary },
    title: @Composable () -> String = { stringResource(id = R.string.system_reader_settings) },
    showTitle: Boolean = true,
    showDivider: Boolean = true
) {
    SettingsSubcategory(
        titleColor = titleColor,
        title = title,
        showTitle = showTitle,
        showDivider = showDivider
    ) {
        item {
            CustomScreenBrightnessOption()
        }

        item {
            ScreenBrightnessOption()
        }

        item {
            ScreenOrientationOption()
        }
    }
}