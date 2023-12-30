package com.acclorite.books_history.data.local.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.acclorite.books_history.domain.model.Category

@Entity
data class BookEntity(
    @PrimaryKey(true) val id: Int? = null,
    val title: String,
    val filePath: String,
    val progress: Float,
    var lastOpened: Long? = null,
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
