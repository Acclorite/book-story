package ua.acclorite.book_story.data.mapper

import ua.acclorite.book_story.data.local.dto.HistoryEntity
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.model.History
import javax.inject.Inject

class HistoryMapperImpl @Inject constructor() : HistoryMapper {
    override suspend fun toHistoryEntity(history: History): HistoryEntity {
        return HistoryEntity(
            id = history.id,
            bookId = history.book.id ?: 0,
            time = history.time
        )
    }

    override suspend fun toHistory(historyEntity: HistoryEntity, book: Book): History {
        return History(
            historyEntity.id,
            book = book,
            time = historyEntity.time
        )
    }
}