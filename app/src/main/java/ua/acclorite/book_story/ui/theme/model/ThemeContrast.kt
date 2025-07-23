/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.theme.model

import androidx.annotation.StringRes
import ua.acclorite.book_story.R


enum class ThemeContrast(@StringRes val title: Int) {
    STANDARD(R.string.theme_contrast_standard),
    MEDIUM(R.string.theme_contrast_medium),
    HIGH(R.string.theme_contrast_high)
}