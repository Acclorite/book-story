/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.repository

import ua.acclorite.book_story.domain.library.book.Book
import ua.acclorite.book_story.domain.library.book.BookWithCover
import ua.acclorite.book_story.domain.reader.ReaderText
import ua.acclorite.book_story.domain.util.CoverImage

interface BookRepository {

    suspend fun getBooks(
        query: String
    ): List<Book>

    suspend fun getBooksById(
        ids: List<Int>
    ): List<Book>

    suspend fun getBookText(
        bookId: Int
    ): List<ReaderText>

    suspend fun insertBook(
        bookWithCover: BookWithCover
    )

    suspend fun updateBook(
        book: Book
    )

    suspend fun updateCoverImageOfBook(
        bookWithOldCover: Book,
        newCoverImage: CoverImage?
    )

    suspend fun deleteBooks(
        books: List<Book>
    )

    suspend fun canResetCover(
        bookId: Int
    ): Boolean

    suspend fun resetCoverImage(
        bookId: Int
    ): Boolean
}