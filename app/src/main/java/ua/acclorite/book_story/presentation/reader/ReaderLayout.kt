package ua.acclorite.book_story.presentation.reader

import android.os.Build
import androidx.compose.foundation.background
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
import ua.acclorite.book_story.domain.reader.FontWithName
import ua.acclorite.book_story.domain.reader.ReaderHorizontalGesture
import ua.acclorite.book_story.domain.reader.ReaderImagesAlignment
import ua.acclorite.book_story.domain.reader.ReaderText
import ua.acclorite.book_story.domain.reader.ReaderTextAlignment
import ua.acclorite.book_story.presentation.core.components.common.LazyColumnWithScrollbar
import ua.acclorite.book_story.presentation.core.components.common.SelectionContainer
import ua.acclorite.book_story.presentation.core.components.common.SpacedItem
import ua.acclorite.book_story.presentation.core.util.LocalActivity
import ua.acclorite.book_story.presentation.core.util.noRippleClickable
import ua.acclorite.book_story.presentation.core.util.showToast
import ua.acclorite.book_story.ui.reader.ReaderEvent

@Composable
fun ReaderLayout(
    text: List<ReaderText>,
    listState: LazyListState,
    contentPadding: PaddingValues,
    verticalPadding: Dp,
    horizontalGesture: ReaderHorizontalGesture,
    horizontalGestureScroll: Float,
    horizontalGestureSensitivity: Dp,
    highlightedReading: Boolean,
    highlightedReadingThickness: FontWeight,
    paragraphHeight: Dp,
    sidePadding: Dp,
    backgroundColor: Color,
    fontColor: Color,
    images: Boolean,
    imagesCornersRoundness: Dp,
    imagesAlignment: ReaderImagesAlignment,
    imagesWidth: Float,
    imagesColorEffects: ColorFilter?,
    fontFamily: FontWithName,
    lineHeight: TextUnit,
    fontStyle: FontStyle,
    chapterTitleAlignment: ReaderTextAlignment,
    textAlignment: ReaderTextAlignment,
    horizontalAlignment: Alignment.Horizontal,
    fontSize: TextUnit,
    letterSpacing: TextUnit,
    paragraphIndentation: TextUnit,
    doubleClickTranslation: Boolean,
    fullscreenMode: Boolean,
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
                    textToShare = textToShare,
                    activity = activity
                )
            )
        },
        onWebSearchRequested = { textToSearch ->
            openWebBrowser(
                ReaderEvent.OnOpenWebBrowser(
                    textToSearch = textToSearch,
                    activity = activity
                )
            )
        },
        onTranslateRequested = { textToTranslate ->
            openTranslator(
                ReaderEvent.OnOpenTranslator(
                    textToTranslate = textToTranslate,
                    translateWholeParagraph = false,
                    activity = activity
                )
            )
        },
        onDictionaryRequested = { textToDefine ->
            openDictionary(
                ReaderEvent.OnOpenDictionary(
                    textToDefine,
                    activity = activity
                )
            )
        }
    ) { toolbarHidden ->
        LazyColumnWithScrollbar(
            state = listState,
            enableScrollbar = false,
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .then(
                    if (!isLoading && toolbarHidden) {
                        Modifier.noRippleClickable(
                            onClick = {
                                menuVisibility(
                                    ReaderEvent.OnMenuVisibility(
                                        show = !showMenu,
                                        fullscreenMode = fullscreenMode,
                                        saveCheckpoint = true,
                                        activity = activity
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
                    isLoading = isLoading
                ),
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
                key = { index, entry -> index }
            ) { index, entry ->
                when {
                    !images && entry is ReaderText.Image -> return@itemsIndexed
                    else -> {
                        SpacedItem(
                            index = index,
                            spacing = paragraphHeight
                        ) {
                            ReaderLayoutText(
                                activity = activity,
                                showMenu = showMenu,
                                entry = entry,
                                imagesCornersRoundness = imagesCornersRoundness,
                                imagesAlignment = imagesAlignment,
                                imagesWidth = imagesWidth,
                                imagesColorEffects = imagesColorEffects,
                                fontFamily = fontFamily,
                                fontColor = fontColor,
                                lineHeight = lineHeight,
                                fontStyle = fontStyle,
                                chapterTitleAlignment = chapterTitleAlignment,
                                textAlignment = textAlignment,
                                horizontalAlignment = horizontalAlignment,
                                fontSize = fontSize,
                                letterSpacing = letterSpacing,
                                sidePadding = sidePadding,
                                paragraphIndentation = paragraphIndentation,
                                fullscreenMode = fullscreenMode,
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
    }
}