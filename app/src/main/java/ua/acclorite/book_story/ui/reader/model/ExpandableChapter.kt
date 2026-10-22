/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.reader.model

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.model.book.Chapter

@Immutable
data class ExpandableChapter(
    val chapter: Chapter,
    val depth: Int,
    val hasChildren: Boolean,
    val expanded: Boolean
)