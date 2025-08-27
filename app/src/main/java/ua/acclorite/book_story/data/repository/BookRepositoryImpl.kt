/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.acclorite.book_story.core.CoverImage
import ua.acclorite.book_story.data.local.room.BookDatabase
import ua.acclorite.book_story.data.mapper.book.BookMapper
import ua.acclorite.book_story.data.mapper.file.FileMapper
import ua.acclorite.book_story.data.parser.FileParser
import ua.acclorite.book_story.data.parser.TextParser
import ua.acclorite.book_story.domain.model.file.File
import ua.acclorite.book_story.domain.model.library.Book
import ua.acclorite.book_story.domain.model.reader.ReaderText
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.domain.service.FileProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepositoryImpl @Inject constructor(
    private val database: BookDatabase,
    private val bookMapper: BookMapper,
    private val fileMapper: FileMapper,
    private val fileParser: FileParser,
    private val textParser: TextParser,
    private val fileProvider: FileProvider
) : BookRepository {

    override suspend fun searchBooks(query: String): Result<List<Book>> = runCatching {
        withContext(Dispatchers.IO) {
            database.bookDao.searchBooks(query).map { bookMapper.toBook(it) }
        }
    }

    override suspend fun getBook(bookId: Int): Result<Book> = runCatching {
        withContext(Dispatchers.IO) {
            database.bookDao.findBookById(bookId).let {
                if (it == null) throw NoSuchElementException("Couldn't get book [$bookId].")
                else bookMapper.toBook(it)
            }
        }
    }

    override suspend fun getText(bookId: Int): Result<List<ReaderText>> {
        return withContext(Dispatchers.IO) {
            getBook(bookId)
                .mapCatching { fileProvider.getFileFromBook(it).getOrThrow() }
                .mapCatching { textParser.parse(it) }
        }
    }

    override suspend fun getFileFromBook(bookId: Int): Result<File> {
        return withContext(Dispatchers.IO) {
            getBook(bookId)
                .mapCatching { fileProvider.getFileFromBook(it).getOrThrow() }
                .mapCatching { fileMapper.toFile(it) }
        }
    }

    override suspend fun addBook(book: Book): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            database.bookDao.insertBook(bookMapper.toBookEntity(book))
        }
    }

    override suspend fun updateBook(book: Book): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            database.bookDao.updateBook(bookMapper.toBookEntity(book)).also {
                if (it == 0) throw Exception("Could not update book in database.")
            }
        }
    }

    override suspend fun deleteBook(book: Book): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            database.bookDao.deleteBook(bookMapper.toBookEntity(book)).also {
                if (it == 0) throw Exception("Could not delete book in database.")
            }
        }
    }

    override suspend fun getDefaultCover(book: Book): Result<CoverImage?> = runCatching {
        return withContext(Dispatchers.IO) {
            fileProvider.getFileFromBook(book)
                .mapCatching { fileParser.parse(it)?.coverImage }
        }
    }
}