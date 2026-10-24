/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.reader

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.model.book.Chapter
import ua.acclorite.book_story.domain.model.book.NodeId
import ua.acclorite.book_story.presentation.reader.model.Checkpoint

@Immutable
sealed class ReaderEvent {

    data object OnLoadContent : ReaderEvent()

    data object OnRetry : ReaderEvent()

    data class OnPaginateForward(
        val lastNodeId: NodeId
    ) : ReaderEvent()

    data class OnPaginateBackward(
        val firstNodeId: NodeId
    ) : ReaderEvent()

    data class OnUpdateProgress(
        val currentNodeId: NodeId,
        val currentNodeIntraOffset: Int,
        val currentNodeOffset: Int? = null
    ) : ReaderEvent()

    data class OnMenuVisibility(
        val show: Boolean,
        val saveCheckpoint: Boolean
    ) : ReaderEvent()

    data class OnScrollToChapter(
        val chapter: Chapter
    ) : ReaderEvent()

    data class OnScroll(
        val progress: Float
    ) : ReaderEvent()

    data class OnRestoreCheckpoint(
        val checkpoint: Checkpoint
    ) : ReaderEvent()

    data object OnScrollConsumed : ReaderEvent()

    data class OnLeave(
        val navigate: () -> Unit
    ) : ReaderEvent()

    data class OnOpenTranslator(
        val textToTranslate: String,
        val translateWholeParagraph: Boolean
    ) : ReaderEvent()

    data class OnOpenShareApp(
        val textToShare: String
    ) : ReaderEvent()

    data class OnOpenWebBrowser(
        val textToSearch: String
    ) : ReaderEvent()

    data class OnOpenDictionary(
        val textToDefine: String
    ) : ReaderEvent()

    data object OnShowSettingsBottomSheet : ReaderEvent()

    data object OnDismissBottomSheet : ReaderEvent()

    data object OnShowChaptersDrawer : ReaderEvent()

    data object OnDismissDrawer : ReaderEvent()

    data object OnNavigateBack : ReaderEvent()

    data class OnNavigateToBookInfo(
        val changePath: Boolean
    ) : ReaderEvent()
}
