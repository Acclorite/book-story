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
    val image: ByteArray = byteArrayOf(),
    val category: Category
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BookEntity

        return image.contentEquals(other.image)
    }

    override fun hashCode(): Int {
        return image.contentHashCode()
    }
}
