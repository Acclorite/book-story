package ua.acclorite.book_story.domain.use_case

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import ua.acclorite.book_story.domain.repository.BookRepository
import javax.inject.Inject

class GetDatastore @Inject constructor(private val repository: BookRepository) {

    suspend fun <T> execute(key: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return repository.retrieveDataFromDataStore(key, defaultValue)
    }
}