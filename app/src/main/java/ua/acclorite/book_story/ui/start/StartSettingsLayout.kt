/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.start

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ua.acclorite.book_story.ui.common.components.common.LazyColumnWithScrollbar

@Composable
fun StartSettingsLayout(
    content: LazyListScope.() -> Unit
) {
    LazyColumnWithScrollbar(Modifier.fillMaxSize()) {
        content()
    }
}