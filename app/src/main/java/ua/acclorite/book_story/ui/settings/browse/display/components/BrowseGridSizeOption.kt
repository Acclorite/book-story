/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.browse.display.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.browse.model.BrowseLayout
import ua.acclorite.book_story.ui.common.components.settings.SliderWithTitle
import ua.acclorite.book_story.ui.common.helpers.LocalSettings
import ua.acclorite.book_story.ui.theme.ExpandingTransition

@Composable
fun BrowseGridSizeOption() {
    val settings = LocalSettings.current

    ExpandingTransition(visible = settings.browseLayout.value == BrowseLayout.GRID) {
        SliderWithTitle(
            value = settings.browseGridSize.value
                    to " ${stringResource(R.string.grid_size_per_row)}",
            valuePlaceholder = stringResource(id = R.string.grid_size_auto),
            showPlaceholder = settings.browseAutoGridSize.value,
            fromValue = 0,
            toValue = 10,
            title = stringResource(id = R.string.grid_size_option),
            onValueChange = {
                settings.browseAutoGridSize.update(it == 0)
                settings.browseGridSize.update(it)
            }
        )
    }
}