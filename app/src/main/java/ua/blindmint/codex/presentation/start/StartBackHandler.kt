/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.start

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
fun StartBackHandler(
    navigateBack: () -> Unit
) {
    BackHandler {
        navigateBack()
    }
}