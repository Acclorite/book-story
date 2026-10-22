/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.book

import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.core.log.logI
import ua.acclorite.book_story.core.log.logW
import ua.acclorite.book_story.domain.model.book.Book
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.domain.repository.HistoryRepository
import ua.acclorite.book_story.domain.service.ImageHandler
import javax.inject.Inject

private const val TAG = "DeleteBook"

class DeleteBookUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val historyRepository: HistoryRepository,
    private val imageHandler: ImageHandler
) {

    suspend operator fun invoke(book: Book) {
        logI(TAG, "Deleting [${book.title}].")

        // Deleting cover image
        book.coverImage?.let { imageHandler.deleteCover(it) }?.onFailure {
            logW(TAG, "Could not delete cover image with error: ${it.message}")
        }

        // Deleting history
        historyRepository.deleteHistoryForBook(bookId = book.id).onFailure {
            logW(TAG, "Could not delete history for [${book.title}] with error: ${it.message}")
        }

        // Deleting book
        bookRepository.deleteBook(book).fold(
            onSuccess = {
                logI(TAG, "Successfully deleted [${book.title}].")
            },
            onFailure = {
                logE(TAG, "Could not delete [${book.title}] with error: ${it.message}")
            }
        )
    }
}