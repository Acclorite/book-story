/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.about.model

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.core.ui.UIText

@Immutable
data class Credit(
    val name: String,
    val source: String?,
    val credits: List<UIText>,
    val website: String?
)