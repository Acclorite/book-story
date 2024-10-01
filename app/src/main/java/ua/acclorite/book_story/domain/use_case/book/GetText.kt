package ua.acclorite.book_story.domain.use_case.book

import androidx.compose.ui.text.AnnotatedString
import ua.acclorite.book_story.domain.repository.BookRepository
import javax.inject.Inject

class GetText @Inject constructor(private val repository: BookRepository) {

    suspend fun execute(textPath: String): List<AnnotatedString> {
        return repository.getBookText(textPath = textPath)
    }
}