/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.book

import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.core.log.logI
import ua.acclorite.book_story.domain.model.library.Book
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.domain.repository.HistoryRepository
import javax.inject.Inject

class SearchBooksUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val historyRepository: HistoryRepository
) {

    suspend operator fun invoke(query: String): List<Book> {
        logI("Searching for books with query: \"$query\".")

        return bookRepository.searchBooks(query).fold(
            onSuccess = { books ->
                logI("Successfully found [${books.size}] books.")
                books.map { book ->
                    val history = historyRepository.getHistoryForBook(book.id).getOrNull()

                    book.copy(
                        lastOpened = history?.time
                    )
                }
            },
            onFailure = {
                logE("Could not find books with error: ${it.message}")
                emptyList()
            }
        )
    }
}