/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.library.display.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.library.model.LibraryLayout
import ua.acclorite.book_story.ui.common.components.settings.SliderWithTitle
import ua.acclorite.book_story.ui.common.helpers.LocalSettings
import ua.acclorite.book_story.ui.theme.ExpandingTransition

@Composable
fun LibraryGridSizeOption() {
    val settings = LocalSettings.current

    ExpandingTransition(visible = settings.libraryLayout.value == LibraryLayout.GRID) {
        SliderWithTitle(
            value = settings.libraryGridSize.value
                    to " ${stringResource(R.string.grid_size_per_row)}",
            valuePlaceholder = stringResource(id = R.string.grid_size_auto),
            showPlaceholder = settings.libraryAutoGridSize.value,
            fromValue = 0,
            toValue = 10,
            title = stringResource(id = R.string.grid_size_option),
            onValueChange = {
                settings.libraryAutoGridSize.update(it == 0)
                settings.libraryGridSize.update(it)
            }
        )
    }
}