/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.start

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextStyle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import ua.acclorite.book_story.domain.navigator.Screen
import ua.acclorite.book_story.domain.navigator.StackEvent
import ua.acclorite.book_story.domain.ui.ButtonItem
import ua.acclorite.book_story.presentation.core.constants.provideLanguages
import ua.acclorite.book_story.presentation.core.util.LocalActivity
import ua.acclorite.book_story.presentation.navigator.LocalNavigator
import ua.acclorite.book_story.presentation.start.StartContent
import ua.acclorite.book_story.ui.browse.BrowseScreen
import ua.acclorite.book_story.ui.help.HelpScreen
import ua.acclorite.book_story.ui.main.MainEvent
import ua.acclorite.book_story.ui.main.MainModel

@Parcelize
object StartScreen : Screen, Parcelable {

    @IgnoredOnParcel
    const val SETTINGS = "settings"

    @IgnoredOnParcel
    const val GENERAL_SETTINGS = "general_settings"

    @IgnoredOnParcel
    const val APPEARANCE_SETTINGS = "appearance_settings"

    @IgnoredOnParcel
    const val SCAN_SETTINGS = "scan_settings"

    @IgnoredOnParcel
    const val DONE = "done"

    @SuppressLint("InlinedApi")
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val mainModel = hiltViewModel<MainModel>()

        val mainState = mainModel.state.collectAsStateWithLifecycle()

        val activity = LocalActivity.current

        val currentPage = remember { mutableIntStateOf(0) }
        val stackEvent = remember { mutableStateOf(StackEvent.Default) }

        val languages = remember(mainState.value.language) {
            provideLanguages().sortedBy { it.second }.map {
                ButtonItem(
                    id = it.first,
                    title = it.second,
                    textStyle = TextStyle(),
                    selected = it.first == mainState.value.language
                )
            }.sortedBy { it.title }
        }

        StartContent(
            currentPage = currentPage.intValue,
            stackEvent = stackEvent.value,
            languages = languages,
            changeLanguage = mainModel::onEvent,
            navigateForward = {
                if (currentPage.intValue + 1 == 4) {
                    return@StartContent
                }

                stackEvent.value = StackEvent.Default
                currentPage.intValue += 1
            },
            navigateBack = {
                if ((currentPage.intValue - 1) < 0) {
                    activity.finish()
                } else {
                    stackEvent.value = StackEvent.Pop
                    currentPage.intValue -= 1
                }
            },
            navigateToBrowse = {
                navigator.push(
                    BrowseScreen,
                    saveInBackStack = false
                )
                BrowseScreen.refreshListChannel.trySend(Unit)
                mainModel.onEvent(MainEvent.OnChangeShowStartScreen(false))
            },
            navigateToHelp = {
                navigator.push(
                    HelpScreen(fromStart = true),
                    saveInBackStack = false
                )
            }
        )
    }
}