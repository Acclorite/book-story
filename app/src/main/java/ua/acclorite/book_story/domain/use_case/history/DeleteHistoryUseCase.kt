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

class DeleteHistoryUseCase @Inject constructor(
    private val historyRepository: HistoryRepository
) {

    suspend operator fun invoke(history: History) {
        logI("Deleting history [${history.id}].")

        return historyRepository.deleteHistory(history).fold(
            onSuccess = {
                logI("Successfully deleted history [${history.id}].")
            },
            onFailure = {
                logE("Could not delete history [${history.id}] with error: ${it.message}")
            }
        )
    }
}