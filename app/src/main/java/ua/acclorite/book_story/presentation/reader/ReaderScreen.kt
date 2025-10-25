/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.reader

import android.content.pm.ActivityInfo
import android.os.Parcelable
import android.view.WindowManager
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.systemBarsIgnoringVisibility
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.parcelize.Parcelize
import ua.acclorite.book_story.core.helpers.calculateProgress
import ua.acclorite.book_story.presentation.navigator.Screen
import ua.acclorite.book_story.presentation.reader.model.ReaderColorEffects
import ua.acclorite.book_story.presentation.reader.model.ReaderProgressCount
import ua.acclorite.book_story.presentation.reader.model.ReaderTextAlignment
import ua.acclorite.book_story.presentation.settings.SettingsModel
import ua.acclorite.book_story.ui.common.helpers.LocalActivity
import ua.acclorite.book_story.ui.common.helpers.LocalSettings
import ua.acclorite.book_story.ui.common.helpers.setBrightness
import ua.acclorite.book_story.ui.reader.ReaderContent
import ua.acclorite.book_story.ui.reader.ReaderEffects
import kotlin.math.roundToInt

@Parcelize
data class ReaderScreen(val bookId: Int) : Screen, Parcelable {

    companion object {
        const val CHAPTERS_DRAWER = "chapters_drawer"
        const val SETTINGS_BOTTOM_SHEET = "settings_bottom_sheet"
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    override fun Content() {
        val screenModel = hiltViewModel<ReaderModel>()
        val settingsModel = hiltViewModel<SettingsModel>()
        val settings = LocalSettings.current

        val state = screenModel.state.collectAsStateWithLifecycle()
        val settingsState = settingsModel.state.collectAsStateWithLifecycle()

        val activity = LocalActivity.current
        val density = LocalDensity.current
        val listState = rememberSaveable(
            state.value.listState,
            saver = LazyListState.Saver
        ) {
            state.value.listState
        }
        val nestedScrollConnection = remember {
            derivedStateOf {
                object : NestedScrollConnection {
                    override fun onPostScroll(
                        consumed: Offset,
                        available: Offset,
                        source: NestedScrollSource
                    ): Offset {
                        consumed.y.let { velocity ->
                            if (velocity in -70f..70f) return@let
                            if (!state.value.showMenu) return@let
                            if (state.value.lockMenu) return@let
                            if (!settings.hideBarsOnFastScroll.lastValue) return@let

                            screenModel.onEvent(
                                ReaderEvent.OnMenuVisibility(
                                    show = false,
                                    saveCheckpoint = false
                                )
                            )
                        }
                        return super.onPostScroll(consumed, available, source)
                    }
                }
            }
        }

        val backgroundColor = animateColorAsState(
            targetValue = settingsState.value.selectedColorPreset.backgroundColor
        )
        val fontColor = animateColorAsState(
            targetValue = settingsState.value.selectedColorPreset.fontColor
        )
        val lineHeight = remember(
            settings.fontSize.value,
            settings.lineHeight.value
        ) {
            (settings.fontSize.lastValue + settings.lineHeight.lastValue).sp
        }
        val letterSpacing = remember(settings.letterSpacing.value) {
            (settings.letterSpacing.lastValue / 100f).em
        }
        val sidePadding = remember(settings.sidePadding.value) {
            (settings.sidePadding.lastValue * 3).dp
        }
        val verticalPadding = remember(settings.verticalPadding.value) {
            (settings.verticalPadding.lastValue * 4.5f).dp
        }
        val paragraphHeight = remember(
            settings.paragraphHeight.value,
            settings.lineHeight.value
        ) {
            ((settings.paragraphHeight.lastValue * 3).dp).coerceAtLeast(
                with(density) { settings.lineHeight.lastValue.sp.toDp().value * 0.5f }.dp
            )
        }
        val fontStyle = remember(settings.italic.value) {
            when (settings.italic.lastValue) {
                true -> FontStyle.Italic
                false -> FontStyle.Normal
            }
        }
        val paragraphIndentation = remember(
            settings.paragraphIndentation.value,
            settings.textAlignment.value
        ) {
            if (
                settings.textAlignment.lastValue == ReaderTextAlignment.CENTER ||
                settings.textAlignment.lastValue == ReaderTextAlignment.END
            ) return@remember 0.sp
            (settings.paragraphIndentation.lastValue * 6).sp
        }
        val perceptionExpanderPadding = remember(
            sidePadding,
            settings.perceptionExpanderPadding.value
        ) {
            sidePadding + (settings.perceptionExpanderPadding.lastValue * 8).dp
        }
        val perceptionExpanderThickness = remember(
            settings.perceptionExpanderThickness.value
        ) {
            (settings.perceptionExpanderThickness.lastValue * 0.25f).dp
        }
        val horizontalGestureSensitivity = remember(settings.horizontalGestureSensitivity.value) {
            (36f + settings.horizontalGestureSensitivity.lastValue * (4f - 36f)).dp
        }
        val highlightedReadingThickness = remember(settings.highlightedReadingThickness.value) {
            when (settings.highlightedReadingThickness.lastValue) {
                2 -> FontWeight.SemiBold
                3 -> FontWeight.Bold
                else -> FontWeight.Medium
            }
        }
        val horizontalAlignment = remember(settings.textAlignment.value) {
            when (settings.textAlignment.lastValue) {
                ReaderTextAlignment.START, ReaderTextAlignment.JUSTIFY -> Alignment.Start
                ReaderTextAlignment.CENTER -> Alignment.CenterHorizontally
                ReaderTextAlignment.END -> Alignment.End
            }
        }
        val imagesWidth = remember(settings.imagesWidth.value) {
            settings.imagesWidth.lastValue.coerceAtLeast(0.01f)
        }
        val imagesCornersRoundness = remember(
            settings.imagesCornersRoundness.value,
            settings.imagesWidth.value
        ) {
            (settings.imagesCornersRoundness.lastValue * 3 * imagesWidth).dp
        }
        val imagesColorEffects = remember(
            settings.imagesColorEffects.value,
            fontColor.value,
            backgroundColor.value
        ) {
            when (settings.imagesColorEffects.lastValue) {
                ReaderColorEffects.OFF -> null

                ReaderColorEffects.GRAYSCALE -> ColorFilter.colorMatrix(
                    ColorMatrix().apply { setToSaturation(0f) }
                )

                ReaderColorEffects.FONT -> ColorFilter.tint(
                    color = fontColor.value,
                    blendMode = BlendMode.Color
                )

                ReaderColorEffects.BACKGROUND -> ColorFilter.tint(
                    color = backgroundColor.value,
                    blendMode = BlendMode.Color
                )
            }
        }
        val progressBarPadding = remember(settings.progressBarPadding.value) {
            (settings.progressBarPadding.lastValue * 3).dp
        }
        val progressBarFontSize = remember(settings.progressBarFontSize.value) {
            (settings.progressBarFontSize.lastValue * 2).sp
        }

        val layoutDirection = LocalLayoutDirection.current
        val cutoutInsets = WindowInsets.displayCutout
        val systemBarsInsets = WindowInsets.systemBarsIgnoringVisibility

        val cutoutInsetsPadding = remember(settings.cutoutPadding.value) {
            derivedStateOf {
                cutoutInsets.asPaddingValues(density = density).run {
                    if (settings.cutoutPadding.lastValue) PaddingValues(
                        top = calculateTopPadding(),
                        start = calculateStartPadding(layoutDirection),
                        end = calculateEndPadding(layoutDirection),
                        bottom = calculateBottomPadding()
                    ) else PaddingValues(0.dp)
                }
            }
        }
        val systemBarsInsetsPadding = remember(settings.fullscreen.value) {
            derivedStateOf {
                systemBarsInsets.asPaddingValues(density = density).run {
                    if (!settings.fullscreen.lastValue) PaddingValues(
                        top = calculateTopPadding(),
                        start = calculateStartPadding(layoutDirection),
                        end = calculateEndPadding(layoutDirection),
                        bottom = calculateBottomPadding()
                    ) else PaddingValues(0.dp)
                }
            }
        }
        val contentPadding = remember(
            cutoutInsetsPadding.value,
            systemBarsInsetsPadding.value
        ) {
            PaddingValues(
                top = systemBarsInsetsPadding.value.calculateTopPadding().run {
                    if (equals(0.dp)) return@run cutoutInsetsPadding.value
                        .calculateTopPadding()
                    this
                },
                start = systemBarsInsetsPadding.value.calculateStartPadding(layoutDirection).run {
                    if (equals(0.dp)) return@run cutoutInsetsPadding.value
                        .calculateStartPadding(layoutDirection)
                    this
                },
                end = systemBarsInsetsPadding.value.calculateEndPadding(layoutDirection).run {
                    if (equals(0.dp)) return@run cutoutInsetsPadding.value
                        .calculateEndPadding(layoutDirection)
                    this
                },
                bottom = systemBarsInsetsPadding.value.calculateBottomPadding().run {
                    if (equals(0.dp)) return@run cutoutInsetsPadding.value
                        .calculateBottomPadding()
                    this
                }
            )
        }
        val bottomBarPadding = remember(settings.bottomBarPadding.value) {
            (settings.bottomBarPadding.lastValue * 4f).dp
        }

        val bookProgress = remember(
            state.value.book.progress,
            state.value.text,
            settings.progressCount.value
        ) {
            when (settings.progressCount.lastValue) {
                ReaderProgressCount.PERCENTAGE -> {
                    "${state.value.book.progress.calculateProgress(2)}%"
                }

                ReaderProgressCount.QUANTITY -> {
                    val index =
                        (state.value.book.progress * state.value.text.lastIndex + 1).roundToInt()
                    "$index / ${state.value.text.size}"
                }
            }
        }
        val chapterProgress = remember(
            state.value.text,
            state.value.book.progress,
            state.value.currentChapter,
            state.value.currentChapterProgress,
            settings.progressCount.value
        ) {
            if (state.value.currentChapter == null) return@remember ""
            when (settings.progressCount.lastValue) {
                ReaderProgressCount.PERCENTAGE -> {
                    " (${state.value.currentChapterProgress.calculateProgress(2)}%)"
                }

                ReaderProgressCount.QUANTITY -> {
                    val (index, length) = screenModel.findChapterIndexAndLength(
                        (state.value.book.progress * state.value.text.lastIndex).roundToInt()
                    ).apply { if (first == -1 && second == -1) return@remember "" }
                    " (${index} / ${length})"
                }
            }
        }
        val progress = remember(bookProgress, chapterProgress) {
            "${bookProgress}${chapterProgress}"
        }

        LaunchedEffect(Unit) {
            screenModel.init(bookId = bookId)
        }
        LaunchedEffect(settings.fullscreen.value) {
            screenModel.onEvent(
                ReaderEvent.OnMenuVisibility(
                    show = state.value.showMenu,
                    saveCheckpoint = false
                )
            )
        }
        LaunchedEffect(listState) {
            screenModel.updateProgress(listState)
        }

        DisposableEffect(settings.screenOrientation.value) {
            activity.requestedOrientation = settings.screenOrientation.lastValue.code
            onDispose {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }
        }
        DisposableEffect(
            settings.screenBrightness.value,
            settings.customScreenBrightness.value
        ) {
            when (settings.customScreenBrightness.lastValue) {
                true -> activity.setBrightness(brightness = settings.screenBrightness.lastValue)
                false -> activity.setBrightness(brightness = null)
            }

            onDispose {
                activity.setBrightness(brightness = null)
            }
        }
        DisposableEffect(settings.keepScreenOn.value) {
            when (settings.keepScreenOn.lastValue) {
                true -> activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                false -> activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
            onDispose {
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }

        DisposableEffect(Unit) {
            onDispose {
                screenModel.clearAsync()
                WindowCompat.getInsetsController(
                    activity.window,
                    activity.window.decorView
                ).show(WindowInsetsCompat.Type.systemBars())
            }
        }

        ReaderEffects(
            effects = screenModel.effects,
            book = state.value.book,
            fullscreen = settings.fullscreen.value
        )

        ReaderContent(
            book = state.value.book,
            text = state.value.text,
            bottomSheet = state.value.bottomSheet,
            drawer = state.value.drawer,
            listState = listState,
            currentChapter = state.value.currentChapter,
            nestedScrollConnection = nestedScrollConnection.value,
            fastColorPresetChange = settings.fastColorPresetChange.value,
            perceptionExpander = settings.perceptionExpander.value,
            perceptionExpanderPadding = perceptionExpanderPadding,
            perceptionExpanderThickness = perceptionExpanderThickness,
            currentChapterProgress = state.value.currentChapterProgress,
            isLoading = state.value.isLoading,
            errorMessage = state.value.errorMessage,
            checkpoints = state.value.checkpoints,
            showMenu = state.value.showMenu,
            lockMenu = state.value.lockMenu,
            contentPadding = contentPadding,
            verticalPadding = verticalPadding,
            horizontalGesture = settings.horizontalGesture.value,
            horizontalGestureScroll = settings.horizontalGestureScroll.value,
            horizontalGestureSensitivity = horizontalGestureSensitivity,
            horizontalGestureAlphaAnim = settings.horizontalGestureAlphaAnim.value,
            horizontalGesturePullAnim = settings.horizontalGesturePullAnim.value,
            horizontalGestureDisableScrolling = settings.horizontalGestureDisableScrolling.value,
            highlightedReading = settings.highlightedReading.value,
            highlightedReadingThickness = highlightedReadingThickness,
            progress = progress,
            progressBar = settings.progressBar.value,
            progressBarPadding = progressBarPadding,
            progressBarAlignment = settings.progressBarAlignment.value,
            progressBarFontSize = progressBarFontSize,
            paragraphHeight = paragraphHeight,
            sidePadding = sidePadding,
            bottomBarPadding = bottomBarPadding,
            backgroundColor = backgroundColor.value,
            fontColor = fontColor.value,
            images = settings.images.value,
            imagesCaptions = settings.imagesCaptions.value,
            imagesCornersRoundness = imagesCornersRoundness,
            imagesAlignment = settings.imagesAlignment.value,
            imagesWidth = imagesWidth,
            imagesColorEffects = imagesColorEffects,
            fontFamily = settings.fontFamily.value,
            lineHeight = lineHeight,
            fontThickness = settings.fontThickness.value,
            fontStyle = fontStyle,
            chapterTitleAlignment = settings.chapterTitleAlignment.value,
            textAlignment = settings.textAlignment.value,
            horizontalAlignment = horizontalAlignment,
            fontSize = settings.fontSize.value.sp,
            letterSpacing = letterSpacing,
            paragraphIndentation = paragraphIndentation,
            doubleClickTranslation = settings.doubleClickTranslation.value,
            switchColorPreset = settingsModel::onEvent,
            leave = screenModel::onEvent,
            restoreCheckpoint = screenModel::onEvent,
            scroll = screenModel::onEvent,
            changeProgress = screenModel::onEvent,
            menuVisibility = screenModel::onEvent,
            openShareApp = screenModel::onEvent,
            openWebBrowser = screenModel::onEvent,
            openTranslator = screenModel::onEvent,
            openDictionary = screenModel::onEvent,
            scrollToChapter = screenModel::onEvent,
            showSettingsBottomSheet = screenModel::onEvent,
            dismissBottomSheet = screenModel::onEvent,
            showChaptersDrawer = screenModel::onEvent,
            dismissDrawer = screenModel::onEvent,
            navigateBack = screenModel::onEvent,
            navigateToBookInfo = screenModel::onEvent
        )
    }
}