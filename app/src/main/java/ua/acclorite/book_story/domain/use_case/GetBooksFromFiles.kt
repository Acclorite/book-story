package ua.acclorite.book_story.domain.use_case

import ua.acclorite.book_story.domain.model.NullableBook
import ua.acclorite.book_story.domain.repository.BookRepository
import java.io.File
import javax.inject.Inject

class GetBooksFromFiles @Inject constructor(
    private val repository: BookRepository
) {

    suspend fun execute(files: List<File>): List<NullableBook> {
        return repository.getBooksFromFiles(files)
    }
}