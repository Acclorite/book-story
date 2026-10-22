/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.storage.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ua.acclorite.book_story.data.converter.CategoryConverter

@Entity
@TypeConverters(CategoryConverter::class)
data class BookEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(defaultValue = "") val uuid: String,
    val title: String,
    val author: String,
    val description: String?,
    val filePath: String,
    @ColumnInfo(defaultValue = "0") val currentNodeId: Int,
    @ColumnInfo(defaultValue = "0") val currentNodeIntraOffset: Int,
    @ColumnInfo(defaultValue = "0") val totalNodes: Int,
    @ColumnInfo(defaultValue = "0") val currentOffset: Int,
    @ColumnInfo(defaultValue = "0") val totalLength: Int,
    val image: String? = null,
    @ColumnInfo(defaultValue = "[]") val categories: List<Int>,

    // Obsolete variables, will be deleted
    // at some point
    val scrollIndex: Int,
    val scrollOffset: Int,
    val progress: Float,
)