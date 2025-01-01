package ua.acclorite.book_story.domain.use_case.book

import ua.acclorite.book_story.domain.library.book.BookWithCover
import ua.acclorite.book_story.domain.repository.BookRepository
import javax.inject.Inject

class InsertBook @Inject constructor(
    private val repository: BookRepository
) {

    suspend fun execute(bookWithCover: BookWithCover): Boolean {
        return repository.insertBook(bookWithCover)
    }
}