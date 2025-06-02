/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.category

import ua.acclorite.book_story.core.logE
import ua.acclorite.book_story.core.logI
import ua.acclorite.book_story.domain.library.CategorySort
import ua.acclorite.book_story.domain.repository.CategoryRepository
import javax.inject.Inject

class UpdateCategorySortingUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {

    suspend operator fun invoke(categorySort: CategorySort) {
        logI("Updating category sorting of [${categorySort.categoryId}].")

        categoryRepository.updateCategorySorting(categorySort).fold(
            onSuccess = {
                logI("Successfully updated category sorting of [${categorySort.categoryId}].")
            },
            onFailure = {
                logE(
                    "Could not update category sorting of [${categorySort.categoryId}] with error: ${it.message}"
                )
            }
        )
    }
}