/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.browse

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.SharedFlow
import ua.acclorite.book_story.R
import ua.acclorite.book_story.core.helpers.toggle
import ua.acclorite.book_story.presentation.browse.BrowseEffect
import ua.acclorite.book_story.presentation.library.LibraryScreen
import ua.acclorite.book_story.presentation.settings.BrowseSettingsScreen
import ua.acclorite.book_story.ui.common.helpers.LocalSettings
import ua.acclorite.book_story.ui.common.helpers.showToast
import ua.acclorite.book_story.ui.navigator.LocalNavigator

@Composable
fun BrowseEffects(effects: SharedFlow<BrowseEffect>, focusRequester: FocusRequester) {
    val navigator = LocalNavigator.current
    val settings = LocalSettings.current
    val context = LocalContext.current

    LaunchedEffect(effects, focusRequester, context, navigator, settings.browsePinnedPaths.value) {
        effects.collect { effect ->
            when (effect) {
                is BrowseEffect.OnRequestFocus -> {
                    focusRequester.requestFocus()
                }

                is BrowseEffect.OnBooksAdded -> {
                    context.getString(R.string.books_added)
                        .showToast(context = context)
                }

                is BrowseEffect.OnNavigateToLibrary -> {
                    navigator.push(LibraryScreen, saveInBackStack = false)
                }

                is BrowseEffect.OnUpdatePinnedPaths -> {
                    settings.browsePinnedPaths.update(
                        settings.browsePinnedPaths.lastValue.toggle(effect.path)
                    )
                }

                is BrowseEffect.OnNavigateToBrowseSettings -> {
                    navigator.push(BrowseSettingsScreen)
                }
            }
        }
    }
}