/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.storage.room.entity.content

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [
        Index("offset")
    ]
)
data class NodeEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val length: Int,
    val offset: Int,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val contentBlob: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NodeEntity

        if (id != other.id) return false
        if (length != other.length) return false
        if (offset != other.offset) return false
        if (!contentBlob.contentEquals(other.contentBlob)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + length
        result = 31 * result + offset
        result = 31 * result + contentBlob.contentHashCode()
        return result
    }

}