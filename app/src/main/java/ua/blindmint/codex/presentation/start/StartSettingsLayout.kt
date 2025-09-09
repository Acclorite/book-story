/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.start

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ua.blindmint.codex.presentation.core.components.common.LazyColumnWithScrollbar

@Composable
fun StartSettingsLayout(
    content: LazyListScope.() -> Unit
) {
    LazyColumnWithScrollbar(Modifier.fillMaxSize()) {
        content()
    }
}