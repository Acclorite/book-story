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

class AddHistoryUseCase @Inject constructor(
    private val historyRepository: HistoryRepository
) {

    suspend operator fun invoke(history: History) {
        logI("Inserting history for [${history.book.id}].")

        historyRepository.addHistory(history = history).fold(
            onSuccess = {
                logI("Successfully inserted history for [${history.book.id}].")
            },
            onFailure = {
                logE("Could not insert history for [${history.book.id}] with error: ${it.message}")
            }
        )
    }
}