/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.book

import android.app.Application
import android.graphics.BitmapFactory
import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.core.log.logI
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.domain.service.CoverImageHandler
import javax.inject.Inject

class CanResetCoverImageUseCase @Inject constructor(
    private val application: Application,
    private val bookRepository: BookRepository,
    private val coverImageHandler: CoverImageHandler
) {

    suspend operator fun invoke(bookId: Int): Boolean {
        logI("Checking if can reset cover image of [$bookId].")

        bookRepository.getBook(bookId).mapCatching { book ->
            // Getting default cover image
            val defaultCoverImage = bookRepository.getDefaultCover(book).getOrThrow()
            if (defaultCoverImage == null) {
                return@mapCatching false
            }

            // Return true if current cover is null (and default is not)
            if (book.coverImage == null) {
                return@mapCatching true
            }

            // Getting compressed cover images
            val compressedDefaultCoverImage =
                coverImageHandler.compressCover(defaultCoverImage).getOrThrow()
            val compressedCoverImage = application.contentResolver
                .openInputStream(book.coverImage)
                ?.use { BitmapFactory.decodeStream(it) }

            !compressedDefaultCoverImage.sameAs(compressedCoverImage)
        }.fold(
            onSuccess = {
                logI("Can reset cover image of [$bookId]: $it.")
                return it
            },
            onFailure = {
                logE("Could not check if can reset cover image of [$bookId] with error: ${it.message}")
                return false
            }
        )
    }
}