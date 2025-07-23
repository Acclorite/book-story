/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.reader.model

import android.content.pm.ActivityInfo
import androidx.annotation.StringRes
import ua.acclorite.book_story.R

enum class ReaderScreenOrientation(val code: Int, @StringRes val title: Int) {
    DEFAULT(
        ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED,
        R.string.default_string
    ),
    FREE(
        ActivityInfo.SCREEN_ORIENTATION_USER,
        R.string.screen_orientation_free
    ),
    PORTRAIT(
        ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT,
        R.string.screen_orientation_portrait
    ),
    LANDSCAPE(
        ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE,
        R.string.screen_orientation_landscape
    ),
    LOCKED_PORTRAIT(
        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT,
        R.string.screen_orientation_locked_portrait
    ),
    LOCKED_LANDSCAPE(
        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE,
        R.string.screen_orientation_locked_landscape
    )
}