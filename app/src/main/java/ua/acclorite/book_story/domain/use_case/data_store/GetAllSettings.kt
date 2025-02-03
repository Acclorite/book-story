/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.data_store

import ua.acclorite.book_story.domain.repository.DataStoreRepository
import ua.acclorite.book_story.ui.main.MainState
import javax.inject.Inject

class GetAllSettings @Inject constructor(
    private val repository: DataStoreRepository
) {

    suspend fun execute(): MainState {
        return repository.getAllSettings()
    }
}