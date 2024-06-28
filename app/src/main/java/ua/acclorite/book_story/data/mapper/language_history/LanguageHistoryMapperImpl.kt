package ua.acclorite.book_story.data.mapper.language_history

import ua.acclorite.book_story.data.local.dto.LanguageHistoryEntity
import ua.acclorite.book_story.domain.model.LanguageHistory
import javax.inject.Inject

class LanguageHistoryMapperImpl @Inject constructor() : LanguageHistoryMapper {

    override suspend fun toLanguageHistoryEntity(languageHistory: LanguageHistory): LanguageHistoryEntity {
        return LanguageHistoryEntity(
            languageCode = languageHistory.languageCode,
            order = languageHistory.order
        )
    }

    override suspend fun toLanguageHistory(languageHistoryEntity: LanguageHistoryEntity): LanguageHistory {
        return LanguageHistory(
            languageCode = languageHistoryEntity.languageCode,
            order = languageHistoryEntity.order
        )
    }
}