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

class UpdateCategoriesOrderUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {

    suspend operator fun invoke(categories: List<Category>) {
        logI("Updating order of [${categories.size}] categories.")

        categoryRepository.updateOrder(categories).fold(
            onSuccess = {
                logI("Successfully updated order of [${categories.size}] categories.")
            },
            onFailure = {
                logI("Could not update order of [${categories.size}] categories with error: ${it.message}")
            }
        )
    }
}