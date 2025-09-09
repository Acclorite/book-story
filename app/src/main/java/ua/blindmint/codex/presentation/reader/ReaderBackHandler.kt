/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.reader

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import ua.blindmint.codex.presentation.core.util.LocalActivity
import ua.blindmint.codex.ui.reader.ReaderEvent

@Composable
fun ReaderBackHandler(
    leave: (ReaderEvent.OnLeave) -> Unit,
    navigateBack: () -> Unit
) {
    val activity = LocalActivity.current

    BackHandler {
        leave(
            ReaderEvent.OnLeave(
                activity = activity,
                navigate = {
                    navigateBack()
                }
            )
        )
    }
}