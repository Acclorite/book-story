/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.mapper.history

import ua.acclorite.book_story.data.mapper.book.BookMapper
import ua.acclorite.book_story.data.storage.room.entity.HistoryEntity
import ua.acclorite.book_story.data.storage.room.entity.HistoryWithBook
import ua.acclorite.book_story.domain.model.history.History
import javax.inject.Inject

class HistoryMapperImpl @Inject constructor(
    private val bookMapper: BookMapper
) : HistoryMapper {

    override fun toHistoryEntity(history: History): HistoryEntity {
        if (history.id <= -1
            || history.time <= 0
            || history.book.id <= -1
        ) {
            throw IllegalStateException("Incorrect History instance.")
        }

        return HistoryEntity(
            id = history.id,
            bookId = history.book.id,
            time = history.time
        )
    }

    override fun toHistory(historyWithBook: HistoryWithBook): History {
        if (historyWithBook.history.id <= -1
            || historyWithBook.history.time <= 0
            || historyWithBook.book.id <= -1
            || historyWithBook.history.bookId != historyWithBook.book.id
        ) {
            throw IllegalStateException("Incorrect HistoryWithBook instance.")
        }

        return History(
            id = historyWithBook.history.id,
            book = bookMapper.toBook(historyWithBook.book),
            time = historyWithBook.history.time
        )
    }
}