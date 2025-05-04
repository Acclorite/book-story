/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.repository

import androidx.datastore.preferences.core.Preferences
import ua.acclorite.book_story.presentation.main.MainState

interface DataStoreRepository {

    suspend fun <T> putDataToDataStore(
        key: Preferences.Key<T>,
        value: T
    )

    suspend fun getAllSettings(): MainState
}