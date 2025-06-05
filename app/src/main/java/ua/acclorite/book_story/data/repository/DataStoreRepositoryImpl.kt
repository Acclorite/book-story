/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.repository

import androidx.datastore.preferences.core.Preferences
import ua.acclorite.book_story.data.local.data_store.DataStore
import ua.acclorite.book_story.domain.repository.DataStoreRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Data Store repository.
 * Manages all [DataStore] related work.
 */
@Singleton
class DataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore
) : DataStoreRepository {

    override suspend fun <T> putPreference(
        key: Preferences.Key<T>,
        value: T
    ): Result<Unit> = runCatching {
        dataStore.putData(key, value)
    }

    override suspend fun <T> getPreference(
        key: Preferences.Key<T>
    ): Result<T> = runCatching {
        dataStore.getNullableData(key) ?: throw NoSuchElementException("Could not get preference.")
    }

    override suspend fun getAllPreferences(): Result<Set<Preferences.Key<*>>> = runCatching {
        dataStore.getAllData() ?: throw NoSuchElementException("Could not get all preferences.")
    }
}