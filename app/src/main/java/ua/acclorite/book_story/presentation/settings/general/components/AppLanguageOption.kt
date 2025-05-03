/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.settings.general.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.common.components.settings.ChipsWithTitle
import ua.acclorite.book_story.presentation.common.constants.provideLanguages
import ua.acclorite.book_story.presentation.common.model.ButtonItem
import ua.acclorite.book_story.ui.main.MainEvent
import ua.acclorite.book_story.ui.main.MainModel

@Composable
fun AppLanguageOption() {
    val mainModel = hiltViewModel<MainModel>()
    val state = mainModel.state.collectAsStateWithLifecycle()

    ChipsWithTitle(
        title = stringResource(id = R.string.language_option),
        chips = provideLanguages().sortedBy { it.second }.map {
            ButtonItem(
                it.first,
                it.second,
                MaterialTheme.typography.labelLarge,
                it.first == state.value.language
            )
        }.sortedBy { it.title }
    ) {
        mainModel.onEvent(MainEvent.OnChangeLanguage(it.id))
    }
}