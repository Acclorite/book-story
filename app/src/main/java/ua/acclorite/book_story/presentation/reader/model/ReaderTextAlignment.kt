/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.reader.model

import androidx.annotation.StringRes
import androidx.compose.ui.text.style.TextAlign
import ua.acclorite.book_story.R

enum class ReaderTextAlignment(val textAlignment: TextAlign, @StringRes val title: Int) {
    START(TextAlign.Start, R.string.alignment_start),
    JUSTIFY(TextAlign.Justify, R.string.alignment_justify),
    CENTER(TextAlign.Center, R.string.alignment_center),
    END(TextAlign.End, R.string.alignment_end)
}