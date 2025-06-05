/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.reader.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.style.TextAlign

@Immutable
enum class ReaderTextAlignment(val textAlignment: TextAlign) {
    START(TextAlign.Start),
    JUSTIFY(TextAlign.Justify),
    CENTER(TextAlign.Center),
    END(TextAlign.End)
}