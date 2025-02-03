/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.reader

enum class ReaderColorEffects {
    OFF,
    GRAYSCALE,
    FONT,
    BACKGROUND
}

fun String.toColorEffects(): ReaderColorEffects {
    return ReaderColorEffects.valueOf(this)
}