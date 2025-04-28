/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.repository

import ua.acclorite.book_story.data.local.room.BookDao
import ua.acclorite.book_story.data.mapper.category.CategoryMapper
import ua.acclorite.book_story.data.mapper.category_sort.CategorySortMapper
import ua.acclorite.book_story.domain.library.category.Category
import ua.acclorite.book_story.domain.library.category.CategorySort
import ua.acclorite.book_story.domain.repository.CategoryRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val database: BookDao,
    private val categoryMapper: CategoryMapper,
    private val categorySortMapper: CategorySortMapper,
) : CategoryRepository {

    override suspend fun insertCategory(category: Category) {
        database.insertCategory(
            categoryMapper.toCategoryEntity(
                category.copy(
                    order = database.getCategoriesCount()
                )
            )
        )
    }

    override suspend fun getCategories(): List<Category> {
        return database.getCategories().map { categoryMapper.toCategory(it) }.sortedBy { it.order }
    }

    override suspend fun updateCategoryTitle(id: Int, title: String) {
        database.updateCategoryTitle(
            id = id,
            title = title
        )
        updateCategoriesOrder(getCategories())
    }

    override suspend fun updateCategoriesOrder(categories: List<Category>) {
        categories.mapIndexed { index, category ->
            database.updateCategoryOrder(category.id, index)
        }
    }

    override suspend fun deleteCategory(category: Category) {
        database.deleteCategory(categoryMapper.toCategoryEntity(category))
        database.deleteCategorySortEntity(category.id)

        updateCategoriesOrder(getCategories())
    }

    override suspend fun updateCategorySortEntity(categorySort: CategorySort) {
        database.updateCategorySort(
            categorySortMapper.toCategorySortEntity(categorySort)
        )
    }

    override suspend fun getCategorySortEntities(): List<CategorySort> {
        return database.getCategorySortEntities().map {
            categorySortMapper.toCategorySort(it)
        }
    }
}