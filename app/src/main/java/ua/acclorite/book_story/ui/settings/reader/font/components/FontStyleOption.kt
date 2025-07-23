/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.reader.font.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import ua.acclorite.book_story.R
import ua.acclorite.book_story.ui.common.components.settings.SegmentedButtonWithTitle
import ua.acclorite.book_story.ui.common.helpers.LocalSettings
import ua.acclorite.book_story.ui.common.model.ListItem

@Composable
fun FontStyleOption() {
    val settings = LocalSettings.current

    SegmentedButtonWithTitle(
        title = stringResource(id = R.string.font_style_option),
        buttons = listOf(
            ListItem(
                item = false,
                title = stringResource(id = R.string.font_style_normal),
                textStyle = {
                    MaterialTheme.typography.labelLarge.copy(
                        fontFamily = settings.fontFamily.value.font,
                        fontStyle = FontStyle.Normal
                    )
                },
                selected = !settings.italic.value
            ),
            ListItem(
                item = true,
                title = stringResource(id = R.string.font_style_italic),
                textStyle = {
                    MaterialTheme.typography.labelLarge.copy(
                        fontFamily = settings.fontFamily.value.font,
                        fontStyle = FontStyle.Italic
                    )
                },
                selected = settings.italic.value
            )
        ),
        onClick = { item ->
            settings.italic.update(item)
        }
    )
}