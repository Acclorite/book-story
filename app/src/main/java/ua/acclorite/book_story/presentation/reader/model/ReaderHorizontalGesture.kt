/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.reader.model

import androidx.annotation.StringRes
import ua.acclorite.book_story.R

enum class ReaderHorizontalGesture(@StringRes val title: Int) {
    OFF(R.string.horizontal_gesture_off),
    ON(R.string.horizontal_gesture_on),
    INVERSE(R.string.horizontal_gesture_inverse)
}