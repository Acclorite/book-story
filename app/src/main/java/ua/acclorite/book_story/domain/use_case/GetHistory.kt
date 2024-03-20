package ua.acclorite.book_story.domain.use_case

import kotlinx.coroutines.flow.Flow
import ua.acclorite.book_story.domain.model.History
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.domain.util.Resource
import javax.inject.Inject

class GetHistory @Inject constructor(private val repository: BookRepository) {

    suspend fun execute(): Flow<Resource<List<History>>> {
        return repository.getHistory()
    }
}