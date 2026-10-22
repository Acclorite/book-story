/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.storage.data_store

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import ua.acclorite.book_story.core.log.logW
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("data_store")
private const val TAG = "DataStore"

class DataStoreImpl @Inject constructor(context: Application) : DataStore {
    private val dataStore = context.dataStore

    override suspend fun <T> getNullableData(key: Preferences.Key<T>): T? {
        return try {
            dataStore.data.firstOrNull()?.get(key)
        } catch (e: Exception) {
            logW(TAG, "Could not get data for key ${key.name} with error: ${e.message}")
            null
        }
    }

    override suspend fun <T> putData(key: Preferences.Key<T>, value: T) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }
}