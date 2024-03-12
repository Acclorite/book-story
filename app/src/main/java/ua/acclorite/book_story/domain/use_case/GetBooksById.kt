package ua.acclorite.book_story.domain.use_case

import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.repository.BookRepository
import javax.inject.Inject

class GetBooksById @Inject constructor(private val repository: BookRepository) {

    suspend fun execute(ids: List<Int>): List<Book> {
        return repository.getBooksById(ids)
    }
}