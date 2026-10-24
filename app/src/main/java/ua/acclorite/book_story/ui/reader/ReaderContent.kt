/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.reader

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import ua.acclorite.book_story.core.BottomSheet
import ua.acclorite.book_story.core.Drawer
import ua.acclorite.book_story.domain.model.book.Book
import ua.acclorite.book_story.domain.model.book.Chapter
import ua.acclorite.book_story.domain.model.book.Node
import ua.acclorite.book_story.domain.model.book.NodeId
import ua.acclorite.book_story.presentation.reader.LayoutState
import ua.acclorite.book_story.presentation.reader.ReaderEvent
import ua.acclorite.book_story.presentation.reader.ScrollTarget
import ua.acclorite.book_story.presentation.reader.model.Checkpoint
import ua.acclorite.book_story.presentation.reader.model.ReaderHorizontalGesture
import ua.acclorite.book_story.presentation.reader.model.ReaderLayout
import ua.acclorite.book_story.presentation.settings.SettingsEvent
import ua.acclorite.book_story.ui.theme.model.HorizontalAlignment

@Composable
fun ReaderContent(
    layout: ReaderLayout,
    layoutState: LayoutState,
    nodes: List<Node>,
    chapters: List<Chapter>,
    book: Book,
    paginating: Boolean,
    scrollTarget: ScrollTarget?,
    bottomSheet: BottomSheet?,
    drawer: Drawer?,
    currentChapter: Chapter?,
    currentChapterString: String?,
    currentChapterProgress: Float,
    chaptersEmpty: Boolean,
    nestedScrollConnection: NestedScrollConnection,
    fastColorPresetChange: Boolean,
    perceptionExpander: Boolean,
    perceptionExpanderPadding: Dp,
    perceptionExpanderThickness: Dp,
    horizontalLimiter: Boolean,
    horizontalLimiterHeight: Dp,
    horizontalLimiterVerticalOffset: Float,
    horizontalLimiterRulerThickness: Dp,
    horizontalLimiterRuler: Boolean,
    horizontalLimiterDimming: Float,
    checkpoints: List<Checkpoint>,
    showMenu: Boolean,
    lockMenu: Boolean,
    contentPadding: PaddingValues,
    verticalPadding: Dp,
    horizontalGesture: ReaderHorizontalGesture,
    horizontalGestureScroll: Float,
    horizontalGestureSensitivity: Dp,
    horizontalGestureAlphaAnim: Boolean,
    horizontalGesturePullAnim: Boolean,
    horizontalGestureDisableScrolling: Boolean,
    highlightedReading: Boolean,
    highlightedReadingThickness: Int,
    progress: String,
    progressBar: Boolean,
    progressBarPadding: Dp,
    progressBarAlignment: HorizontalAlignment,
    progressBarFontSize: TextUnit,
    paragraphTextStyle: TextStyle,
    boldFontThickness: Int,
    header1TextStyle: TextStyle,
    header2TextStyle: TextStyle,
    header3TextStyle: TextStyle,
    header4TextStyle: TextStyle,
    header5TextStyle: TextStyle,
    header6TextStyle: TextStyle,
    paragraphHeight: Dp,
    sidePadding: Dp,
    bottomBarPadding: Dp,
    backgroundColor: Color,
    fontColor: Color,
    images: Boolean,
    imagesCaptions: Boolean,
    imagesCornersRoundness: Dp,
    imagesAlignment: HorizontalAlignment,
    imagesWidth: Float,
    imagesColorEffects: ColorFilter?,
    imagesOriginalSize: Boolean,
    horizontalAlignment: Alignment.Horizontal,
    doubleClickTranslation: Boolean,
    switchColorPreset: (SettingsEvent.OnSwitchColorPreset) -> Unit,
    menuVisibility: (ReaderEvent.OnMenuVisibility) -> Unit,
    retry: (ReaderEvent.OnRetry) -> Unit,
    leave: (ReaderEvent.OnLeave) -> Unit,
    restoreCheckpoint: (ReaderEvent.OnRestoreCheckpoint) -> Unit,
    scroll: (ReaderEvent.OnScroll) -> Unit,
    openShareApp: (ReaderEvent.OnOpenShareApp) -> Unit,
    openWebBrowser: (ReaderEvent.OnOpenWebBrowser) -> Unit,
    openTranslator: (ReaderEvent.OnOpenTranslator) -> Unit,
    openDictionary: (ReaderEvent.OnOpenDictionary) -> Unit,
    scrollToChapter: (ReaderEvent.OnScrollToChapter) -> Unit,
    showSettingsBottomSheet: (ReaderEvent.OnShowSettingsBottomSheet) -> Unit,
    dismissBottomSheet: (ReaderEvent.OnDismissBottomSheet) -> Unit,
    showChaptersDrawer: (ReaderEvent.OnShowChaptersDrawer) -> Unit,
    dismissDrawer: (ReaderEvent.OnDismissDrawer) -> Unit,
    updateProgress: (ReaderEvent.OnUpdateProgress) -> Unit,
    paginateForward: (ReaderEvent.OnPaginateForward) -> Unit,
    paginateBackward: (ReaderEvent.OnPaginateBackward) -> Unit,
    scrollConsumed: (ReaderEvent.OnScrollConsumed) -> Unit,
    navigateToBookInfo: (ReaderEvent.OnNavigateToBookInfo) -> Unit,
    navigateBack: (ReaderEvent.OnNavigateBack) -> Unit,
    loadNodesBefore: suspend (NodeId, Int) -> List<Node>
) {
    ReaderBottomSheet(
        bottomSheet = bottomSheet,
        menuVisibility = menuVisibility,
        dismissBottomSheet = dismissBottomSheet
    )

    when (layoutState) {
        is LayoutState.Loading -> {
            ReaderLoadingPlaceholder(navigateBack)
        }

        is LayoutState.Parsing -> {
            ReaderParsingPlaceholder(layoutState, navigateBack)
        }

        is LayoutState.Saving -> {
            ReaderSavingPlaceholder(navigateBack)
        }

        is LayoutState.Error -> {
            ReaderErrorPlaceholder(layoutState, retry, navigateBack)
        }

        is LayoutState.Showing -> {
            ReaderScaffold(
                layout = layout,
                nodes = nodes,
                book = book,
                paginating = paginating,
                scrollTarget = scrollTarget,
                currentChapter = currentChapter,
                currentChapterString = currentChapterString,
                chaptersEmpty = chaptersEmpty,
                nestedScrollConnection = nestedScrollConnection,
                fastColorPresetChange = fastColorPresetChange,
                perceptionExpander = perceptionExpander,
                perceptionExpanderPadding = perceptionExpanderPadding,
                perceptionExpanderThickness = perceptionExpanderThickness,
                horizontalLimiter = horizontalLimiter,
                horizontalLimiterHeight = horizontalLimiterHeight,
                horizontalLimiterVerticalOffset = horizontalLimiterVerticalOffset,
                horizontalLimiterRuler = horizontalLimiterRuler,
                horizontalLimiterRulerThickness = horizontalLimiterRulerThickness,
                horizontalLimiterDimming = horizontalLimiterDimming,
                currentChapterProgress = currentChapterProgress,
                checkpoints = checkpoints,
                showMenu = showMenu,
                lockMenu = lockMenu,
                contentPadding = contentPadding,
                verticalPadding = verticalPadding,
                horizontalGesture = horizontalGesture,
                horizontalGestureScroll = horizontalGestureScroll,
                horizontalGestureSensitivity = horizontalGestureSensitivity,
                horizontalGestureAlphaAnim = horizontalGestureAlphaAnim,
                horizontalGesturePullAnim = horizontalGesturePullAnim,
                horizontalGestureDisableScrolling = horizontalGestureDisableScrolling,
                highlightedReading = highlightedReading,
                highlightedReadingThickness = highlightedReadingThickness,
                progress = progress,
                progressBar = progressBar,
                progressBarPadding = progressBarPadding,
                progressBarAlignment = progressBarAlignment,
                progressBarFontSize = progressBarFontSize,
                paragraphTextStyle = paragraphTextStyle,
                boldFontThickness = boldFontThickness,
                header1TextStyle = header1TextStyle,
                header2TextStyle = header2TextStyle,
                header3TextStyle = header3TextStyle,
                header4TextStyle = header4TextStyle,
                header5TextStyle = header5TextStyle,
                header6TextStyle = header6TextStyle,
                paragraphHeight = paragraphHeight,
                sidePadding = sidePadding,
                bottomBarPadding = bottomBarPadding,
                backgroundColor = backgroundColor,
                fontColor = fontColor,
                images = images,
                imagesCaptions = imagesCaptions,
                imagesCornersRoundness = imagesCornersRoundness,
                imagesAlignment = imagesAlignment,
                imagesWidth = imagesWidth,
                imagesColorEffects = imagesColorEffects,
                imagesOriginalSize = imagesOriginalSize,
                horizontalAlignment = horizontalAlignment,
                doubleClickTranslation = doubleClickTranslation,
                switchColorPreset = switchColorPreset,
                menuVisibility = menuVisibility,
                leave = leave,
                restoreCheckpoint = restoreCheckpoint,
                scroll = scroll,
                openShareApp = openShareApp,
                openWebBrowser = openWebBrowser,
                openTranslator = openTranslator,
                openDictionary = openDictionary,
                showSettingsBottomSheet = showSettingsBottomSheet,
                showChaptersDrawer = showChaptersDrawer,
                updateProgress = updateProgress,
                paginateForward = paginateForward,
                paginateBackward = paginateBackward,
                scrollConsumed = scrollConsumed,
                navigateBack = navigateBack,
                navigateToBookInfo = navigateToBookInfo,
                loadNodesBefore = loadNodesBefore
            )
        }
    }

    ReaderDrawer(
        drawer = drawer,
        chapters = chapters,
        currentChapter = currentChapter,
        currentChapterProgress = currentChapterProgress,
        scrollToChapter = scrollToChapter,
        dismissDrawer = dismissDrawer
    )

    ReaderBackHandler(
        leave = leave,
        navigateBack = navigateBack
    )
}
