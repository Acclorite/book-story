/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.book

import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.domain.model.book.Book
import ua.acclorite.book_story.domain.model.book.Node
import ua.acclorite.book_story.domain.repository.BookRepository
import javax.inject.Inject

private const val TAG = "GetBookContentNodeByOffset"

class GetBookContentNodeByOffsetUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(book: Book, offset: Int): Node? {
        return bookRepository.getBookContentNodeByOffset(book, offset).fold(
            onSuccess = { it },
            onFailure = {
                logE(TAG, "Could not get node by offset [$offset] with error: ${it.message}")
                null
            }
        )
    }
}
