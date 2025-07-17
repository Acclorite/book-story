/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.core.helpers

fun <T> MutableList<T>.addAll(calculation: () -> List<T>) {
    addAll(calculation())
}

fun <T> List<T>.toggle(item: T): List<T> {
    return toMutableList().apply { toggle(item) }
}

fun <T> MutableList<T>.toggle(item: T) {
    if (contains(item)) remove(item)
    else add(item)
}