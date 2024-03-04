package ua.acclorite.book_story.domain.use_case

import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.repository.BookRepository
import javax.inject.Inject

class FastGetBooks @Inject constructor(private val repository: BookRepository) {

    suspend fun execute(query: String): List<Book> {
        return repository.fastGetBooks(query)
    }
}