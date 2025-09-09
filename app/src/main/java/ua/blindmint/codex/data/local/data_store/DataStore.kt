/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.data.local.data_store

import androidx.datastore.preferences.core.Preferences

interface DataStore {
    suspend fun <T> getNullableData(key: Preferences.Key<T>): T?
    suspend fun getAllData(): Set<Preferences.Key<*>>?
    suspend fun <T> putData(key: Preferences.Key<T>, value: T)
}