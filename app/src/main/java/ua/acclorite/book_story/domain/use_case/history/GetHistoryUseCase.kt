/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.history

import ua.acclorite.book_story.core.logE
import ua.acclorite.book_story.core.logI
import ua.acclorite.book_story.domain.history.History
import ua.acclorite.book_story.domain.repository.HistoryRepository
import javax.inject.Inject

class GetHistoryUseCase @Inject constructor(
    private val historyRepository: HistoryRepository
) {

    suspend operator fun invoke(): List<History> {
        logI("Getting all history.")

        return historyRepository.getHistory().fold(
            onSuccess = {
                logI("Successfully got ${it.size} history entries.")
                it
            },
            onFailure = {
                logE("Could not get history entries with error: ${it.message}")
                emptyList()
            }
        )
    }
}