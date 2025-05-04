/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.browse.model

import androidx.compose.runtime.Immutable

@Immutable
data class GroupedFiles(
    val header: String,
    val pinned: Boolean,
    val files: List<SelectableFile>
)