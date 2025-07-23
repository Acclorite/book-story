/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.reader.font.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.reader.model.ReaderFontThickness
import ua.acclorite.book_story.ui.common.components.settings.ChipsWithTitle
import ua.acclorite.book_story.ui.common.helpers.LocalSettings
import ua.acclorite.book_story.ui.common.model.ListItem

@Composable
fun FontThicknessOption() {
    val settings = LocalSettings.current

    ChipsWithTitle(
        title = stringResource(id = R.string.font_thickness_option),
        chips = ReaderFontThickness.entries.map { item ->
            ListItem(
                item = item,
                title = stringResource(id = item.title),
                textStyle = {
                    MaterialTheme.typography.labelLarge.copy(
                        fontFamily = settings.fontFamily.value.font,
                        fontWeight = item.thickness
                    )
                },
                selected = item == settings.fontThickness.value
            )
        },
        onClick = { item ->
            settings.fontThickness.update(item)
        }
    )
}