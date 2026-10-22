/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.book

import ua.acclorite.book_story.core.data.ParsersData
import ua.acclorite.book_story.domain.model.book.Book
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.domain.service.FileHandler
import javax.inject.Inject

class IsBookContentValidUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val fileHandler: FileHandler
) {
    suspend operator fun invoke(book: Book): Result<Boolean> = runCatching {
        // 1. Check if parsed content exists
        val metadata = bookRepository.getBookContentMetadata(book).getOrNull()
            ?: return@runCatching false
        if (bookRepository.isBookContentNodesEmpty(book).getOrDefault(true)) {
            return@runCatching false
        }

        // 2. Check if file hash is the same
        val fileHash = fileHandler.getFileHashFromBook(book).getOrThrow()
        if (fileHash != metadata.fileHash) return@runCatching false

        // 3. Check if parser version is the same
        if (ParsersData.getCurrentParserVersion(metadata.parser) != metadata.parserVersion) return@runCatching false

        return@runCatching true
    }
}