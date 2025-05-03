/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.category

import ua.acclorite.book_story.domain.library.CategorySort
import ua.acclorite.book_story.domain.repository.CategoryRepository
import javax.inject.Inject

class GetCategorySort @Inject constructor(
    private val repository: CategoryRepository
) {

    suspend fun execute(): List<CategorySort> {
        return repository.getCategorySortEntities()
    }
}