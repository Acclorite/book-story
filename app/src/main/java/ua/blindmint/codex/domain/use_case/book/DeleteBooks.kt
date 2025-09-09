/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.domain.use_case.book

import ua.blindmint.codex.domain.library.book.Book
import ua.blindmint.codex.domain.repository.BookRepository
import ua.blindmint.codex.domain.repository.HistoryRepository
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