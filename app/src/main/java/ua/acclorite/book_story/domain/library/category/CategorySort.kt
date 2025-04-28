/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.library.category

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.library.display.LibrarySortOrder

@Immutable
data class CategorySort(
    val categoryId: Int,
    val sortOrder: LibrarySortOrder,
    val sortOrderDescending: Boolean
)