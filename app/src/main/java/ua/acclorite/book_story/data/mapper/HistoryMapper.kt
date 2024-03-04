package ua.acclorite.book_story.data.mapper

import ua.acclorite.book_story.data.local.dto.HistoryEntity
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.model.History

interface HistoryMapper {
    suspend fun toHistoryEntity(history: History): HistoryEntity

    suspend fun toHistory(historyEntity: HistoryEntity, book: Book): History
}