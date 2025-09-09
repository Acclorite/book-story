/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.ui.settings

import android.os.Parcelable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import kotlinx.parcelize.Parcelize
import ua.blindmint.codex.domain.navigator.Screen
import ua.blindmint.codex.presentation.core.components.top_bar.collapsibleTopAppBarScrollBehavior
import ua.blindmint.codex.presentation.navigator.LocalNavigator
import ua.blindmint.codex.presentation.settings.browse.BrowseSettingsContent

@Parcelize
object BrowseSettingsScreen : Screen, Parcelable {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val (scrollBehavior, listState) = TopAppBarDefaults.collapsibleTopAppBarScrollBehavior()

        BrowseSettingsContent(
            listState = listState,
            scrollBehavior = scrollBehavior,
            navigateBack = {
                navigator.pop()
            }
        )
    }
}