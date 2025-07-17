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
fun ProgressBarPaddingOption() {
    val settings = LocalSettings.current

    ExpandingTransition(visible = settings.progressBar.value) {
        SliderWithTitle(
            value = settings.progressBarPadding.value to "pt",
            fromValue = 1,
            toValue = 12,
            title = stringResource(id = R.string.progress_bar_padding_option),
            onValueChange = {
                settings.progressBarPadding.update(it)
            }
        )
    }
}