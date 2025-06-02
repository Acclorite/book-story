/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.repository

import ua.acclorite.book_story.data.local.room.BookDao
import ua.acclorite.book_story.data.mapper.category.CategoryMapper
import ua.acclorite.book_story.data.mapper.category_sort.CategorySortMapper
import ua.acclorite.book_story.domain.library.Category
import ua.acclorite.book_story.domain.library.CategorySort
import ua.acclorite.book_story.domain.repository.CategoryRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val database: BookDao,
    private val categoryMapper: CategoryMapper,
    private val categorySortMapper: CategorySortMapper,
) : CategoryRepository {

    override suspend fun addCategory(category: Category): Result<Unit> = runCatching {
        database.insertCategory(
            categoryMapper.toCategoryEntity(
                category.copy(
                    order = database.getCategoriesCount()
                )
            )
        )
    }

    override suspend fun getCategories(): Result<List<Category>> = runCatching {
        database.getCategories().map { categoryMapper.toCategory(it) }.sortedBy { it.order }
    }

    override suspend fun updateCategory(category: Category): Result<Unit> = runCatching {
        database.updateCategoryTitle(
            id = category.id,
            title = category.title
        )
        updateOrder(getCategories().getOrThrow())
    }

    override suspend fun updateOrder(categories: List<Category>): Result<Unit> = runCatching {
        categories.forEachIndexed { index, category ->
            database.updateCategoryOrder(category.id, index)
        }
    }

    override suspend fun deleteCategory(category: Category): Result<Unit> = runCatching {
        database.deleteCategory(categoryMapper.toCategoryEntity(category))
        database.deleteCategorySortEntity(category.id)

        updateOrder(getCategories().getOrThrow())
    }

    override suspend fun updateCategorySorting(
        categorySort: CategorySort
    ): Result<Unit> = runCatching {
        database.updateCategorySort(
            categorySortMapper.toCategorySortEntity(categorySort)
        )
    }

    override suspend fun getCategorySorting(): Result<List<CategorySort>> = runCatching {
        database.getCategorySortEntities().map {
            categorySortMapper.toCategorySort(it)
        }
    }
}