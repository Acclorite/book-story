/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.reader

import android.annotation.SuppressLint
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import ua.acclorite.book_story.domain.model.book.Book
import ua.acclorite.book_story.domain.model.book.Chapter
import ua.acclorite.book_story.domain.model.book.Node
import ua.acclorite.book_story.presentation.reader.ReaderEvent
import ua.acclorite.book_story.presentation.reader.ScrollTarget
import ua.acclorite.book_story.presentation.reader.model.Checkpoint
import ua.acclorite.book_story.presentation.reader.model.ReaderHorizontalGesture
import ua.acclorite.book_story.presentation.reader.model.ReaderLayout
import ua.acclorite.book_story.presentation.settings.SettingsEvent
import ua.acclorite.book_story.ui.common.components.common.AnimatedVisibility
import ua.acclorite.book_story.ui.theme.model.HorizontalAlignment

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReaderScaffold(
    layout: ReaderLayout,
    nodes: List<Node>,
    book: Book,
    paginating: Boolean,
    scrollTarget: ScrollTarget?,
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
    leave: (ReaderEvent.OnLeave) -> Unit,
    restoreCheckpoint: (ReaderEvent.OnRestoreCheckpoint) -> Unit,
    scroll: (ReaderEvent.OnScroll) -> Unit,
    openShareApp: (ReaderEvent.OnOpenShareApp) -> Unit,
    openWebBrowser: (ReaderEvent.OnOpenWebBrowser) -> Unit,
    openTranslator: (ReaderEvent.OnOpenTranslator) -> Unit,
    openDictionary: (ReaderEvent.OnOpenDictionary) -> Unit,
    showSettingsBottomSheet: (ReaderEvent.OnShowSettingsBottomSheet) -> Unit,
    showChaptersDrawer: (ReaderEvent.OnShowChaptersDrawer) -> Unit,
    updateProgress: (ReaderEvent.OnUpdateProgress) -> Unit,
    paginateForward: (ReaderEvent.OnPaginateForward) -> Unit,
    paginateBackward: (ReaderEvent.OnPaginateBackward) -> Unit,
    scrollConsumed: (ReaderEvent.OnScrollConsumed) -> Unit,
    navigateToBookInfo: (ReaderEvent.OnNavigateToBookInfo) -> Unit,
    navigateBack: (ReaderEvent.OnNavigateBack) -> Unit
) {
    Scaffold(
        Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            AnimatedVisibility(
                visible = showMenu,
                enter = slideInVertically { -it },
                exit = slideOutVertically { -it }
            ) {
                ReaderTopBar(
                    book = book,
                    currentChapter = currentChapter,
                    currentChapterString = currentChapterString,
                    currentChapterProgress = currentChapterProgress,
                    chaptersEmpty = chaptersEmpty,
                    fastColorPresetChange = fastColorPresetChange,
                    menuShowing = showMenu,
                    lockMenu = lockMenu,
                    leave = leave,
                    switchColorPreset = switchColorPreset,
                    showSettingsBottomSheet = showSettingsBottomSheet,
                    showChaptersDrawer = showChaptersDrawer,
                    navigateBack = navigateBack,
                    navigateToBookInfo = navigateToBookInfo
                )
            }
        },
        bottomBar = {
            AnimatedVisibility(
                modifier = Modifier.fillMaxWidth(),
                visible = showMenu,
                enter = slideInVertically { it },
                exit = slideOutVertically { it }
            ) {
                ReaderBottomBar(
                    book = book,
                    progress = progress,
                    lockMenu = lockMenu,
                    checkpoints = checkpoints,
                    bottomBarPadding = bottomBarPadding,
                    restoreCheckpoint = restoreCheckpoint,
                    scroll = scroll
                )
            }
        }
    ) {
        when (layout) {
            ReaderLayout.CONTINUOUS -> {
                ReaderLayoutContinuous(
                    book = book,
                    nodes = nodes,
                    paginating = paginating,
                    scrollTarget = scrollTarget,
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
                    header1TextStyle = header1TextStyle,
                    header2TextStyle = header2TextStyle,
                    header3TextStyle = header3TextStyle,
                    header4TextStyle = header4TextStyle,
                    header5TextStyle = header5TextStyle,
                    header6TextStyle = header6TextStyle,
                    paragraphHeight = paragraphHeight,
                    sidePadding = sidePadding,
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
                    showMenu = showMenu,
                    menuVisibility = menuVisibility,
                    openShareApp = openShareApp,
                    openWebBrowser = openWebBrowser,
                    openTranslator = openTranslator,
                    openDictionary = openDictionary,
                    updateProgress = updateProgress,
                    paginateForward = paginateForward,
                    paginateBackward = paginateBackward,
                    scrollConsumed = scrollConsumed
                )

                ReaderHorizontalLimiter(
                    horizontalLimiter = horizontalLimiter,
                    horizontalLimiterHeight = horizontalLimiterHeight,
                    horizontalLimiterVerticalOffset = horizontalLimiterVerticalOffset,
                    horizontalLimiterRulerThickness = horizontalLimiterRulerThickness,
                    horizontalLimiterRuler = horizontalLimiterRuler,
                    horizontalLimiterDimming = horizontalLimiterDimming,
                    horizontalLimiterDimmingColor = backgroundColor,
                    horizontalLimiterRulerColor = fontColor
                )
            }
        }

        ReaderPerceptionExpander(
            perceptionExpander = perceptionExpander,
            perceptionExpanderPadding = perceptionExpanderPadding,
            perceptionExpanderThickness = perceptionExpanderThickness,
            perceptionExpanderColor = fontColor
        )
    }
}
