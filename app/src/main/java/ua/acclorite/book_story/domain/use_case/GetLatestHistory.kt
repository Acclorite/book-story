package ua.acclorite.book_story.domain.use_case

import ua.acclorite.book_story.domain.model.History
import ua.acclorite.book_story.domain.repository.BookRepository
import javax.inject.Inject

class GetLatestHistory @Inject constructor(private val repository: BookRepository) {

    suspend fun execute(bookId: Int): History? {
        return repository.getLatestBookHistory(bookId)
    }
}