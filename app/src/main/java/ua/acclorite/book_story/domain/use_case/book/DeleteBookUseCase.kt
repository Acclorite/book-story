/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.book

import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.core.log.logI
import ua.acclorite.book_story.core.log.logW
import ua.acclorite.book_story.domain.model.library.Book
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.domain.repository.HistoryRepository
import ua.acclorite.book_story.domain.service.CoverImageHandler
import javax.inject.Inject

class DeleteBookUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val historyRepository: HistoryRepository,
    private val coverImageHandler: CoverImageHandler
) {

    suspend operator fun invoke(book: Book) {
        logI("Deleting [${book.title}].")

        // Deleting cover image
        book.coverImage?.let { coverImageHandler.deleteCover(it) }?.onFailure {
            logW("Could not delete cover image with error: ${it.message}")
        }

        // Deleting history
        historyRepository.deleteHistoryForBook(bookId = book.id).onFailure {
            logW("Could not delete history for [${book.title}] with error: ${it.message}")
        }

        // Deleting book
        bookRepository.deleteBook(book).fold(
            onSuccess = {
                logI("Successfully deleted [${book.title}].")
            },
            onFailure = {
                logE("Could not delete [${book.title}] with error: ${it.message}")
            }
        )
    }
}