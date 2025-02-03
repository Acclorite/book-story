/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.history

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.library.book.Book

@Immutable
data class History(
    val id: Int = 0,
    val bookId: Int,
    val book: Book?,
    val time: Long
)