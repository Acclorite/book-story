package ua.acclorite.book_story.domain.use_case

import ua.acclorite.book_story.domain.repository.BookRepository
import javax.inject.Inject

class UpdateFavoriteDirectory @Inject constructor(private val repository: BookRepository) {
    suspend fun execute(path: String) {
        return repository.updateFavoriteDirectory(path)
    }
}