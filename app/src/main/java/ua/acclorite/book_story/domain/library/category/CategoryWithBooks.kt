/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.library.category

import ua.acclorite.book_story.domain.library.book.SelectableBook
import ua.acclorite.book_story.domain.ui.UIText

data class CategoryWithBooks(
    val category: Category,
    val title: UIText,
    val books: List<SelectableBook>
)