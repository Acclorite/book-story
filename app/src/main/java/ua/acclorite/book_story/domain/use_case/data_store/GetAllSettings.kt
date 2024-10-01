package ua.acclorite.book_story.domain.use_case.data_store

import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.presentation.data.MainState
import javax.inject.Inject

class GetAllSettings @Inject constructor(
    private val repository: BookRepository
) {

    suspend fun execute(): MainState {
        return repository.getAllSettings()
    }
}