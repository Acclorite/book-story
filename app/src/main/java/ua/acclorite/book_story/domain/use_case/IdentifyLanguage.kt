package ua.acclorite.book_story.domain.use_case

import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.domain.util.LanguageCode
import ua.acclorite.book_story.domain.util.Resource
import javax.inject.Inject

class IdentifyLanguage @Inject constructor(
    private val repository: BookRepository
) {

    suspend fun execute(text: String): Resource<LanguageCode> {
        return repository.identifyLanguage(text)
    }
}