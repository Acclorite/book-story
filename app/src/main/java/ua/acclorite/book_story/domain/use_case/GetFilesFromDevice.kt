package ua.acclorite.book_story.domain.use_case

import kotlinx.coroutines.flow.Flow
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.domain.util.Resource
import java.io.File
import javax.inject.Inject

class GetFilesFromDevice @Inject constructor(
    private val repository: BookRepository
) {

    suspend fun execute(query: String): Flow<Resource<List<File>>> {
        return repository.getFilesFromDevice(query)
    }
}