package ua.acclorite.book_story.data.mapper.history

import ua.acclorite.book_story.data.local.dto.HistoryEntity
import ua.acclorite.book_story.domain.history.History

interface HistoryMapper {
    suspend fun toHistoryEntity(history: History): HistoryEntity

    suspend fun toHistory(historyEntity: HistoryEntity): History
}