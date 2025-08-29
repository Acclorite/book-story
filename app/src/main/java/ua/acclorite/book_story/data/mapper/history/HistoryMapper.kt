/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.mapper.history

import ua.acclorite.book_story.data.local.dto.HistoryEntity
import ua.acclorite.book_story.data.local.dto.HistoryWithBook
import ua.acclorite.book_story.domain.model.history.History

interface HistoryMapper {
    suspend fun toHistoryEntity(history: History): HistoryEntity
    suspend fun toHistory(historyWithBook: HistoryWithBook): History
}