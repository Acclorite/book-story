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
import ua.acclorite.book_story.ui.common.components.settings.ChipsWithTitle
import ua.acclorite.book_story.ui.common.helpers.LocalSettings
import ua.acclorite.book_story.ui.common.model.ButtonItem
import ua.acclorite.book_story.ui.reader.data.ReaderData

@Composable
fun FontFamilyOption() {
    val settings = LocalSettings.current

    ChipsWithTitle(
        title = stringResource(id = R.string.font_family_option),
        chips = ReaderData.fonts.map {
            ButtonItem(
                id = it.id,
                title = it.fontName.asString(),
                textStyle = MaterialTheme.typography.labelLarge.copy(
                    fontFamily = it.font
                ),
                selected = it == settings.fontFamily.value
            )
        },
        onClick = {
            settings.fontFamily.update(
                ReaderData.fonts.find { font -> font.id == it.id }
                    ?: ReaderData.fonts[0]
            )
        }
    )
}