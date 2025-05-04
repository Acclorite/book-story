/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.library.tabs.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.main.MainEvent
import ua.acclorite.book_story.presentation.main.MainModel
import ua.acclorite.book_story.ui.common.components.settings.SwitchWithTitle

@Composable
fun LibraryShowCategoryTabsOption() {
    val mainModel = hiltViewModel<MainModel>()
    val state = mainModel.state.collectAsStateWithLifecycle()

    SwitchWithTitle(
        selected = state.value.libraryShowCategoryTabs,
        title = stringResource(id = R.string.show_category_tabs_option),
        onClick = {
            mainModel.onEvent(
                MainEvent.OnChangeLibraryShowCategoryTabs(
                    !state.value.libraryShowCategoryTabs
                )
            )
        }
    )
}