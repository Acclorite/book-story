/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.theme.model

import androidx.annotation.StringRes
import androidx.compose.ui.Alignment
import ua.acclorite.book_story.R

enum class HorizontalAlignment(val alignment: Alignment, @StringRes val title: Int) {
    START(Alignment.Companion.CenterStart, R.string.alignment_start),
    CENTER(Alignment.Companion.Center, R.string.alignment_center),
    END(Alignment.Companion.CenterEnd, R.string.alignment_end)
}