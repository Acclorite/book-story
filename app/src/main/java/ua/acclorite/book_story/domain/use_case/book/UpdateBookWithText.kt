package ua.acclorite.book_story.domain.use_case.book

import ua.acclorite.book_story.domain.library.book.BookWithText
import ua.acclorite.book_story.domain.repository.BookRepository
import javax.inject.Inject

class UpdateBookWithText @Inject constructor(
    private val repository: BookRepository
) {

    suspend fun execute(bookWithText: BookWithText): Boolean {
        return repository.updateBookWithText(bookWithText)
    }
}