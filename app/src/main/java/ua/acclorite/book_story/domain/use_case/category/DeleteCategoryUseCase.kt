/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.category

import ua.acclorite.book_story.core.log.logI
import ua.acclorite.book_story.domain.model.library.Category
import ua.acclorite.book_story.domain.repository.CategoryRepository
import javax.inject.Inject

class DeleteCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {

    suspend operator fun invoke(category: Category) {
        logI("Deleting category [${category.id}].")

        categoryRepository.deleteCategory(category).fold(
            onSuccess = {
                logI("Successfully deleted category [${category.id}].")
            },
            onFailure = {
                logI("Could not delete category [${category.id}] with error: ${it.message}")
            }
        )
    }
}