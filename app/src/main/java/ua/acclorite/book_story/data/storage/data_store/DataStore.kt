/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.storage.data_store

import androidx.datastore.preferences.core.Preferences

interface DataStore {
    suspend fun <T> getNullableData(key: Preferences.Key<T>): T?
    suspend fun <T> putData(key: Preferences.Key<T>, value: T)
}