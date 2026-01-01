/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.settings

import android.os.Parcelable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import kotlinx.parcelize.Parcelize
import ua.acclorite.book_story.presentation.navigator.Screen
import ua.acclorite.book_story.presentation.start.StartScreen
import ua.acclorite.book_story.ui.common.components.top_bar.collapsibleTopAppBarScrollBehavior
import ua.acclorite.book_story.ui.common.helpers.LocalSettings
import ua.acclorite.book_story.ui.navigator.LocalNavigator
import ua.acclorite.book_story.ui.settings.SettingsContent

@Parcelize
object SettingsScreen : Screen, Parcelable {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val settings = LocalSettings.current
        val (scrollBehavior, listState) = TopAppBarDefaults.collapsibleTopAppBarScrollBehavior()

        SettingsContent(
            listState = listState,
            scrollBehavior = scrollBehavior,
            navigateToGeneralSettings = {
                navigator.push(GeneralSettingsScreen)
            },
            navigateToAppearanceSettings = {
                navigator.push(AppearanceSettingsScreen)
            },
            navigateToReaderSettings = {
                navigator.push(ReaderSettingsScreen)
            },
            navigateToLibrarySettings = {
                navigator.push(LibrarySettingsScreen)
            },
            navigateToBrowseSettings = {
                navigator.push(BrowseSettingsScreen)
            },
            navigateToStart = {
                settings.showStartScreen.update(true)
                navigator.push(StartScreen, saveInBackStack = false)
            },
            navigateBack = {
                navigator.pop()
            }
        )
    }
}