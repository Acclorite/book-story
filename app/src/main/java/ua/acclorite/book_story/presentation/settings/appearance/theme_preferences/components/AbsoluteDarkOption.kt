/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.settings.appearance.theme_preferences.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.common.components.settings.SwitchWithTitle
import ua.acclorite.book_story.ui.main.MainEvent
import ua.acclorite.book_story.ui.main.MainModel
import ua.acclorite.book_story.ui.main.model.isDark
import ua.acclorite.book_story.ui.main.model.isPureDark
import ua.acclorite.book_story.ui.theme.ExpandingTransition

@Composable
fun AbsoluteDarkOption() {
    val mainModel = hiltViewModel<MainModel>()
    val state = mainModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ExpandingTransition(
        visible = state.value.pureDark.isPureDark(context)
                && state.value.darkTheme.isDark()
    ) {
        SwitchWithTitle(
            selected = state.value.absoluteDark,
            title = stringResource(id = R.string.absolute_dark_option),
            description = stringResource(id = R.string.absolute_dark_option_desc),
            onClick = {
                mainModel.onEvent(
                    MainEvent.OnChangeAbsoluteDark(
                        !state.value.absoluteDark
                    )
                )
            }
        )
    }
}