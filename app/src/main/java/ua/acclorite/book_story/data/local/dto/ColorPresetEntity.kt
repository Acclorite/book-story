/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.local.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ColorPresetEntity(
    @PrimaryKey(true)
    val id: Int? = null,
    val name: String,
    val backgroundColor: Long,
    val fontColor: Long,
    val isSelected: Boolean,
    val order: Int
)