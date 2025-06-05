/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.core.data

import kotlinx.collections.immutable.persistentListOf

object ExtensionsData {
    val fileExtensions = persistentListOf(
        ".epub",
        ".pdf",
        ".fb2",
        ".txt",
        ".html",
        ".htm",
        ".md"
    )

    val imageExtensions = persistentListOf(
        ".png",
        ".jpg",
        ".jpeg",
        ".gif"
    )
}