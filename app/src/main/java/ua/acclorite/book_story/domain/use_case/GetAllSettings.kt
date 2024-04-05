package ua.acclorite.book_story.domain.use_case

import kotlinx.coroutines.CoroutineScope
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.presentation.data.MainState
import javax.inject.Inject

class GetAllSettings @Inject constructor(
    private val repository: BookRepository
) {

    suspend fun execute(scope: CoroutineScope): MainState {
        return repository.getAllSettings(scope)
    }
}