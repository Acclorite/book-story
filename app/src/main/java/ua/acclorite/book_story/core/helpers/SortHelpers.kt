/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.core.helpers

fun <T> compareByWithOrder(
    descending: Boolean,
    selector: (T) -> Comparable<*>?
): Comparator<T> {
    return if (descending) {
        compareByDescending(selector)
    } else {
        compareBy(selector)
    }
}