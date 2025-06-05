/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.history

import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.core.log.logI
import ua.acclorite.book_story.domain.model.history.History
import ua.acclorite.book_story.domain.repository.HistoryRepository
import javax.inject.Inject

class GetHistoryForBookUseCase @Inject constructor(
    private val historyRepository: HistoryRepository
) {

    suspend operator fun invoke(bookId: Int): History? {
        logI("Getting history for [$bookId].")

        return historyRepository.getHistoryForBook(bookId = bookId).fold(
            onSuccess = {
                logI("Successfully got history for [$bookId].")
                it
            },
            onFailure = {
                logE("Could not get history for [$bookId] with error: ${it.message}")
                null
            }
        )
    }
}