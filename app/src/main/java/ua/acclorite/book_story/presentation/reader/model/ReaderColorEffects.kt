/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.reader.model

import androidx.annotation.StringRes
import ua.acclorite.book_story.R

enum class ReaderColorEffects(@StringRes val title: Int) {
    OFF(R.string.color_effects_off),
    GRAYSCALE(R.string.color_effects_grayscale),
    FONT(R.string.color_effects_font),
    BACKGROUND(R.string.color_effects_background)
}