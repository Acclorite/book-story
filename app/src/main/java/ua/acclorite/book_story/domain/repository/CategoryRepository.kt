/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.repository

import ua.acclorite.book_story.domain.library.Category
import ua.acclorite.book_story.domain.library.CategorySort

interface CategoryRepository {

    suspend fun insertCategory(
        category: Category
    )

    suspend fun getCategories(): List<Category>

    suspend fun updateCategoryTitle(
        id: Int,
        title: String
    )

    suspend fun updateCategoriesOrder(
        categories: List<Category>
    )

    suspend fun deleteCategory(
        category: Category
    )

    suspend fun updateCategorySortEntity(
        categorySort: CategorySort
    )

    suspend fun getCategorySortEntities(): List<CategorySort>
}