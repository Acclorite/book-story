package ua.acclorite.book_story.domain.use_case

import ua.acclorite.book_story.domain.model.LanguageHistory
import ua.acclorite.book_story.domain.repository.BookRepository
import javax.inject.Inject

class UpdateLanguageHistory @Inject constructor(private val repository: BookRepository) {

    suspend fun execute(history: List<LanguageHistory>) {
        repository.updateLanguageHistory(history)
    }
}