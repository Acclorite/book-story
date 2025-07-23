/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.reader.images.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.reader.model.ReaderColorEffects
import ua.acclorite.book_story.ui.common.components.settings.ChipsWithTitle
import ua.acclorite.book_story.ui.common.helpers.LocalSettings
import ua.acclorite.book_story.ui.common.model.ListItem
import ua.acclorite.book_story.ui.theme.ExpandingTransition

@Composable
fun ImagesColorEffectsOption() {
    val settings = LocalSettings.current

    ExpandingTransition(visible = settings.images.value) {
        ChipsWithTitle(
            title = stringResource(id = R.string.images_color_effects_option),
            chips = ReaderColorEffects.entries.map { item ->
                ListItem(
                    item = item,
                    title = stringResource(id = item.title),
                    selected = item == settings.imagesColorEffects.value
                )
            },
            onClick = { item ->
                settings.imagesColorEffects.update(item)
            }
        )
    }
}