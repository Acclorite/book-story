/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.main.model

import android.content.Context
import android.os.PowerManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

@Immutable
enum class PureDark {
    OFF,
    ON,
    SAVER
}

@Composable
fun PureDark.isPureDark(context: Context): Boolean {
    val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager

    return when (this) {
        PureDark.OFF -> false
        PureDark.ON -> true
        PureDark.SAVER -> powerManager.isPowerSaveMode
    }
}