/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.settings.appearance.theme_preferences.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.blindmint.codex.R
import ua.blindmint.codex.domain.ui.ButtonItem
import ua.blindmint.codex.domain.ui.DarkTheme
import ua.blindmint.codex.presentation.core.components.settings.SegmentedButtonWithTitle
import ua.blindmint.codex.ui.main.MainEvent
import ua.blindmint.codex.ui.main.MainModel

@Composable
fun DarkThemeOption() {
    val mainModel = hiltViewModel<MainModel>()
    val state = mainModel.state.collectAsStateWithLifecycle()

    SegmentedButtonWithTitle(
        title = stringResource(id = R.string.dark_theme_option),
        buttons = DarkTheme.entries.map {
            ButtonItem(
                it.toString(),
                title = when (it) {
                    DarkTheme.OFF -> stringResource(id = R.string.dark_theme_off)
                    DarkTheme.ON -> stringResource(id = R.string.dark_theme_on)
                    DarkTheme.FOLLOW_SYSTEM -> stringResource(id = R.string.dark_theme_follow_system)
                },
                textStyle = MaterialTheme.typography.labelLarge,
                selected = it == state.value.darkTheme
            )
        }
    ) {
        mainModel.onEvent(MainEvent.OnChangeDarkTheme(it.id))
    }
}