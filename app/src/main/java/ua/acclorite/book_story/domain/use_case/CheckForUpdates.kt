package ua.acclorite.book_story.domain.use_case

import ua.acclorite.book_story.data.remote.dto.LatestReleaseInfo
import ua.acclorite.book_story.domain.repository.BookRepository
import javax.inject.Inject

class CheckForUpdates @Inject constructor(private val repository: BookRepository) {

    suspend fun execute(postNotification: Boolean): LatestReleaseInfo? {
        return repository.checkForUpdates(postNotification)
    }
}