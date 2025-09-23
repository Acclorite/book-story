/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.model.library

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.presentation.library.model.LibrarySortOrder

@Immutable
data class Category(
    val id: Int = 0,
    val title: String,
    val order: Int = 0,
    val sortOrder: LibrarySortOrder = LibrarySortOrder.LAST_READ,
    val sortOrderDescending: Boolean = true
)