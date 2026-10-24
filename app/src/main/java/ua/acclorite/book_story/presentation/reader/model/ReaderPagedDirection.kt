/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.reader.model

import androidx.annotation.StringRes
import ua.acclorite.book_story.R

enum class ReaderPagedDirection(
    @StringRes val title: Int,
    val vertical: Boolean,
    val reversed: Boolean
) {
    LEFT_TO_RIGHT(R.string.paged_direction_left_to_right, vertical = false, reversed = false),
    RIGHT_TO_LEFT(R.string.paged_direction_right_to_left, vertical = false, reversed = true),
    TOP_TO_BOTTOM(R.string.paged_direction_top_to_bottom, vertical = true, reversed = false),
    BOTTOM_TO_TOP(R.string.paged_direction_bottom_to_top, vertical = true, reversed = true)
}
