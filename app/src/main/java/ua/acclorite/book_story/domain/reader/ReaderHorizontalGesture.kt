/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.reader

enum class ReaderHorizontalGesture {
    OFF,
    ON,
    INVERSE
}

fun String.toHorizontalGesture(): ReaderHorizontalGesture {
    return ReaderHorizontalGesture.valueOf(this)
}