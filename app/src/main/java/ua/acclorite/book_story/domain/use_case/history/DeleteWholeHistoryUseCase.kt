/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.history

import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.core.log.logI
import ua.acclorite.book_story.domain.repository.HistoryRepository
import javax.inject.Inject

private const val TAG = "DeleteWholeHistory"

class DeleteWholeHistoryUseCase @Inject constructor(
    private val historyRepository: HistoryRepository
) {

    suspend operator fun invoke() {
        logI(TAG, "Deleting whole history.")

        return historyRepository.deleteWholeHistory().fold(
            onSuccess = {
                logI(TAG, "Successfully deleted whole history.")
            },
            onFailure = {
                logE(TAG, "Could not delete whole history with error: ${it.message}")
            }
        )
    }
}