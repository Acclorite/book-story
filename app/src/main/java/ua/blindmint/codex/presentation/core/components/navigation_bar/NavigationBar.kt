/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.core.components.navigation_bar

import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.blindmint.codex.domain.navigator.NavigatorItem
import ua.blindmint.codex.presentation.navigator.LocalNavigator

@Composable
fun NavigationBar(tabs: List<NavigatorItem>) {
    val navigator = LocalNavigator.current
    val lastItem = navigator.lastItem.collectAsStateWithLifecycle()

    val currentTab = remember { mutableStateOf(lastItem.value) }
    LaunchedEffect(lastItem.value) {
        if (tabs.any { it.screen::class == lastItem.value::class }) {
            currentTab.value = lastItem.value
        }
    }

    NavigationBar {
        tabs.forEach { tab ->
            NavigationBarItem(
                item = tab,
                isSelected = currentTab.value::class == tab.screen::class
            ) {
                navigator.push(tab.screen)
            }
        }
    }
}