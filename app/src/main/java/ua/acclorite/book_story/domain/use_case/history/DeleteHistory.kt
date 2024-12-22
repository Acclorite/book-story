package ua.acclorite.book_story.domain.use_case.history

import ua.acclorite.book_story.domain.history.History
import ua.acclorite.book_story.domain.repository.HistoryRepository
import javax.inject.Inject

class DeleteHistory @Inject constructor(
    private val repository: HistoryRepository
) {

    suspend fun execute(history: History) {
        repository.deleteHistory(history)
    }
}