/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.reader.reading_speed.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.ui.common.components.settings.SliderWithTitle
import ua.acclorite.book_story.ui.common.helpers.LocalSettings
import ua.acclorite.book_story.ui.theme.ExpandingTransition

@Composable
fun HorizontalLimiterDimmingOption() {
    val settings = LocalSettings.current

    ExpandingTransition(visible = settings.horizontalLimiter.value) {
        SliderWithTitle(
            value = settings.horizontalLimiterDimming.value to "%",
            toValue = 100,
            title = stringResource(id = R.string.horizontal_limiter_dimming_option),
            onValueChange = {
                settings.horizontalLimiterDimming.update(it)
            }
        )
    }
}