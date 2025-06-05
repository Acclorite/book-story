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

class AddCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {

    suspend operator fun invoke(title: String) {
        logI("Inserting [${title}] category.")

        categoryRepository.addCategory(Category(title = title)).fold(
            onSuccess = {
                logI("Successfully inserted [${title}] category.")
            },
            onFailure = {
                logE("Could not insert [${title}] category with error: ${it.message}")
            }
        )
    }
}