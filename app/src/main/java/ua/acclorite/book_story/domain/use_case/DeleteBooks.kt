package ua.acclorite.book_story.domain.use_case

import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.repository.BookRepository
import javax.inject.Inject

class DeleteBooks @Inject constructor(private val repository: BookRepository) {

    suspend fun execute(books: List<Book>) {
        repository.deleteBooks(books)
        books.forEach {
            repository.deleteBookHistory(it.id)
        }
    }
}