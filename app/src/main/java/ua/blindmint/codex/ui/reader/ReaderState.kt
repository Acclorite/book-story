/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.ui.reader

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Immutable
import ua.blindmint.codex.domain.library.book.Book
import ua.blindmint.codex.domain.reader.Checkpoint
import ua.blindmint.codex.domain.reader.ReaderText
import ua.blindmint.codex.domain.reader.ReaderText.Chapter
import ua.blindmint.codex.domain.ui.UIText
import ua.blindmint.codex.domain.util.BottomSheet
import ua.blindmint.codex.domain.util.Drawer
import ua.blindmint.codex.presentation.core.constants.provideEmptyBook

@Immutable
data class ReaderState(
    val book: Book = provideEmptyBook(),
    val text: List<ReaderText> = emptyList(),
    val listState: LazyListState = LazyListState(),

    val currentChapter: Chapter? = null,
    val currentChapterProgress: Float = 0f,

    val errorMessage: UIText? = null,
    val isLoading: Boolean = true,

    val showMenu: Boolean = false,
    val checkpoint: Checkpoint = Checkpoint(0, 0),
    val lockMenu: Boolean = false,

    val bottomSheet: BottomSheet? = null,
    val drawer: Drawer? = null
)