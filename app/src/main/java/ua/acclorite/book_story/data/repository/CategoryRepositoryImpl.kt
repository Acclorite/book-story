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
import ua.acclorite.book_story.domain.model.library.Category
import ua.acclorite.book_story.domain.repository.CategoryRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val database: BookDatabase,
    private val categoryMapper: CategoryMapper
) : CategoryRepository {

    override suspend fun addCategory(category: Category): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            database.categoryDao.insertCategory(
                categoryMapper.toCategoryEntity(
                    category.copy(
                        order = database.categoryDao.getCategoriesCount()
                    )
                )
            )
        }
    }

    override suspend fun getCategories(): Result<List<Category>> = runCatching {
        withContext(Dispatchers.IO) {
            database.categoryDao.getCategories().map { categoryMapper.toCategory(it) }
        }
    }

    override suspend fun updateCategory(category: Category): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            database.categoryDao.updateCategory(
                category = categoryMapper.toCategoryEntity(
                    if (category.id != -1) category else category.copy(
                        title = "",
                        order = -1
                    )
                )
            )
            updateOrder(getCategories().getOrThrow())
        }
    }

    override suspend fun updateOrder(categories: List<Category>): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            categories.forEachIndexed { index, category ->
                if (category.id == -1) return@forEachIndexed
                database.categoryDao.updateCategory(
                    categoryMapper.toCategoryEntity(
                        category.copy(order = index)
                    )
                )
            }
        }
    }

    override suspend fun deleteCategory(category: Category): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            if (category.id == -1) throw IllegalArgumentException("Id should not be -1.")
            database.categoryDao.deleteCategory(categoryMapper.toCategoryEntity(category))
            updateOrder(getCategories().getOrThrow())
        }
    }
}