package ua.acclorite.book_story.data.local.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LanguageHistoryEntity(
    @PrimaryKey(false) val languageCode: String,
    val order: Int
)
