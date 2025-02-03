/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.ui

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle

@Immutable
data class ButtonItem(
    val id: String,
    val title: String,
    val textStyle: TextStyle,
    val selected: Boolean
)
