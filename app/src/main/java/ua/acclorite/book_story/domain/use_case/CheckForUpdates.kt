package ua.acclorite.book_story.domain.use_case

import ua.acclorite.book_story.data.remote.dto.ReleaseResponse
import ua.acclorite.book_story.domain.repository.BookRepository
import javax.inject.Inject

class CheckForUpdates @Inject constructor(private val repository: BookRepository) {

    suspend fun execute(postNotification: Boolean): ReleaseResponse? {
        return repository.checkForUpdates(postNotification)
    }
}