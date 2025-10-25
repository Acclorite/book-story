/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.reader

import android.annotation.SuppressLint
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import ua.acclorite.book_story.domain.model.library.Book
import ua.acclorite.book_story.domain.model.reader.ReaderText
import ua.acclorite.book_story.domain.model.reader.ReaderText.Chapter
import ua.acclorite.book_story.presentation.reader.ReaderEvent
import ua.acclorite.book_story.presentation.reader.model.Checkpoint
import ua.acclorite.book_story.presentation.reader.model.ReaderFontThickness
import ua.acclorite.book_story.presentation.reader.model.ReaderHorizontalGesture
import ua.acclorite.book_story.presentation.reader.model.ReaderTextAlignment
import ua.acclorite.book_story.presentation.settings.SettingsEvent
import ua.acclorite.book_story.ui.common.components.common.AnimatedVisibility
import ua.acclorite.book_story.ui.reader.model.FontWithName
import ua.acclorite.book_story.ui.theme.model.HorizontalAlignment

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReaderScaffold(
    book: Book,
    text: List<ReaderText>,
    listState: LazyListState,
    currentChapter: Chapter?,
    nestedScrollConnection: NestedScrollConnection,
    fastColorPresetChange: Boolean,
    perceptionExpander: Boolean,
    perceptionExpanderPadding: Dp,
    perceptionExpanderThickness: Dp,
    currentChapterProgress: Float,
    isLoading: Boolean,
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
    highlightedReadingThickness: FontWeight,
    progress: String,
    progressBar: Boolean,
    progressBarPadding: Dp,
    progressBarAlignment: HorizontalAlignment,
    progressBarFontSize: TextUnit,
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
    fontFamily: FontWithName,
    lineHeight: TextUnit,
    fontThickness: ReaderFontThickness,
    fontStyle: FontStyle,
    chapterTitleAlignment: ReaderTextAlignment,
    textAlignment: ReaderTextAlignment,
    horizontalAlignment: Alignment.Horizontal,
    fontSize: TextUnit,
    letterSpacing: TextUnit,
    paragraphIndentation: TextUnit,
    doubleClickTranslation: Boolean,
    switchColorPreset: (SettingsEvent.OnSwitchColorPreset) -> Unit,
    menuVisibility: (ReaderEvent.OnMenuVisibility) -> Unit,
    leave: (ReaderEvent.OnLeave) -> Unit,
    restoreCheckpoint: (ReaderEvent.OnRestoreCheckpoint) -> Unit,
    scroll: (ReaderEvent.OnScroll) -> Unit,
    changeProgress: (ReaderEvent.OnChangeProgress) -> Unit,
    openShareApp: (ReaderEvent.OnOpenShareApp) -> Unit,
    openWebBrowser: (ReaderEvent.OnOpenWebBrowser) -> Unit,
    openTranslator: (ReaderEvent.OnOpenTranslator) -> Unit,
    openDictionary: (ReaderEvent.OnOpenDictionary) -> Unit,
    showSettingsBottomSheet: (ReaderEvent.OnShowSettingsBottomSheet) -> Unit,
    showChaptersDrawer: (ReaderEvent.OnShowChaptersDrawer) -> Unit,
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
                    fastColorPresetChange = fastColorPresetChange,
                    currentChapterProgress = currentChapterProgress,
                    isLoading = isLoading,
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
                    text = text,
                    listState = listState,
                    lockMenu = lockMenu,
                    checkpoints = checkpoints,
                    bottomBarPadding = bottomBarPadding,
                    restoreCheckpoint = restoreCheckpoint,
                    scroll = scroll,
                    changeProgress = changeProgress
                )
            }
        }
    ) {
        ReaderLayout(
            text = text,
            listState = listState,
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
            fontFamily = fontFamily,
            lineHeight = lineHeight,
            fontThickness = fontThickness,
            fontStyle = fontStyle,
            chapterTitleAlignment = chapterTitleAlignment,
            textAlignment = textAlignment,
            horizontalAlignment = horizontalAlignment,
            fontSize = fontSize,
            letterSpacing = letterSpacing,
            paragraphIndentation = paragraphIndentation,
            doubleClickTranslation = doubleClickTranslation,
            isLoading = isLoading,
            showMenu = showMenu,
            menuVisibility = menuVisibility,
            openShareApp = openShareApp,
            openWebBrowser = openWebBrowser,
            openTranslator = openTranslator,
            openDictionary = openDictionary
        )

        ReaderPerceptionExpander(
            perceptionExpander = perceptionExpander,
            perceptionExpanderPadding = perceptionExpanderPadding,
            perceptionExpanderThickness = perceptionExpanderThickness,
            perceptionExpanderColor = fontColor
        )

        if (isLoading) {
            ReaderLoadingPlaceholder()
        }
    }
}