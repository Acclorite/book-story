package ua.acclorite.book_story.domain.use_case

import ua.acclorite.book_story.domain.repository.BookRepository
import javax.inject.Inject

class DeleteLanguageModel @Inject constructor(
    private val repository: BookRepository
) {

    suspend fun execute(languageCode: String): Boolean {
        return repository.deleteLanguageModel(
            languageCode = languageCode
        )
    }
}