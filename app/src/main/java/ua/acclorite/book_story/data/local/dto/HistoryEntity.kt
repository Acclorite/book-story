package ua.acclorite.book_story.data.local.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryEntity(
    @PrimaryKey(true)
    val id: Int? = null,
    val bookId: Int,
    val time: Long
)
