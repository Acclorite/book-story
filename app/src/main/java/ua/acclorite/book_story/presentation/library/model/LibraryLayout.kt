/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.library.model

import androidx.annotation.StringRes
import ua.acclorite.book_story.R

enum class LibraryLayout(@StringRes val title: Int) {
    GRID(R.string.layout_grid),
    LIST(R.string.layout_list)
}