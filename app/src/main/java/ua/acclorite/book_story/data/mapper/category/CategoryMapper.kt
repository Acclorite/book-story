/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.mapper.category

import ua.acclorite.book_story.data.local.dto.CategoryEntity
import ua.acclorite.book_story.domain.model.library.Category

interface CategoryMapper {
    fun toCategoryEntity(category: Category): CategoryEntity
    fun toCategory(categoryEntity: CategoryEntity): Category
}