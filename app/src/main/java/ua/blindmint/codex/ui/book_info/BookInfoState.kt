/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.ui.book_info

import androidx.compose.runtime.Immutable
import ua.blindmint.codex.domain.library.book.Book
import ua.blindmint.codex.domain.util.BottomSheet
import ua.blindmint.codex.domain.util.Dialog
import ua.blindmint.codex.presentation.core.constants.provideEmptyBook

@Immutable
data class BookInfoState(
    val book: Book = provideEmptyBook(),

    val canResetCover: Boolean = false,

    val dialog: Dialog? = null,
    val bottomSheet: BottomSheet? = null
)