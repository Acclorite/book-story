package ua.acclorite.book_story.domain.use_case

import ua.acclorite.book_story.domain.model.BookWithTextAndCover
import ua.acclorite.book_story.domain.repository.BookRepository
import javax.inject.Inject

class InsertBook @Inject constructor(private val repository: BookRepository) {
    suspend fun execute(
        bookWithTextAndCover: BookWithTextAndCover
    ): Boolean {
        return repository.insertBook(bookWithTextAndCover)
    }
}