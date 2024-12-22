package ua.acclorite.book_story.domain.use_case.book

import ua.acclorite.book_story.domain.library.book.Book
import ua.acclorite.book_story.domain.repository.BookRepository
import javax.inject.Inject

class GetBookById @Inject constructor(
    private val repository: BookRepository
) {

    suspend fun execute(id: Int): Book? {
        return repository.getBooksById(listOf(id)).firstOrNull()
    }
}