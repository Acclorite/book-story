/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.storage.room.entity.content

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MetadataEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val fileHash: String,
    val parserVersion: Int,
    val parser: String
)