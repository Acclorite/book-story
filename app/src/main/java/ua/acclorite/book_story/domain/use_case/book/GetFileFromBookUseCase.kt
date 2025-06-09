/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.book

import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.core.log.logI
import ua.acclorite.book_story.domain.model.file.File
import ua.acclorite.book_story.domain.repository.BookRepository
import javax.inject.Inject

class GetFileFromBookUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {

    suspend operator fun invoke(bookId: Int): File? {
        logI("Getting file from book [$bookId].")

        return bookRepository.getFileFromBook(bookId).fold(
            onSuccess = {
                logI("Successfully got file from book [$bookId].")
                it
            },
            onFailure = {
                logE("Could not get file from book [$bookId] with error: ${it.message}")
                null
            }
        )
    }
}