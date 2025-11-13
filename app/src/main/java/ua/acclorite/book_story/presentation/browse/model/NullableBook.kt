/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.browse.model

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.core.CoverImage
import ua.acclorite.book_story.core.ui.UIText
import ua.acclorite.book_story.domain.model.library.Book

@Immutable
sealed class NullableBook {
    data class NotNull(
        val book: Book,
        val coverImage: CoverImage?
    ) : NullableBook()

    data class Null(
        val fileName: String,
        val message: UIText?
    ) : NullableBook()
}