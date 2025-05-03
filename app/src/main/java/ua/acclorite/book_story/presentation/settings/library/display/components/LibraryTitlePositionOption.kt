/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.settings.library.display.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.common.components.settings.ChipsWithTitle
import ua.acclorite.book_story.presentation.common.model.ButtonItem
import ua.acclorite.book_story.ui.library.model.LibraryLayout
import ua.acclorite.book_story.ui.library.model.LibraryTitlePosition
import ua.acclorite.book_story.ui.main.MainEvent
import ua.acclorite.book_story.ui.main.MainModel
import ua.acclorite.book_story.ui.theme.ExpandingTransition

@Composable
fun LibraryTitlePositionOption() {
    val mainModel = hiltViewModel<MainModel>()
    val state = mainModel.state.collectAsStateWithLifecycle()

    ExpandingTransition(visible = state.value.libraryLayout == LibraryLayout.GRID) {
        ChipsWithTitle(
            title = stringResource(id = R.string.title_position_option),
            chips = LibraryTitlePosition.entries.map {
                ButtonItem(
                    it.toString(),
                    when (it) {
                        LibraryTitlePosition.OFF -> {
                            stringResource(id = R.string.library_title_position_off)
                        }

                        LibraryTitlePosition.BELOW -> {
                            stringResource(id = R.string.library_title_position_below)
                        }

                        LibraryTitlePosition.INSIDE -> {
                            stringResource(id = R.string.library_title_position_inside)
                        }
                    },
                    MaterialTheme.typography.labelLarge,
                    it == state.value.libraryTitlePosition
                )
            }
        ) {
            mainModel.onEvent(
                MainEvent.OnChangeLibraryTitlePosition(
                    it.id
                )
            )
        }
    }
}