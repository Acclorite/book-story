/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.data_store

import androidx.datastore.preferences.core.Preferences
import ua.acclorite.book_story.domain.repository.DataStoreRepository
import javax.inject.Inject

class SetDatastore @Inject constructor(
    private val repository: DataStoreRepository
) {

    suspend fun <T> execute(key: Preferences.Key<T>, value: T) {
        repository.putDataToDataStore(key, value)
    }
}