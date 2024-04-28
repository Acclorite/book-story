package ua.acclorite.book_story.domain.use_case

import ua.acclorite.book_story.domain.model.StringWithId
import ua.acclorite.book_story.domain.repository.BookRepository
import javax.inject.Inject

class GetText @Inject constructor(private val repository: BookRepository) {

    suspend fun execute(textPath: String): List<StringWithId> {
        return repository.getBookText(textPath = textPath)
    }
}