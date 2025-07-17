/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.theme.model

import android.os.Build
import androidx.annotation.StringRes
import ua.acclorite.book_story.R

enum class Theme(
    val hasThemeContrast: Boolean,
    @StringRes val title: Int
) {
    DYNAMIC(hasThemeContrast = false, title = R.string.dynamic_theme),
    BLUE(hasThemeContrast = true, title = R.string.blue_theme),
    GREEN(hasThemeContrast = true, title = R.string.green_theme),
    GREEN2(hasThemeContrast = false, title = R.string.green2_theme),
    GREEN_GRAY(hasThemeContrast = false, title = R.string.green_gray_theme),
    MARSH(hasThemeContrast = true, title = R.string.marsh_theme),
    RED(hasThemeContrast = true, title = R.string.red_theme),
    RED_GRAY(hasThemeContrast = false, title = R.string.red_gray_theme),
    PURPLE(hasThemeContrast = true, title = R.string.purple_theme),
    PURPLE_GRAY(hasThemeContrast = false, title = R.string.purple_gray_theme),
    LAVENDER(hasThemeContrast = true, title = R.string.lavender_theme),
    PINK(hasThemeContrast = true, title = R.string.pink_theme),
    PINK2(hasThemeContrast = false, title = R.string.pink2_theme),
    YELLOW(hasThemeContrast = true, title = R.string.yellow_theme),
    YELLOW2(hasThemeContrast = false, title = R.string.yellow2_theme),
    AQUA(hasThemeContrast = true, title = R.string.aqua_theme),
    GRAY(hasThemeContrast = false, title = R.string.gray_theme);

    companion object {
        fun entries(): List<Theme> {
            return when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> entries
                else -> entries.dropWhile { it == DYNAMIC }
            }
        }
    }
}