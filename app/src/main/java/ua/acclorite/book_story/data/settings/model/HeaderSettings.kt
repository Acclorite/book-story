/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.settings.model

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.presentation.reader.model.ReaderTextAlignment

@Immutable
class HeaderSettings(
    val fontSize: Setting<Int, Int>,
    val fontWeight: Setting<Int, Int>,
    val textAlignment: Setting<ReaderTextAlignment, String>
)
