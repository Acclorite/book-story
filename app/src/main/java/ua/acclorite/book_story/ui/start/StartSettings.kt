/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.start

import androidx.compose.runtime.Composable
import ua.acclorite.book_story.core.language.Language
import ua.acclorite.book_story.presentation.navigator.StackEvent
import ua.acclorite.book_story.presentation.start.StartScreen
import ua.acclorite.book_story.ui.common.model.ListItem

@Composable
fun StartSettings(
    currentPage: Int,
    stackEvent: StackEvent,
    languages: List<ListItem<Language>>,
    updateLanguage: (Language) -> Unit,
    navigateForward: () -> Unit
) {
    StartSettingsScaffold(
        navigateForward = navigateForward
    ) {
        StartContentTransition(
            targetValue = when (currentPage) {
                0 -> StartScreen.GENERAL_SETTINGS
                1 -> StartScreen.APPEARANCE_SETTINGS
                else -> StartScreen.SCAN_SETTINGS
            },
            stackEvent = stackEvent
        ) { page ->
            StartSettingsLayout {
                when (page) {
                    StartScreen.GENERAL_SETTINGS -> {
                        StartSettingsLayoutGeneral(
                            languages = languages,
                            updateLanguage = updateLanguage
                        )
                    }

                    StartScreen.APPEARANCE_SETTINGS -> {
                        StartSettingsLayoutAppearance()
                    }

                    StartScreen.SCAN_SETTINGS -> {
                        StartSettingsLayoutScan()
                    }
                }
            }
        }
    }
}