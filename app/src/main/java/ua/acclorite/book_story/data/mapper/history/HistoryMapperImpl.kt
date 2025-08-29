/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.mapper.history

import ua.acclorite.book_story.data.local.dto.HistoryEntity
import ua.acclorite.book_story.data.local.dto.HistoryWithBook
import ua.acclorite.book_story.data.mapper.book.BookMapper
import ua.acclorite.book_story.domain.model.history.History
import javax.inject.Inject

class HistoryMapperImpl @Inject constructor(
    private val bookMapper: BookMapper
) : HistoryMapper {
    override suspend fun toHistoryEntity(history: History): HistoryEntity {
        return HistoryEntity(
            id = history.id,
            bookId = history.book.id,
            time = history.time
        )
    }

    override suspend fun toHistory(historyWithBook: HistoryWithBook): History {
        return History(
            id = historyWithBook.history.id,
            book = bookMapper.toBook(historyWithBook.book),
            time = historyWithBook.history.time
        )
    }
}