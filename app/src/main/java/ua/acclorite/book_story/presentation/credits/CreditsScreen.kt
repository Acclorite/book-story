/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.credits

import android.os.Parcelable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.parcelize.Parcelize
import ua.acclorite.book_story.presentation.about.AboutModel
import ua.acclorite.book_story.presentation.navigator.Screen
import ua.acclorite.book_story.ui.about.AboutEffects
import ua.acclorite.book_story.ui.common.components.top_bar.collapsibleTopAppBarScrollBehavior
import ua.acclorite.book_story.ui.credits.CreditsContent

@Parcelize
object CreditsScreen : Screen, Parcelable {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val screenModel = hiltViewModel<AboutModel>()

        val (scrollBehavior, listState) = TopAppBarDefaults.collapsibleTopAppBarScrollBehavior()

        AboutEffects(screenModel.effects)

        CreditsContent(
            scrollBehavior = scrollBehavior,
            listState = listState,
            navigateToBrowserPage = screenModel::onEvent,
            navigateBack = screenModel::onEvent
        )
    }
}