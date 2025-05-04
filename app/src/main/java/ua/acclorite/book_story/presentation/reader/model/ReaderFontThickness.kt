/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.reader.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.font.FontWeight

@Immutable
enum class ReaderFontThickness(val thickness: FontWeight) {
    THIN(FontWeight.Thin),
    EXTRA_LIGHT(FontWeight.ExtraLight),
    LIGHT(FontWeight.Light),
    NORMAL(FontWeight.Normal),
    MEDIUM(FontWeight.Medium)
}