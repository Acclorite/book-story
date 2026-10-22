/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.repository

import kotlinx.coroutines.flow.Flow
import ua.acclorite.book_story.core.CoverImage
import ua.acclorite.book_story.data.parser.content.ContentParserState
import ua.acclorite.book_story.domain.model.book.Book
import ua.acclorite.book_story.domain.model.book.Chapter
import ua.acclorite.book_story.domain.model.book.Metadata
import ua.acclorite.book_story.domain.model.book.Node
import ua.acclorite.book_story.domain.model.book.NodeId
import ua.acclorite.book_story.domain.model.file.File
import ua.acclorite.book_story.domain.model.file.FilePath

interface BookRepository {
    suspend fun searchBooks(
        query: String
    ): Result<List<Book>>

    suspend fun getBook(
        bookId: Int
    ): Result<Book>

    suspend fun getBookContentMetadata(
        book: Book
    ): Result<Metadata>

    suspend fun getBookContentChapters(
        book: Book
    ): Result<List<Chapter>>

    suspend fun getBookContentNodesInRange(
        book: Book,
        nodeId: NodeId,
        range: Int
    ): Result<List<Node>>

    suspend fun getBookContentNodesAfter(
        book: Book,
        nodeId: NodeId,
        limit: Int
    ): Result<List<Node>>

    suspend fun getBookContentNodesBefore(
        book: Book,
        nodeId: NodeId,
        limit: Int
    ): Result<List<Node>>

    suspend fun getBookContentNodeByOffset(
        book: Book,
        offset: Int
    ): Result<Node?>

    suspend fun isBookContentNodesEmpty(
        book: Book
    ): Result<Boolean>

    suspend fun saveBookContentImage(
        book: Book,
        imagePath: FilePath,
        id: Int
    ): Result<FilePath>

    suspend fun saveBookContentNodes(
        book: Book,
        nodes: List<Node>
    ): Result<Unit>

    suspend fun saveBookContentChapters(
        book: Book,
        chapters: List<Chapter>
    ): Result<Unit>

    suspend fun saveBookContentMetadata(
        book: Book,
        metadata: Metadata
    ): Result<Unit>

    suspend fun clearBookContentImages(book: Book): Result<Unit>

    suspend fun clearBookContent(
        book: Book
    ): Result<Unit>

    suspend fun parseBookContent(
        book: Book
    ): Result<Flow<ContentParserState>>

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
