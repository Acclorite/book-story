/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.util

import androidx.compose.ui.Alignment

enum class HorizontalAlignment(val alignment: Alignment) {
    START(Alignment.CenterStart),
    CENTER(Alignment.Center),
    END(Alignment.CenterEnd)
}

fun String.toHorizontalAlignment(): HorizontalAlignment {
    return HorizontalAlignment.valueOf(this)
}