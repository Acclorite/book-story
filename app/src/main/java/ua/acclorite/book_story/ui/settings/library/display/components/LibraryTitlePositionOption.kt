/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.library.display.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.library.model.LibraryLayout
import ua.acclorite.book_story.presentation.library.model.LibraryTitlePosition
import ua.acclorite.book_story.ui.common.components.settings.ChipsWithTitle
import ua.acclorite.book_story.ui.common.helpers.LocalSettings
import ua.acclorite.book_story.ui.common.model.ButtonItem
import ua.acclorite.book_story.ui.theme.ExpandingTransition

@Composable
fun LibraryTitlePositionOption() {
    val settings = LocalSettings.current

    ExpandingTransition(visible = settings.libraryLayout.value == LibraryLayout.GRID) {
        ChipsWithTitle(
            title = stringResource(id = R.string.title_position_option),
            chips = LibraryTitlePosition.entries.map {
                ButtonItem(
                    it.name,
                    when (it) {
                        LibraryTitlePosition.OFF -> stringResource(id = R.string.library_title_position_off)
                        LibraryTitlePosition.BELOW -> stringResource(id = R.string.library_title_position_below)
                        LibraryTitlePosition.INSIDE -> stringResource(id = R.string.library_title_position_inside)
                    },
                    MaterialTheme.typography.labelLarge,
                    it == settings.libraryTitlePosition.value
                )
            }
        ) {
            settings.libraryTitlePosition.update(LibraryTitlePosition.valueOf(it.id))
        }
    }
}