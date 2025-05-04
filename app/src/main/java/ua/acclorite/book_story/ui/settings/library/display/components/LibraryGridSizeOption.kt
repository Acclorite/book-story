/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.library.display.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.library.model.LibraryLayout
import ua.acclorite.book_story.presentation.main.MainEvent
import ua.acclorite.book_story.presentation.main.MainModel
import ua.acclorite.book_story.presentation.theme.ExpandingTransition
import ua.acclorite.book_story.ui.common.components.settings.SliderWithTitle

@Composable
fun LibraryGridSizeOption() {
    val mainModel = hiltViewModel<MainModel>()
    val state = mainModel.state.collectAsStateWithLifecycle()

    ExpandingTransition(visible = state.value.libraryLayout == LibraryLayout.GRID) {
        SliderWithTitle(
            value = state.value.libraryGridSize
                    to " ${stringResource(R.string.grid_size_per_row)}",
            valuePlaceholder = stringResource(id = R.string.grid_size_auto),
            showPlaceholder = state.value.libraryAutoGridSize,
            fromValue = 0,
            toValue = 10,
            title = stringResource(id = R.string.grid_size_option),
            onValueChange = {
                mainModel.onEvent(MainEvent.OnChangeLibraryAutoGridSize(it == 0))
                mainModel.onEvent(
                    MainEvent.OnChangeLibraryGridSize(it)
                )
            }
        )
    }
}