package ua.acclorite.book_story.domain.use_case

import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.domain.util.LanguageCode
import ua.acclorite.book_story.domain.util.Resource
import javax.inject.Inject

class TranslateText @Inject constructor(
    private val repository: BookRepository
) {

    suspend fun execute(
        sourceLanguage: LanguageCode,
        targetLanguage: LanguageCode,
        text: String
    ): Resource<String> {
        return repository.translateText(
            sourceLanguage,
            targetLanguage,
            text
        )
    }
}