/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
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
fun ImagesCaptionsOption() {
    val settings = LocalSettings.current

    ExpandingTransition(visible = settings.images.value) {
        SwitchWithTitle(
            selected = settings.imagesCaptions.value,
            title = stringResource(id = R.string.images_captions_option),
            description = stringResource(id = R.string.images_captions_option_desc),
            onClick = {
                settings.imagesCaptions.update(!settings.imagesCaptions.lastValue)
            }
        )
    }
}