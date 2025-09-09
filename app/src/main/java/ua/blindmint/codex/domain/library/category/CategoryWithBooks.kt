/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.domain.library.category

import androidx.compose.runtime.Immutable
import ua.blindmint.codex.domain.library.book.SelectableBook
import ua.blindmint.codex.domain.ui.UIText

@Immutable
data class CategoryWithBooks(
    val category: Category,
    val title: UIText,
    val books: List<SelectableBook>
)