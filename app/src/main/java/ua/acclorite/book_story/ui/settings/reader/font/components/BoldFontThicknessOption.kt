/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.reader.font.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.ui.common.components.settings.SliderWithTitle
import ua.acclorite.book_story.ui.common.helpers.LocalSettings

@Composable
fun BoldFontThicknessOption() {
    val settings = LocalSettings.current

    SliderWithTitle(
        value = (settings.boldFontThickness.value / 100) to "",
        valuePlaceholder = "+${settings.boldFontThickness.value}",
        showPlaceholder = true,
        fromValue = 1,
        toValue = 8,
        title = stringResource(id = R.string.bold_font_thickness_option),
        onValueChange = { settings.boldFontThickness.update(it * 100) }
    )
}
