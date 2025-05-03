/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.main.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment

@Immutable
enum class HorizontalAlignment(val alignment: Alignment) {
    START(Alignment.Companion.CenterStart),
    CENTER(Alignment.Companion.Center),
    END(Alignment.Companion.CenterEnd)
}