package ua.acclorite.book_story.data.mapper.language_history

import ua.acclorite.book_story.data.local.dto.LanguageHistoryEntity
import ua.acclorite.book_story.domain.model.LanguageHistory

interface LanguageHistoryMapper {
    suspend fun toLanguageHistoryEntity(languageHistory: LanguageHistory): LanguageHistoryEntity

    suspend fun toLanguageHistory(languageHistoryEntity: LanguageHistoryEntity): LanguageHistory
}