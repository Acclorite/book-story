/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.domain.use_case.book

import ua.blindmint.codex.domain.library.book.Book
import ua.blindmint.codex.domain.repository.BookRepository
import javax.inject.Inject

class GetBookById @Inject constructor(
    private val repository: BookRepository
) {

    suspend fun execute(id: Int): Book? {
        return repository.getBooksById(listOf(id)).firstOrNull()
    }
}