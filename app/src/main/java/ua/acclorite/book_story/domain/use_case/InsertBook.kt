package ua.acclorite.book_story.domain.use_case

import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.domain.util.CoverImage
import javax.inject.Inject

class InsertBook @Inject constructor(private val repository: BookRepository) {
    suspend fun execute(
        book: Book,
        coverImage: CoverImage?,
        text: List<String>
    ): Boolean {
        return repository.insertBook(book, coverImage, text)
    }
}