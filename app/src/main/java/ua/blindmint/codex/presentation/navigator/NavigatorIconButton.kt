/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.navigator

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import ua.blindmint.codex.R
import ua.blindmint.codex.presentation.core.components.common.IconButton

@Composable
fun NavigatorIconButton(
    onClick: () -> Unit = { navigatorBottomSheetChannel.trySend(true) }
) {
    IconButton(
        icon = Icons.Default.MoreVert,
        contentDescription = R.string.more_content_desc,
        disableOnClick = false
    ) {
        onClick()
    }
}