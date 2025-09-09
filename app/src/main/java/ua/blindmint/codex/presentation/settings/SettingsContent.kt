/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.settings

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    listState: LazyListState,
    scrollBehavior: TopAppBarScrollBehavior,
    navigateToAppearanceSettings: () -> Unit,
    navigateToReaderSettings: () -> Unit,
    navigateToBrowseSettings: () -> Unit,
    navigateBack: () -> Unit
) {
    SettingsScaffold(
        listState = listState,
        scrollBehavior = scrollBehavior,
        navigateToAppearanceSettings = navigateToAppearanceSettings,
        navigateToReaderSettings = navigateToReaderSettings,
        navigateToBrowseSettings = navigateToBrowseSettings,
        navigateBack = navigateBack
    )
}