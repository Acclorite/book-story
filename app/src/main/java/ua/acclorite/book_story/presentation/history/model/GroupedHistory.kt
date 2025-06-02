/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.history.model

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.model.history.History

@Immutable
data class GroupedHistory(
    val title: String,
    val history: List<History>
)