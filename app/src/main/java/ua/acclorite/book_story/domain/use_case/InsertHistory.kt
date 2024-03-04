package ua.acclorite.book_story.domain.use_case

import ua.acclorite.book_story.domain.model.History
import ua.acclorite.book_story.domain.repository.BookRepository
import javax.inject.Inject

class InsertHistory @Inject constructor(private val repository: BookRepository) {
    suspend fun execute(history: List<History>) {
        repository.insertHistory(history)
    }
}