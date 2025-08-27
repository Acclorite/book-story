/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.model.reader

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class ColorPreset(
    val id: Int,
    val name: String,
    val backgroundColor: Color,
    val fontColor: Color,
    val isSelected: Boolean
) {
    companion object {
        val default = ColorPreset(
            id = -1,
            name = "",
            backgroundColor = Color(0xFFFAF8FF), // Blue Light Surface (hardcoded)
            fontColor = Color(0xFF44464F), // Blue Light OnSurfaceVariant (hardcoded)
            isSelected = false
        )
    }
}