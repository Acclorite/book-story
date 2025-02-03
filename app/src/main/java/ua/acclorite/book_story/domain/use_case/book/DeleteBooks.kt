/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.book

import ua.acclorite.book_story.domain.library.book.Book
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.domain.repository.HistoryRepository
import javax.inject.Inject

class DeleteBooks @Inject constructor(
    private val bookRepository: BookRepository,
    private val historyRepository: HistoryRepository
) {

    suspend fun execute(books: List<Book>) {
        bookRepository.deleteBooks(books)
        books.forEach {
            historyRepository.deleteBookHistory(it.id)
        }
    }
}