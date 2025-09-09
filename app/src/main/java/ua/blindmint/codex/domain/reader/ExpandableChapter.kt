/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.domain.reader

import androidx.compose.runtime.Immutable

@Immutable
data class ExpandableChapter(
    val parent: ReaderText.Chapter,
    val expanded: Boolean,
    val chapters: List<ReaderText.Chapter>?
)