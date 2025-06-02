/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.category

import ua.acclorite.book_story.core.logI
import ua.acclorite.book_story.domain.library.Category
import ua.acclorite.book_story.domain.repository.CategoryRepository
import javax.inject.Inject

class UpdateCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {

    suspend operator fun invoke(categoryId: Int, newTitle: String) {
        logI("Updating category [$categoryId].")

        categoryRepository.updateCategory(Category(id = categoryId, title = newTitle)).fold(
            onSuccess = {
                logI("Successfully updated [$categoryId].")
            },
            onFailure = {
                logI("Could not update [$categoryId] with error: ${it.message}")
            }
        )
    }
}