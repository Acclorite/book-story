/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.theme.model

import android.content.Context
import android.os.PowerManager
import androidx.compose.runtime.Composable

enum class PureDark {
    OFF,
    ON,
    SAVER;

    @Composable
    fun isPureDark(context: Context): Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager

        return when (this) {
            OFF -> false
            ON -> true
            SAVER -> powerManager.isPowerSaveMode
        }
    }
}