/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.category

import ua.acclorite.book_story.core.logE
import ua.acclorite.book_story.core.logI
import ua.acclorite.book_story.domain.model.library.CategorySort
import ua.acclorite.book_story.domain.repository.CategoryRepository
import javax.inject.Inject

class GetCategorySortingUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {

    suspend operator fun invoke(): List<CategorySort> {
        logI("Getting category sorting.")

        return categoryRepository.getCategorySorting().fold(
            onSuccess = {
                logI("Successfully got category sorting.")
                it
            },
            onFailure = {
                logE("Could not get category sorting with error: ${it.message}")
                emptyList()
            }
        )
    }
}