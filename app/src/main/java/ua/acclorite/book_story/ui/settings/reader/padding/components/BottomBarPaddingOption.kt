/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.reader.padding.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.ui.common.components.settings.SliderWithTitle
import ua.acclorite.book_story.ui.common.helpers.LocalSettings

@Composable
fun BottomBarPaddingOption() {
    val settings = LocalSettings.current

    SliderWithTitle(
        value = settings.bottomBarPadding.value to "pt",
        fromValue = 0,
        toValue = 24,
        title = stringResource(id = R.string.bottom_bar_padding_option),
        onValueChange = {
            settings.bottomBarPadding.update(it)
        }
    )
}