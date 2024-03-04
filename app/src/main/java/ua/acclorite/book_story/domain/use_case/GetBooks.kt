package ua.acclorite.book_story.domain.use_case

import kotlinx.coroutines.flow.Flow
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.util.Resource
import javax.inject.Inject

class GetBooks @Inject constructor(private val repository: BookRepository) {

    suspend fun execute(query: String): Flow<Resource<List<Book>>> {
        return repository.getBooks(query)
    }
}