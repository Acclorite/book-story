/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.repository

import androidx.datastore.preferences.core.Preferences

interface DataStoreRepository {
    suspend fun <T> putPreference(
        key: Preferences.Key<T>,
        value: T
    ): Result<Unit>

    suspend fun <T> getPreference(
        key: Preferences.Key<T>,
    ): Result<T>

    suspend fun getAllPreferences(): Result<Set<Preferences.Key<*>>>
}