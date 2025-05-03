/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.category

import ua.acclorite.book_story.domain.library.Category
import ua.acclorite.book_story.domain.repository.CategoryRepository
import javax.inject.Inject

class UpdateCategoryOrder @Inject constructor(
    private val repository: CategoryRepository
) {

    suspend fun execute(categories: List<Category>) {
        repository.updateCategoriesOrder(categories)
    }
}