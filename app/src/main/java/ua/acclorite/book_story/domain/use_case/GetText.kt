package ua.acclorite.book_story.domain.use_case

import ua.acclorite.book_story.domain.repository.BookRepository
import javax.inject.Inject

class GetText @Inject constructor(private val repository: BookRepository) {

    suspend fun execute(id: Int): String {
        return repository.getBookTextById(bookId = id)
    }
}