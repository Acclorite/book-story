/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.reader

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.core.BottomSheet
import ua.acclorite.book_story.core.Drawer
import ua.acclorite.book_story.core.ui.UIText
import ua.acclorite.book_story.domain.model.book.Book
import ua.acclorite.book_story.domain.model.book.Chapter
import ua.acclorite.book_story.domain.model.book.Node
import ua.acclorite.book_story.domain.model.book.NodeId
import ua.acclorite.book_story.domain.use_case.book.GetBookContentState
import ua.acclorite.book_story.presentation.reader.model.Checkpoint

@Immutable
sealed class LayoutState {
    @Immutable
    data object Loading : LayoutState()

    @Immutable
    data class Parsing(
        val progress: Float
    ) : LayoutState()

    @Immutable
    data object Saving : LayoutState()

    @Immutable
    data object Showing : LayoutState()

    @Immutable
    data class Error(
        val message: UIText,
        val action: GetBookContentState.Companion.ErrorAction
    ) : LayoutState()
}

@Immutable
data class ScrollTarget(
    val nodeId: NodeId,
    val nodeIntraOffset: Int
)

@Immutable
data class ReaderState(
    val book: Book = Book.default,
    val chapters: List<Chapter> = emptyList(),
    val nodes: List<Node> = emptyList(),
    val paginating: Boolean = false,
    val scrollTarget: ScrollTarget? = null,

    val layoutState: LayoutState = LayoutState.Loading,

    val showMenu: Boolean = false,
    val checkpoints: List<Checkpoint> = emptyList(),
    val lockMenu: Boolean = false,

    val bottomSheet: BottomSheet? = null,
    val drawer: Drawer? = null
)
