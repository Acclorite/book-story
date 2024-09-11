package ua.acclorite.book_story.data.local.data_store

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("data_store")

class DataStoreImpl @Inject constructor(context: Application) : DataStore {
    private val dataStore = context.dataStore

    /**
     * Gets data from DataStore by given [key]. If no such [key] exists, returns null.
     */
    override suspend fun <T> getNullableData(key: Preferences.Key<T>): T? =
        dataStore.data.catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }.map { preferences ->
            preferences[key]
        }.firstOrNull()

    /**
     * Gets all keys from DataStore.
     */
    override suspend fun getAllData(): Set<Preferences.Key<*>>? {
        return dataStore.data
            .map {
                it.asMap().keys
            }.firstOrNull()
    }

    /**
     * Puts data in DataStore by given [key].
     */
    override suspend fun <T> putData(key: Preferences.Key<T>, value: T) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }
}