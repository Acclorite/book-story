package ua.acclorite.book_story.domain.use_case.history

import ua.acclorite.book_story.domain.repository.BookRepository
import javax.inject.Inject

class DeleteWholeHistory @Inject constructor(private val repository: BookRepository) {

    suspend fun execute() {
        repository.deleteWholeHistory()
    }
}