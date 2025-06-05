/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.book

import androidx.core.net.toUri
import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.core.log.logI
import ua.acclorite.book_story.core.log.logW
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.domain.service.CoverImageHandler
import javax.inject.Inject

class ResetCoverImageUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val coverImageHandler: CoverImageHandler
) {

    suspend operator fun invoke(bookId: Int): Boolean {
        logI("Resetting cover image of [$bookId].")

        bookRepository.getBook(bookId).mapCatching { book ->
            // Getting default cover image
            val defaultCoverImage = bookRepository.getDefaultCover(book).getOrThrow()
            if (defaultCoverImage == null) {
                throw NoSuchElementException("Could not find default cover image")
            }

            // Deleting old cover
            book.coverImage?.let { coverImageHandler.deleteCover(it) }?.onFailure {
                logW("Could not delete old cover image with error: ${it.message}")
            }

            // Resetting cover
            val newCoverImage = defaultCoverImage.let {
                coverImageHandler.saveCover(it).getOrThrow()
            }

            book.copy(coverImage = newCoverImage.toUri())
        }.mapCatching { newBook ->
            bookRepository.updateBook(newBook).getOrThrow()
        }.fold(
            onSuccess = {
                logI("Successfully reset cover image of [$bookId].")
                return true
            },
            onFailure = {
                logE("Could not reset cover image of [$bookId] with error: ${it.message}")
                return false
            }
        )
    }
}