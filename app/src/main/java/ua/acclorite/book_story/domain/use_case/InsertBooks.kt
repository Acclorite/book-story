package ua.acclorite.book_story.domain.use_case

import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.domain.util.CoverImage
import javax.inject.Inject

class InsertBooks @Inject constructor(private val repository: BookRepository) {
    suspend fun execute(books: List<Pair<Book, CoverImage?>>): Boolean {
        return repository.insertBooks(books)
    }
}