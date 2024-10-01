package ua.acclorite.book_story.domain.use_case

import ua.acclorite.book_story.domain.model.Chapter
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.domain.util.Resource
import javax.inject.Inject

class CheckTextForUpdate @Inject constructor(private val repository: BookRepository) {

    suspend fun execute(bookId: Int): Resource<Pair<List<String>, List<Chapter>>?> {
        return repository.checkTextForUpdate(bookId)
    }
}