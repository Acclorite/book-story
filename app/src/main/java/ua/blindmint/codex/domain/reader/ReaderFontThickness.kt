/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.domain.reader

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.font.FontWeight

@Immutable
enum class ReaderFontThickness(val thickness: FontWeight) {
    THIN(FontWeight.Thin),
    EXTRA_LIGHT(FontWeight.ExtraLight),
    LIGHT(FontWeight.Light),
    NORMAL(FontWeight.Normal),
    MEDIUM(FontWeight.Medium)
}

fun String.toFontThickness(): ReaderFontThickness {
    return ReaderFontThickness.valueOf(this)
}