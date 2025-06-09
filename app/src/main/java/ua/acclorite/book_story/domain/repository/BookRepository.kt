/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.repository

import ua.acclorite.book_story.core.CoverImage
import ua.acclorite.book_story.domain.model.file.File
import ua.acclorite.book_story.domain.model.library.Book
import ua.acclorite.book_story.domain.model.reader.ReaderText

interface BookRepository {
    suspend fun searchBooks(
        query: String
    ): Result<List<Book>>

    suspend fun getBook(
        bookId: Int
    ): Result<Book>

    suspend fun getText(
        bookId: Int
    ): Result<List<ReaderText>>

    suspend fun getFileFromBook(
        bookId: Int
    ): Result<File>

    suspend fun addBook(
        book: Book
    ): Result<Unit>

    suspend fun updateBook(
        book: Book
    ): Result<Unit>

    suspend fun deleteBook(
        book: Book
    ): Result<Unit>

    suspend fun getDefaultCover(
        book: Book
    ): Result<CoverImage?>
}