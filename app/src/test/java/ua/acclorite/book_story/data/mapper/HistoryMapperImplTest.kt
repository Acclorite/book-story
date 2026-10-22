/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.mapper

import junit.framework.TestCase.assertEquals
import org.junit.Test
import ua.acclorite.book_story.core.ui.UIText
import ua.acclorite.book_story.data.mapper.book.BookMapperImpl
import ua.acclorite.book_story.data.mapper.history.HistoryMapperImpl
import ua.acclorite.book_story.data.storage.room.entity.BookEntity
import ua.acclorite.book_story.data.storage.room.entity.HistoryEntity
import ua.acclorite.book_story.data.storage.room.entity.HistoryWithBook
import ua.acclorite.book_story.domain.model.book.Book
import ua.acclorite.book_story.domain.model.book.BookProgress
import ua.acclorite.book_story.domain.model.history.History
import kotlin.test.assertFailsWith

class HistoryMapperImplTest {

    private val bookMapper = BookMapperImpl()
    private val historyMapper = HistoryMapperImpl(bookMapper)

    // TODO: Tests for mappers

    @Test
    fun `History should survive round trip`() {
        val book = Book(
            title = "Test title",
            author = UIText.StringValue("Test author"),
            description = "Test description",
            filePath = "a/b/c",
            coverImage = null,
            progress = BookProgress.default,
            lastOpened = null,
            categories = listOf(1)
        )
        val history = History(
            id = 5,
            book = book,
            time = 123
        )

        val historyEntity = historyMapper.toHistoryEntity(history)
        val bookEntity = bookMapper.toBookEntity(book)

        val result = historyMapper.toHistory(HistoryWithBook(historyEntity, bookEntity))

        assertEquals(history, result)
    }

    @Test
    fun `Incorrect History should throw`() {
        val book = Book(
            title = "Test title",
            author = UIText.StringValue("Test author"),
            description = "Test description",
            filePath = "a/b/c",
            coverImage = null,
            progress = BookProgress.default,
            lastOpened = null,
            categories = listOf(1)
        )
        val history = History(
            id = -2, // ID is less than 0
            book = book,
            time = 123
        )

        val exception = assertFailsWith<IllegalStateException> {
            historyMapper.toHistoryEntity(history)
        }

        assertEquals("Incorrect History instance.", exception.message)
    }

    @Test
    fun `Incorrect HistoryWithBook should throw`() {
        val book = BookEntity(
            id = 0,
            title = "Test title",
            author = "Test author",
            description = "Test description",
            filePath = "a/b/c",
            categories = emptyList(),
            uuid = "123",
            currentNodeId = 0,
            currentNodeIntraOffset = 33,
            totalNodes = 5,
            currentOffset = 4,
            totalLength = 400,
            image = null,
            scrollIndex = -1,
            scrollOffset = -1,
            progress = -1f
        )
        val history = HistoryEntity(
            id = 2,
            bookId = 5, // bookId does not match BookEntity's ID
            time = 123
        )

        val exception = assertFailsWith<IllegalStateException> {
            historyMapper.toHistory(HistoryWithBook(history, book))
        }

        assertEquals("Incorrect HistoryWithBook instance.", exception.message)
    }
}