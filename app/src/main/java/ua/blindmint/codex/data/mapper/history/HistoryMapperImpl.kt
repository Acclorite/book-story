/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.data.mapper.history

import ua.blindmint.codex.data.local.dto.HistoryEntity
import ua.blindmint.codex.domain.history.History
import javax.inject.Inject

class HistoryMapperImpl @Inject constructor() : HistoryMapper {
    override suspend fun toHistoryEntity(history: History): HistoryEntity {
        return HistoryEntity(
            id = history.id,
            bookId = history.bookId,
            time = history.time
        )
    }

    override suspend fun toHistory(historyEntity: HistoryEntity): History {
        return History(
            historyEntity.id,
            bookId = historyEntity.bookId,
            book = null,
            time = historyEntity.time
        )
    }
}