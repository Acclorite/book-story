/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.browse

enum class BrowseSortOrder {
    NAME,
    FILE_FORMAT,
    LAST_MODIFIED,
    FILE_SIZE,
}

fun String.toBrowseSortOrder(): BrowseSortOrder {
    return try {
        BrowseSortOrder.valueOf(this)
    } catch (_: IllegalArgumentException) {
        BrowseSortOrder.LAST_MODIFIED
    }
}