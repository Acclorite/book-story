/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.reader.model

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.model.reader.ReaderText

@Immutable
data class ExpandableChapter(
    val parent: ReaderText.Chapter,
    val expanded: Boolean,
    val chapters: List<ReaderText.Chapter>?
)