/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.reader.progress.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.ui.common.components.settings.SliderWithTitle
import ua.acclorite.book_story.ui.common.helpers.LocalSettings
import ua.acclorite.book_story.ui.theme.ExpandingTransition

@Composable
fun ProgressBarFontSizeOption() {
    val settings = LocalSettings.current

    ExpandingTransition(visible = settings.progressBar.value) {
        SliderWithTitle(
            value = settings.progressBarFontSize.value to "pt",
            fromValue = 4,
            toValue = 16,
            title = stringResource(id = R.string.progress_bar_font_size_option),
            onValueChange = {
                settings.progressBarFontSize.update(it)
            }
        )
    }
}