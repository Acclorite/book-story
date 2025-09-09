/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.domain.use_case.history

import ua.blindmint.codex.domain.history.History
import ua.blindmint.codex.domain.repository.HistoryRepository
import javax.inject.Inject

class InsertHistory @Inject constructor(
    private val repository: HistoryRepository
) {

    suspend fun execute(history: History) {
        repository.insertHistory(history)
    }
}