package ua.acclorite.book_story.domain.use_case

import ua.acclorite.book_story.domain.model.LineWithTranslation
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.domain.util.ID
import javax.inject.Inject

class GetText @Inject constructor(private val repository: BookRepository) {

    suspend fun execute(textPath: String): Map<ID, LineWithTranslation> {
        return repository.getBookText(textPath = textPath).withIndex().associate {
            it.index to LineWithTranslation(originalLine = it.value)
        }
    }
}