/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.ui.about

import android.content.Context
import androidx.compose.runtime.Immutable

@Immutable
sealed class AboutEvent {
    data class OnNavigateToBrowserPage(
        val page: String,
        val context: Context
    ) : AboutEvent()
}