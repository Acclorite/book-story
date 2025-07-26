/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.general.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import ua.acclorite.book_story.R
import ua.acclorite.book_story.core.data.CoreData
import ua.acclorite.book_story.presentation.settings.SettingsEvent
import ua.acclorite.book_story.presentation.settings.SettingsModel
import ua.acclorite.book_story.ui.common.components.settings.ChipsWithTitle
import ua.acclorite.book_story.ui.common.helpers.LocalSettings
import ua.acclorite.book_story.ui.common.model.ListItem

@Composable
fun AppLanguageOption() {
    val settingsModel = hiltViewModel<SettingsModel>()
    val settings = LocalSettings.current

    ChipsWithTitle(
        title = stringResource(id = R.string.language_option),
        chips = CoreData.languages.map { item ->
            ListItem(
                item = item,
                title = item.displayName,
                selected = item == settings.language.value
            )
        },
        onClick = { item ->
            settingsModel.onEvent(SettingsEvent.OnUpdateLanguage(item))
        }
    )
}