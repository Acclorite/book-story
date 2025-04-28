/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.local.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import ua.acclorite.book_story.domain.library.display.LibrarySortOrder

@Entity
data class CategorySortEntity(
    @PrimaryKey(false)
    val categoryId: Int,
    val sortOrder: LibrarySortOrder,
    val sortOrderDescending: Boolean
)