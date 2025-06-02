/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.repository

import ua.acclorite.book_story.core.CoverImage
import ua.acclorite.book_story.data.local.room.BookDao
import ua.acclorite.book_story.data.mapper.book.BookMapper
import ua.acclorite.book_story.data.parser.FileParser
import ua.acclorite.book_story.data.parser.TextParser
import ua.acclorite.book_story.domain.library.Book
import ua.acclorite.book_story.domain.reader.ReaderText
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.domain.service.FileProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepositoryImpl @Inject constructor(
    private val database: BookDao,
    private val bookMapper: BookMapper,
    private val fileParser: FileParser,
    private val textParser: TextParser,
    private val fileProvider: FileProvider
) : BookRepository {

    override suspend fun searchBooks(query: String): Result<List<Book>> = runCatching {
        database.searchBooks(query).map { bookMapper.toBook(it) }
    }

    override suspend fun getBook(bookId: Int): Result<Book> = runCatching {
        database.findBookById(bookId).let {
            if (it == null) throw NoSuchElementException("Couldn't get book [$bookId].")
            else bookMapper.toBook(it)
        }
    }

    override suspend fun getText(bookId: Int): Result<List<ReaderText>> {
        return getBook(bookId)
            .mapCatching { fileProvider.getFileFromBook(it).getOrThrow() }
            .mapCatching { textParser.parse(it) }
    }

    override suspend fun addBook(book: Book): Result<Unit> = runCatching {
        database.insertBook(bookMapper.toBookEntity(book))
    }

    override suspend fun updateBook(book: Book): Result<Unit> = runCatching {
        database.updateBook(bookMapper.toBookEntity(book)).also {
            if (it == 0) throw Exception("Could not update book in database.")
        }
    }

    override suspend fun deleteBook(book: Book): Result<Unit> = runCatching {
        database.deleteBook(bookMapper.toBookEntity(book)).also {
            if (it == 0) throw Exception("Could not delete book in database.")
        }
    }

    override suspend fun getDefaultCover(book: Book): Result<CoverImage?> = runCatching {
        return fileProvider.getFileFromBook(book)
            .mapCatching { fileParser.parse(it)?.coverImage }
    }
}