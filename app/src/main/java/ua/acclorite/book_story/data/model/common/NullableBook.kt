/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.model.common

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.core.ui.UIText

@Immutable
sealed class NullableBook(
    val bookWithCover: BookWithCover?,
    val fileName: String?,
    val message: UIText?
) {
    class NotNull(
        bookWithCover: BookWithCover
    ) : NullableBook(
        bookWithCover = bookWithCover,
        fileName = null,
        message = null
    )

    class Null(
        fileName: String,
        message: UIText?
    ) : NullableBook(
        bookWithCover = null,
        fileName = fileName,
        message = message
    )
}