/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.reader.images.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.ui.common.components.settings.SwitchWithTitle
import ua.acclorite.book_story.ui.common.helpers.LocalSettings
import ua.acclorite.book_story.ui.theme.ExpandingTransition

@Composable
fun ImagesOriginalSizeOption() {
    val settings = LocalSettings.current

    ExpandingTransition(visible = settings.images.value) {
        SwitchWithTitle(
            selected = settings.imagesOriginalSize.value,
            title = stringResource(id = R.string.images_original_size_option),
            description = stringResource(id = R.string.images_original_size_option_desc),
            onClick = {
                settings.imagesOriginalSize.update(!settings.imagesOriginalSize.lastValue)
            }
        )
    }
}
