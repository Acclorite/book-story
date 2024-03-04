package ua.acclorite.book_story.domain.use_case

import androidx.datastore.preferences.core.Preferences
import ua.acclorite.book_story.domain.repository.BookRepository
import javax.inject.Inject

class SetDatastore @Inject constructor(private val repository: BookRepository) {

    suspend fun <T> execute(key: Preferences.Key<T>, value: T) {
        repository.putDataToDataStore(key, value)
    }
}