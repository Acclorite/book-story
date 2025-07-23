/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.appearance.theme_preferences.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.ui.common.components.settings.SegmentedButtonWithTitle
import ua.acclorite.book_story.ui.common.helpers.LocalSettings
import ua.acclorite.book_story.ui.common.model.ListItem
import ua.acclorite.book_story.ui.theme.BookStoryTheme
import ua.acclorite.book_story.ui.theme.ExpandingTransition
import ua.acclorite.book_story.ui.theme.model.ThemeContrast

@Composable
fun ThemeContrastOption() {
    val settings = LocalSettings.current

    val themeContrastTheme = remember { mutableStateOf(settings.theme.lastValue) }
    LaunchedEffect(settings.theme.value) {
        if (settings.theme.lastValue.hasThemeContrast) {
            themeContrastTheme.value = settings.theme.lastValue
        }
    }

    BookStoryTheme(
        theme = themeContrastTheme.value,
        isDark = settings.darkTheme.value.isDark(),
        isPureDark = settings.pureDark.value.isPureDark(context = LocalContext.current),
        themeContrast = settings.themeContrast.value
    ) {
        ExpandingTransition(visible = settings.theme.value.hasThemeContrast) {
            SegmentedButtonWithTitle(
                title = stringResource(id = R.string.theme_contrast_option),
                enabled = settings.theme.value.hasThemeContrast,
                buttons = ThemeContrast.entries.map { item ->
                    ListItem(
                        item = item,
                        title = stringResource(id = item.title),
                        selected = item == settings.themeContrast.value
                    )
                },
                onClick = { item ->
                    settings.themeContrast.update(item)
                }
            )
        }
    }
}