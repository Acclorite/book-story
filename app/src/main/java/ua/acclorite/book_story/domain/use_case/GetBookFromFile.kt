package ua.acclorite.book_story.domain.use_case

import ua.acclorite.book_story.domain.model.NullableBook
import ua.acclorite.book_story.domain.repository.BookRepository
import java.io.File
import javax.inject.Inject

class GetBookFromFile @Inject constructor(
    private val repository: BookRepository
) {

    suspend fun execute(file: File): NullableBook {
        return repository.getBooksFromFiles(listOf(file)).first()
    }
}