/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.domain.ui

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

fun String.toPureDark(): PureDark {
    return PureDark.valueOf(this)
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