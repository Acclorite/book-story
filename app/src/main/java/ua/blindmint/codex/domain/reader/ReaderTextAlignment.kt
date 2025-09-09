/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.domain.reader

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.style.TextAlign

@Immutable
enum class ReaderTextAlignment(val textAlignment: TextAlign) {
    START(TextAlign.Start),
    JUSTIFY(TextAlign.Justify),
    CENTER(TextAlign.Center),
    END(TextAlign.End)
}

fun String.toTextAlignment(): ReaderTextAlignment {
    return ReaderTextAlignment.valueOf(this)
}