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
import javax.inject.Inject

class UpdateBookUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {

    suspend operator fun invoke(book: Book) {
        logI("Updating [${book.title}].")

        bookRepository.updateBook(book).fold(
            onSuccess = {
                logI("Successfully updated [${book.title}].")
            },
            onFailure = {
                logE("Could not update [${book.title}] with error: ${it.message}")
            }
        )
    }
}