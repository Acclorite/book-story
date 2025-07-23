/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.appearance.theme_preferences.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.ui.common.components.settings.SegmentedButtonWithTitle
import ua.acclorite.book_story.ui.common.helpers.LocalSettings
import ua.acclorite.book_story.ui.common.model.ListItem
import ua.acclorite.book_story.ui.theme.ExpandingTransition
import ua.acclorite.book_story.ui.theme.model.PureDark

@Composable
fun PureDarkOption() {
    val settings = LocalSettings.current

    ExpandingTransition(visible = settings.darkTheme.value.isDark()) {
        SegmentedButtonWithTitle(
            title = stringResource(id = R.string.pure_dark_option),
            buttons = PureDark.entries.map { item ->
                ListItem(
                    item = item,
                    title = stringResource(id = item.title),
                    selected = item == settings.pureDark.value
                )
            },
            onClick = { item ->
                settings.pureDark.update(item)
            }
        )
    }
}