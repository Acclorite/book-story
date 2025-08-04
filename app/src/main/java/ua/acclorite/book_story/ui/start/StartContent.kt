/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.start

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ua.acclorite.book_story.core.language.Language
import ua.acclorite.book_story.presentation.navigator.StackEvent
import ua.acclorite.book_story.presentation.start.StartScreen
import ua.acclorite.book_story.ui.common.model.ListItem

@Composable
fun StartContent(
    currentPage: Int,
    stackEvent: StackEvent,
    languages: List<ListItem<Language>>,
    updateLanguage: (Language) -> Unit,
    navigateForward: () -> Unit,
    navigateBack: () -> Unit,
    navigateToBrowse: () -> Unit,
    navigateToHelp: () -> Unit
) {
    StartContentTransition(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        targetValue = when {
            currentPage in 0..2 -> StartScreen.SETTINGS
            else -> StartScreen.DONE
        },
        stackEvent = stackEvent
    ) { page ->
        when (page) {
            StartScreen.SETTINGS -> {
                StartSettings(
                    currentPage = currentPage,
                    stackEvent = stackEvent,
                    languages = languages,
                    updateLanguage = updateLanguage,
                    navigateForward = navigateForward
                )
            }

            StartScreen.DONE -> {
                StartDone(
                    navigateToBrowse = navigateToBrowse,
                    navigateToHelp = navigateToHelp
                )
            }
        }
    }

    StartBackHandler(
        navigateBack = navigateBack
    )
}