/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.book

import ua.acclorite.book_story.domain.reader.ReaderText
import ua.acclorite.book_story.domain.repository.BookRepository
import javax.inject.Inject

class GetText @Inject constructor(
    private val repository: BookRepository
) {

    suspend fun execute(bookId: Int): List<ReaderText> {
        return repository.getBookText(bookId = bookId)
    }
}