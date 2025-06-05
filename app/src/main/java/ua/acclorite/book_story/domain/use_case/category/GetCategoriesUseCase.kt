/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.category

import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.core.log.logI
import ua.acclorite.book_story.domain.model.library.Category
import ua.acclorite.book_story.domain.repository.CategoryRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {

    suspend operator fun invoke(): List<Category> {
        logI("Getting all categories.")

        return categoryRepository.getCategories().fold(
            onSuccess = {
                logI("Successfully got [${it.size}] categories.")
                it
            },
            onFailure = {
                logE("Could not get categories with error: ${it.message}")
                emptyList()
            }
        )
    }
}