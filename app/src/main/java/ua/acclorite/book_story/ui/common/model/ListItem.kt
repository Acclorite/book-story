/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.common.model

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle

@Immutable
data class ListItem<T>(
    val item: T,
    val title: String,
    val textStyle: @Composable () -> TextStyle = { MaterialTheme.typography.labelLarge },
    val selected: Boolean
)