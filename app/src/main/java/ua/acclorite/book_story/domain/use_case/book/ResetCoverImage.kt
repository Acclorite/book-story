package ua.acclorite.book_story.domain.use_case.book

import ua.acclorite.book_story.domain.repository.BookRepository
import javax.inject.Inject

class ResetCoverImage @Inject constructor(
    private val repository: BookRepository
) {

    suspend fun execute(bookId: Int): Boolean {
        return repository.resetCoverImage(bookId)
    }
}