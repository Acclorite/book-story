package com.acclorite.books_history.domain.use_case

import com.acclorite.books_history.data.repository.FakeBookRepository
import com.acclorite.books_history.domain.model.Book
import com.acclorite.books_history.domain.model.Category
import com.google.common.truth.Truth.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class InsertBooksTest {

    private lateinit var insertBooks: InsertBooks
    private lateinit var fakeRepository: FakeBookRepository

    @Before
    fun setUp() {
        fakeRepository = FakeBookRepository()
        insertBooks = InsertBooks(fakeRepository)
    }

    @Test
    fun `Insert all books, inserted book`() = runBlocking {
        val booksToInsert = mutableListOf<Book>()
        ('a'..'z').forEachIndexed { index, c ->
            booksToInsert.add(
                Book(
                    index,
                    c.toString(),
                    c.toString(),
                    c.toString(),
                    emptyList(),
                    (index / 100f),
                    null,
                    null,
                    Category.READING,
                    coverImage = null
                )
            )
        }
        booksToInsert.shuffle()

        val oldRepoSize = fakeRepository.getBooks("").first().data?.size ?: return@runBlocking
        insertBooks.execute(booksToInsert)
        val repoSize = fakeRepository.getBooks("").first().data?.size ?: return@runBlocking

        assertThat(oldRepoSize).isLessThan(repoSize)
    }
}