/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.browse

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ua.blindmint.codex.R
import ua.blindmint.codex.presentation.core.components.common.AnimatedVisibility
import ua.blindmint.codex.presentation.core.components.placeholder.EmptyPlaceholder
import ua.blindmint.codex.ui.theme.Transitions

@Composable
fun BoxScope.BrowseEmptyPlaceholder(
    filesEmpty: Boolean,
    dialogHidden: Boolean,
    isLoading: Boolean,
    isRefreshing: Boolean,
    navigateToBrowseSettings: () -> Unit
) {
    AnimatedVisibility(
        visible = !isLoading
                && dialogHidden
                && filesEmpty
                && !isRefreshing,
        modifier = Modifier.align(Alignment.Center),
        enter = Transitions.DefaultTransitionIn,
        exit = Transitions.NoExitAnimation
    ) {
        EmptyPlaceholder(
            message = stringResource(id = R.string.browse_empty),
            icon = painterResource(id = R.drawable.empty_browse),
            actionTitle = stringResource(id = R.string.set_up_scanning),
            action = {
                navigateToBrowseSettings()
            }
        )
    }
}