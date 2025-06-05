/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.book

import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.core.log.logI
import ua.acclorite.book_story.domain.model.reader.ReaderText
import ua.acclorite.book_story.domain.repository.BookRepository
import javax.inject.Inject

class GetTextUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {

    suspend operator fun invoke(bookId: Int): List<ReaderText> {
        logI("Getting text from: [$bookId].")
        if (bookId == -1) return emptyList()

        bookRepository.getText(bookId).mapCatching { readerText ->
            if (
                readerText.filterIsInstance<ReaderText.Text>().isEmpty() ||
                readerText.filterIsInstance<ReaderText.Chapter>().isEmpty()
            ) {
                throw Exception("ReaderText is empty.")
            }

            readerText
        }.fold(
            onSuccess = {
                logI("Successfully loaded text from [$bookId] with markdown.")
                return it
            },
            onFailure = {
                logE("Could not load text with exception: ${it.message}")
                return emptyList()
            }
        )
    }
}