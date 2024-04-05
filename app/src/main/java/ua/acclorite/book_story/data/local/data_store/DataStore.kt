package ua.acclorite.book_story.data.local.data_store

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface DataStore {
    suspend fun <T> getData(key: Preferences.Key<T>, defaultValue: T): Flow<T>
    suspend fun <T> getNullableData(key: Preferences.Key<T>): T?
    suspend fun getAllData(): Set<Preferences.Key<*>>?
    suspend fun <T> putData(key: Preferences.Key<T>, value: T)
}