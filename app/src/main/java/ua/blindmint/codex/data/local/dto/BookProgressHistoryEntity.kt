/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.data.local.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity to store progress history for books that have been removed from the library.
 * This allows seamless resumption when the same book is re-added.
 */
@Entity
data class BookProgressHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val filePath: String, // Unique identifier for the book file
    val title: String,
    val author: String?,
    val scrollIndex: Int,
    val scrollOffset: Int,
    val progress: Float,
    val lastModified: Long // Timestamp when this progress was saved
)