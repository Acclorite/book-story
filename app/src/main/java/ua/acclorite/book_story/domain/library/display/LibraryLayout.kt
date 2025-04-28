/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.library.display

import androidx.compose.runtime.Immutable

@Immutable
enum class LibraryLayout {
    GRID, LIST
}

fun String.toLibraryLayout(): LibraryLayout {
    return LibraryLayout.valueOf(this)
}