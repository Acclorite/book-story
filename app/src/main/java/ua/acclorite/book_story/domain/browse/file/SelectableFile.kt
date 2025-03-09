/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.browse.file

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.file.CachedFile
import ua.acclorite.book_story.domain.util.Selected

@Immutable
data class SelectableFile(
    val data: CachedFile,
    val selected: Selected
)