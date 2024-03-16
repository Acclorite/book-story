package ua.acclorite.book_story.data.local.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import ua.acclorite.book_story.domain.model.Category

@Entity
data class BookEntity(
    @PrimaryKey(true) val id: Int? = null,
    val title: String,
    val author: String?,
    val description: String?,
    val text: String,
    val filePath: String,
    val progress: Float,
    val image: String? = null,
    val category: Category
)
