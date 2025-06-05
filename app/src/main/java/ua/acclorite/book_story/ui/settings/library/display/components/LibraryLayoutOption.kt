/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.library.display.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.library.model.LibraryLayout
import ua.acclorite.book_story.presentation.main.MainEvent
import ua.acclorite.book_story.presentation.main.MainModel
import ua.acclorite.book_story.ui.common.components.settings.SegmentedButtonWithTitle
import ua.acclorite.book_story.ui.common.model.ButtonItem

@Composable
fun LibraryLayoutOption() {
    val mainModel = hiltViewModel<MainModel>()
    val state = mainModel.state.collectAsStateWithLifecycle()

    SegmentedButtonWithTitle(
        title = stringResource(id = R.string.layout_option),
        buttons = LibraryLayout.entries.map {
            ButtonItem(
                it.toString(),
                when (it) {
                    LibraryLayout.LIST -> stringResource(id = R.string.layout_list)
                    LibraryLayout.GRID -> stringResource(id = R.string.layout_grid)
                },
                MaterialTheme.typography.labelLarge,
                it == state.value.libraryLayout
            )
        }
    ) {
        mainModel.onEvent(
            MainEvent.OnChangeLibraryLayout(
                it.id
            )
        )
    }
}