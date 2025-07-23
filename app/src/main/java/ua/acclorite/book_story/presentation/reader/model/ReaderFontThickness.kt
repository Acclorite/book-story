/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.reader.model

import androidx.annotation.StringRes
import androidx.compose.ui.text.font.FontWeight
import ua.acclorite.book_story.R

enum class ReaderFontThickness(val thickness: FontWeight, @StringRes val title: Int) {
    THIN(FontWeight.Thin, R.string.font_thickness_thin),
    EXTRA_LIGHT(FontWeight.ExtraLight, R.string.font_thickness_extra_light),
    LIGHT(FontWeight.Light, R.string.font_thickness_light),
    NORMAL(FontWeight.Normal, R.string.font_thickness_normal),
    MEDIUM(FontWeight.Medium, R.string.font_thickness_medium)
}