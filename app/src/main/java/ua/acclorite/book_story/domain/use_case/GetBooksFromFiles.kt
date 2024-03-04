package ua.acclorite.book_story.domain.use_case

import kotlinx.coroutines.flow.Flow
import ua.acclorite.book_story.domain.model.NullableBook
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.util.Resource
import java.io.File
import javax.inject.Inject

class GetBooksFromFiles @Inject constructor(
    private val repository: BookRepository
) {

    suspend fun execute(files: List<File>): Flow<Resource<List<NullableBook>>> {
        return repository.getBooksFromFiles(files)
    }
}