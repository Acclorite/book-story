package ua.acclorite.book_story.data.local.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteDirectoryEntity(
    @PrimaryKey(autoGenerate = false)
    val path: String
)