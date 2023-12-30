package com.acclorite.books_history.data.local.data_store

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface DataStore {
    suspend fun <T> getData (key: Preferences.Key<T>, defaultValue: T): Flow<T>
    suspend fun <T> putData (key: Preferences.Key<T>, value: T)
}