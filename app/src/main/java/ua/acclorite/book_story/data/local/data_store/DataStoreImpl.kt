package ua.acclorite.book_story.data.local.data_store

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("data_store")

class DataStoreImpl @Inject constructor(context: Application) : DataStore {
    private val dataStore = context.dataStore

    override suspend fun <T> getData(key: Preferences.Key<T>, defaultValue: T): Flow<T> =
        dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val result = preferences[key] ?: defaultValue
            result
        }

    override suspend fun <T> getNullableData(key: Preferences.Key<T>): T? =
        dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val result = preferences[key]
            result
        }.firstOrNull()

    override suspend fun getAllData(): Set<Preferences.Key<*>>? {
        val keys = dataStore.data
            .map {
                it.asMap().keys
            }
        return keys.firstOrNull()
    }

    override suspend fun <T> putData(key: Preferences.Key<T>, value: T) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }
}