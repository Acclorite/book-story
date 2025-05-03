/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.mapper.category

import ua.acclorite.book_story.data.local.dto.CategoryEntity
import ua.acclorite.book_story.domain.library.Category
import javax.inject.Inject

class CategoryMapperImpl @Inject constructor() : CategoryMapper {
    override suspend fun toCategoryEntity(category: Category): CategoryEntity {
        return CategoryEntity(
            id = category.id,
            title = category.title,
            order = category.order
        )
    }

    override suspend fun toCategory(categoryEntity: CategoryEntity): Category {
        return Category(
            id = categoryEntity.id,
            title = categoryEntity.title,
            order = categoryEntity.order
        )
    }
}