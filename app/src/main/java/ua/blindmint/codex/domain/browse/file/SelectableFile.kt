/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.domain.browse.file

import androidx.compose.runtime.Immutable
import ua.blindmint.codex.domain.file.CachedFile
import ua.blindmint.codex.domain.util.Selected

@Immutable
data class SelectableFile(
    val data: CachedFile,
    val selected: Selected
)