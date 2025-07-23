/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.library.model

import androidx.annotation.StringRes
import ua.acclorite.book_story.R


enum class LibraryTitlePosition(@StringRes val title: Int) {
    OFF(R.string.library_title_position_off),
    BELOW(R.string.library_title_position_below),
    INSIDE(R.string.library_title_position_inside)
}