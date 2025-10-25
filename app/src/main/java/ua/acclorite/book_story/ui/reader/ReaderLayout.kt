/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.reader

import android.os.Build
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.reader.ReaderText
import ua.acclorite.book_story.presentation.reader.ReaderEvent
import ua.acclorite.book_story.presentation.reader.model.ReaderFontThickness
import ua.acclorite.book_story.presentation.reader.model.ReaderHorizontalGesture
import ua.acclorite.book_story.presentation.reader.model.ReaderTextAlignment
import ua.acclorite.book_story.ui.common.components.common.AnimatedVisibility
import ua.acclorite.book_story.ui.common.components.common.LazyColumnWithScrollbar
import ua.acclorite.book_story.ui.common.components.common.SelectionContainer
import ua.acclorite.book_story.ui.common.components.common.SpacedItem
import ua.acclorite.book_story.ui.common.helpers.LocalActivity
import ua.acclorite.book_story.ui.common.helpers.noRippleClickable
import ua.acclorite.book_story.ui.common.helpers.showToast
import ua.acclorite.book_story.ui.reader.model.FontWithName
import ua.acclorite.book_story.ui.theme.model.HorizontalAlignment

@Composable
fun ReaderLayout(
    text: List<ReaderText>,
    listState: LazyListState,
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
    isLoading: Boolean,
    showMenu: Boolean,
    menuVisibility: (ReaderEvent.OnMenuVisibility) -> Unit,
    openShareApp: (ReaderEvent.OnOpenShareApp) -> Unit,
    openWebBrowser: (ReaderEvent.OnOpenWebBrowser) -> Unit,
    openTranslator: (ReaderEvent.OnOpenTranslator) -> Unit,
    openDictionary: (ReaderEvent.OnOpenDictionary) -> Unit
) {
    val activity = LocalActivity.current
    SelectionContainer(
        onCopyRequested = {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                activity.getString(R.string.copied)
                    .showToast(context = activity, longToast = false)
            }
        },
        onShareRequested = { textToShare ->
            openShareApp(
                ReaderEvent.OnOpenShareApp(
                    textToShare = textToShare
                )
            )
        },
        onWebSearchRequested = { textToSearch ->
            openWebBrowser(
                ReaderEvent.OnOpenWebBrowser(
                    textToSearch = textToSearch
                )
            )
        },
        onTranslateRequested = { textToTranslate ->
            openTranslator(
                ReaderEvent.OnOpenTranslator(
                    textToTranslate = textToTranslate,
                    translateWholeParagraph = false
                )
            )
        },
        onDictionaryRequested = { textToDefine ->
            openDictionary(
                ReaderEvent.OnOpenDictionary(
                    textToDefine = textToDefine
                )
            )
        }
    ) { toolbarHidden ->
        Column(
            Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .then(
                    if (!isLoading && toolbarHidden) {
                        Modifier.noRippleClickable(
                            onClick = {
                                menuVisibility(
                                    ReaderEvent.OnMenuVisibility(
                                        show = !showMenu,
                                        saveCheckpoint = true
                                    )
                                )
                            }
                        )
                    } else Modifier
                )
                .padding(contentPadding)
                .padding(vertical = verticalPadding)
                .readerHorizontalGesture(
                    listState = listState,
                    horizontalGesture = horizontalGesture,
                    horizontalGestureScroll = horizontalGestureScroll,
                    horizontalGestureSensitivity = horizontalGestureSensitivity,
                    horizontalGestureAlphaAnim = horizontalGestureAlphaAnim,
                    horizontalGesturePullAnim = horizontalGesturePullAnim,
                    isLoading = isLoading
                )
        ) {
            LazyColumnWithScrollbar(
                state = listState,
                enableScrollbar = false,
                parentModifier = Modifier.weight(1f),
                modifier = Modifier.fillMaxSize(),
                userScrollEnabled = !horizontalGestureDisableScrolling,
                contentPadding = PaddingValues(
                    top = (WindowInsets.displayCutout.asPaddingValues()
                        .calculateTopPadding() + paragraphHeight)
                        .coerceAtLeast(18.dp),
                    bottom = (WindowInsets.displayCutout.asPaddingValues()
                        .calculateBottomPadding() + paragraphHeight)
                        .coerceAtLeast(18.dp),
                )
            ) {
                itemsIndexed(
                    text,
                    key = { index, _ -> index }
                ) { index, entry ->
                    val previousEntry = text.getOrNull(index - 1)

                    when {
                        !images && (entry is ReaderText.Image || previousEntry is ReaderText.Image) -> return@itemsIndexed
                        !imagesCaptions && previousEntry is ReaderText.Image -> return@itemsIndexed
                        else -> {
                            SpacedItem(
                                index = index,
                                spacing = paragraphHeight
                            ) {
                                ReaderLayoutText(
                                    showMenu = showMenu,
                                    entry = entry,
                                    imagesCornersRoundness = imagesCornersRoundness,
                                    imagesAlignment = imagesAlignment,
                                    imagesWidth = imagesWidth,
                                    imagesColorEffects = imagesColorEffects,
                                    fontFamily = fontFamily,
                                    fontColor = fontColor,
                                    lineHeight = lineHeight,
                                    fontThickness = fontThickness,
                                    fontStyle = fontStyle,
                                    chapterTitleAlignment = chapterTitleAlignment,
                                    textAlignment = textAlignment,
                                    horizontalAlignment = horizontalAlignment,
                                    fontSize = fontSize,
                                    letterSpacing = letterSpacing,
                                    sidePadding = sidePadding,
                                    paragraphIndentation = paragraphIndentation,
                                    doubleClickTranslation = doubleClickTranslation,
                                    highlightedReading = highlightedReading,
                                    highlightedReadingThickness = highlightedReadingThickness,
                                    toolbarHidden = toolbarHidden,
                                    openTranslator = openTranslator,
                                    menuVisibility = menuVisibility
                                )
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = !showMenu && progressBar,
                enter = slideInVertically { it } + expandVertically(),
                exit = slideOutVertically { it } + shrinkVertically()
            ) {
                ReaderProgressBar(
                    progress = progress,
                    progressBarPadding = progressBarPadding,
                    progressBarAlignment = progressBarAlignment,
                    progressBarFontSize = progressBarFontSize,
                    fontColor = fontColor,
                    sidePadding = sidePadding
                )
            }
        }
    }
}