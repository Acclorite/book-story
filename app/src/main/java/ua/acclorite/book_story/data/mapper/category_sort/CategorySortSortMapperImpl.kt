/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.mapper.category_sort

import ua.acclorite.book_story.data.local.dto.CategorySortEntity
import ua.acclorite.book_story.domain.library.CategorySort
import javax.inject.Inject

class CategorySortSortMapperImpl @Inject constructor() : CategorySortMapper {
    override suspend fun toCategorySortEntity(categorySort: CategorySort): CategorySortEntity {
        return CategorySortEntity(
            categoryId = categorySort.categoryId,
            sortOrder = categorySort.sortOrder,
            sortOrderDescending = categorySort.sortOrderDescending
        )
    }

    override suspend fun toCategorySort(categorySortEntity: CategorySortEntity): CategorySort {
        return CategorySort(
            categoryId = categorySortEntity.categoryId,
            sortOrder = categorySortEntity.sortOrder,
            sortOrderDescending = categorySortEntity.sortOrderDescending
        )
    }
}