package ua.acclorite.book_story.domain.use_case

import kotlinx.coroutines.flow.Flow
import ua.acclorite.book_story.domain.model.StringWithId
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.util.Resource
import java.io.File
import javax.inject.Inject

class GetText @Inject constructor(private val repository: BookRepository) {

    suspend fun execute(file: File): Flow<Resource<List<StringWithId>>> {
        return repository.getBookTextFromFile(file)
    }
}