/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.acclorite.book_story.data.local.room.BookDatabase
import ua.acclorite.book_story.data.mapper.category.CategoryMapper
import ua.acclorite.book_story.data.mapper.category_sort.CategorySortMapper
import ua.acclorite.book_story.domain.model.library.Category
import ua.acclorite.book_story.domain.model.library.CategorySort
import ua.acclorite.book_story.domain.repository.CategoryRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val database: BookDatabase,
    private val categoryMapper: CategoryMapper,
    private val categorySortMapper: CategorySortMapper,
) : CategoryRepository {

    override suspend fun addCategory(category: Category): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            database.bookDao.insertCategory(
                categoryMapper.toCategoryEntity(
                    category.copy(
                        order = database.bookDao.getCategoriesCount()
                    )
                )
            )
        }
    }

    override suspend fun getCategories(): Result<List<Category>> = runCatching {
        withContext(Dispatchers.IO) {
            database.bookDao.getCategories().map { categoryMapper.toCategory(it) }
                .sortedBy { it.order }
        }
    }

    override suspend fun updateCategory(category: Category): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            database.bookDao.updateCategoryTitle(
                id = category.id,
                title = category.title
            )
            updateOrder(getCategories().getOrThrow())
        }
    }

    override suspend fun updateOrder(categories: List<Category>): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            categories.forEachIndexed { index, category ->
                database.bookDao.updateCategoryOrder(category.id, index)
            }
        }
    }

    override suspend fun deleteCategory(category: Category): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            database.bookDao.deleteCategory(categoryMapper.toCategoryEntity(category))
            database.bookDao.deleteCategorySortEntity(category.id)

            updateOrder(getCategories().getOrThrow())
        }
    }

    override suspend fun updateCategorySorting(
        categorySort: CategorySort
    ): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            database.bookDao.updateCategorySort(
                categorySortMapper.toCategorySortEntity(categorySort)
            )
        }
    }

    override suspend fun getCategorySorting(): Result<List<CategorySort>> = runCatching {
        withContext(Dispatchers.IO) {
            database.bookDao.getCategorySortEntities().map {
                categorySortMapper.toCategorySort(it)
            }
        }
    }
}