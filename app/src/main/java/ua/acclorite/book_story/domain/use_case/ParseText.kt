package ua.acclorite.book_story.domain.use_case

import ua.acclorite.book_story.domain.model.ChapterWithText
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.domain.util.Resource
import java.io.File
import javax.inject.Inject

class ParseText @Inject constructor(private val repository: BookRepository) {

    suspend fun execute(file: File): Resource<List<ChapterWithText>> {
        return repository.parseText(file)
    }
}