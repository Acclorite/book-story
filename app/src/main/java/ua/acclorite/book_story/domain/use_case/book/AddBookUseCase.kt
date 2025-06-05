/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.book

import androidx.core.net.toUri
import ua.acclorite.book_story.core.CoverImage
import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.core.log.logI
import ua.acclorite.book_story.core.log.logW
import ua.acclorite.book_story.domain.model.library.Book
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.domain.service.CoverImageHandler
import javax.inject.Inject

class AddBookUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val coverImageHandler: CoverImageHandler
) {

    suspend operator fun invoke(book: Book, coverImage: CoverImage?) {
        logI("Inserting [${book.title}].")

        val coverImageUri = coverImage?.let {
            coverImageHandler.saveCover(it).fold(
                onSuccess = {
                    logI("Successfully saved cover image of [${book.title}].")
                    it.toUri()
                },
                onFailure = {
                    logW("Could not save cover image with error: ${it.message}")
                    null
                }
            )
        }

        bookRepository.addBook(book = book.copy(coverImage = coverImageUri)).fold(
            onSuccess = {
                logI("Successfully inserted [${book.title}].")
            },
            onFailure = {
                logE("Could not insert [${book.title}] with error: ${it.message}")
            }
        )
    }
}