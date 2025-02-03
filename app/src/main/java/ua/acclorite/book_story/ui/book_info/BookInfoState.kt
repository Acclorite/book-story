/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.book_info

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.library.book.Book
import ua.acclorite.book_story.domain.util.BottomSheet
import ua.acclorite.book_story.domain.util.Dialog
import ua.acclorite.book_story.presentation.core.constants.Constants
import ua.acclorite.book_story.presentation.core.constants.provideEmptyBook

@Immutable
data class BookInfoState(
    val book: Book = Constants.provideEmptyBook(),

    val canResetCover: Boolean = false,

    val dialog: Dialog? = null,
    val bottomSheet: BottomSheet? = null
)