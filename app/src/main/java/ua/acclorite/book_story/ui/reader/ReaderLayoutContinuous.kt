/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.reader

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.book.Book
import ua.acclorite.book_story.domain.model.book.Node
import ua.acclorite.book_story.domain.model.book.NodeContent
import ua.acclorite.book_story.presentation.reader.ReaderEvent
import ua.acclorite.book_story.presentation.reader.ScrollTarget
import ua.acclorite.book_story.presentation.reader.model.ReaderHorizontalGesture
import ua.acclorite.book_story.ui.common.components.common.AnimatedVisibility
import ua.acclorite.book_story.ui.common.components.common.LazyColumnWithScrollbar
import ua.acclorite.book_story.ui.common.components.common.SelectionContainer
import ua.acclorite.book_story.ui.common.components.common.SpacedItem
import ua.acclorite.book_story.ui.common.components.common.getHighlightedText
import ua.acclorite.book_story.ui.common.components.progress_indicator.CircularProgressIndicator
import ua.acclorite.book_story.ui.common.helpers.LocalActivity
import ua.acclorite.book_story.ui.common.helpers.noRippleClickable
import ua.acclorite.book_story.ui.common.helpers.showToast
import ua.acclorite.book_story.ui.reader.model.ReaderNode
import ua.acclorite.book_story.ui.theme.DefaultTransition
import ua.acclorite.book_story.ui.theme.model.HorizontalAlignment

private const val PRELOAD_THRESHOLD = 30

@OptIn(FlowPreview::class)
@SuppressLint("FrequentlyChangingValue")
@Composable
fun ReaderLayoutContinuous(
    book: Book,
    nodes: List<Node>,
    paginating: Boolean,
    scrollTarget: ScrollTarget?,
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
    paragraphTextStyle: TextStyle,
    header1TextStyle: TextStyle,
    header2TextStyle: TextStyle,
    header3TextStyle: TextStyle,
    header4TextStyle: TextStyle,
    header5TextStyle: TextStyle,
    header6TextStyle: TextStyle,
    paragraphHeight: Dp,
    sidePadding: Dp,
    backgroundColor: Color,
    fontColor: Color,
    images: Boolean,
    imagesCaptions: Boolean,
    imagesCornersRoundness: Dp,
    imagesAlignment: HorizontalAlignment,
    imagesWidth: Float,
    imagesOriginalSize: Boolean,
    imagesColorEffects: ColorFilter?,
    horizontalAlignment: Alignment.Horizontal,
    doubleClickTranslation: Boolean,
    showMenu: Boolean,
    menuVisibility: (ReaderEvent.OnMenuVisibility) -> Unit,
    openShareApp: (ReaderEvent.OnOpenShareApp) -> Unit,
    openWebBrowser: (ReaderEvent.OnOpenWebBrowser) -> Unit,
    openTranslator: (ReaderEvent.OnOpenTranslator) -> Unit,
    openDictionary: (ReaderEvent.OnOpenDictionary) -> Unit,
    updateProgress: (ReaderEvent.OnUpdateProgress) -> Unit,
    paginateForward: (ReaderEvent.OnPaginateForward) -> Unit,
    paginateBackward: (ReaderEvent.OnPaginateBackward) -> Unit,
    scrollConsumed: (ReaderEvent.OnScrollConsumed) -> Unit
) {
    val activity = LocalActivity.current
    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    val layoutDirection = LocalLayoutDirection.current
    val listState = rememberLazyListState()
    val textMeasurer = rememberTextMeasurer()

    val isInitialLoading = remember { mutableStateOf(true) }
    val readerNodes = remember(
        nodes,
        density,
        highlightedReading,
        highlightedReadingThickness
    ) {
        nodes.map { node ->
            when (val nodeContent = node.content) {
                is NodeContent.Header -> ReaderNode.Header(
                    originalNode = node,
                    title = getHighlightedText(
                        highlight = highlightedReading,
                        text = buildAnnotatedString { append(nodeContent.title) },
                        highlightThickness = highlightedReadingThickness
                    ),
                    level = nodeContent.level
                )

                is NodeContent.Paragraph -> ReaderNode.Paragraph(
                    originalNode = node,
                    text = getHighlightedText(
                        highlight = highlightedReading,
                        text = buildAnnotatedString {
                            append(nodeContent.text)

                            nodeContent.styles.forEach { style ->
                                when (style.style) {
                                    is NodeContent.TextStyle.Bold -> addStyle(
                                        SpanStyle(fontWeight = FontWeight.Bold),
                                        style.start,
                                        style.end
                                    )

                                    is NodeContent.TextStyle.Italic -> addStyle(
                                        SpanStyle(fontStyle = FontStyle.Italic),
                                        style.start,
                                        style.end
                                    )

                                    is NodeContent.TextStyle.Link -> addLink(
                                        LinkAnnotation.Url(
                                            style.style.link,
                                            TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline))
                                        ),
                                        style.start,
                                        style.end
                                    )
                                }
                            }
                        },
                        highlightThickness = highlightedReadingThickness
                    )
                )

                is NodeContent.Image -> ReaderNode.Image(
                    originalNode = node,
                    content = nodeContent.content,
                    caption = nodeContent.caption,
                    originalWidth = with(density) { nodeContent.width.toDp() },
                    originalHeight = with(density) { nodeContent.height.toDp() },
                    aspectRatio = nodeContent.aspectRatio
                )

                is NodeContent.Separator -> ReaderNode.Separator(originalNode = node)
            }
        }
    }
    val reachedFirst = remember(readerNodes) {
        if (readerNodes.isEmpty()) return@remember false
        readerNodes.first().node.id.id <= 0
    }
    val reachedLast = remember(readerNodes, book.progress.totalNodes) {
        if (readerNodes.isEmpty() || book.progress.totalNodes <= 0) return@remember false
        readerNodes.last().node.id.id >= (book.progress.totalNodes - 1)
    }
    val constraints = remember(density, windowInfo, contentPadding, sidePadding) {
        Constraints(
            maxWidth = with(density) {
                windowInfo.containerSize.width.minus(
                    contentPadding.calculateLeftPadding(layoutDirection).toPx()
                ).minus(
                    contentPadding.calculateRightPadding(layoutDirection).toPx()
                ).minus(
                    sidePadding.toPx() * 2
                )
            }.toInt()
        )
    }

    val contentAlpha by animateFloatAsState(
        targetValue = if (scrollTarget != null || isInitialLoading.value) 0f else 1f,
        animationSpec = tween(durationMillis = 150),
        label = "continuous_reader_fade"
    )

    // Target scrolling (initial load & programmatic jumps)
    LaunchedEffect(
        scrollTarget,
        readerNodes,
        textMeasurer,
        paragraphTextStyle,
        header1TextStyle,
        header2TextStyle,
        header3TextStyle,
        header4TextStyle,
        header5TextStyle,
        header6TextStyle,
        constraints
    ) {
        val target = scrollTarget ?: return@LaunchedEffect
        if (readerNodes.isEmpty()) return@LaunchedEffect

        val index = readerNodes.indexOfFirst { it.node.id == target.nodeId }
        if (index != -1) {
            val readerNode = readerNodes[index]
            val offset = readerNode.getNodeVerticalOffsetPx(
                nodeIntraOffset = target.nodeIntraOffset,
                textMeasurer = textMeasurer,
                constraints = constraints,
                paragraphTextStyle = paragraphTextStyle,
                header1TextStyle = header1TextStyle,
                header2TextStyle = header2TextStyle,
                header3TextStyle = header3TextStyle,
                header4TextStyle = header4TextStyle,
                header5TextStyle = header5TextStyle,
                header6TextStyle = header6TextStyle
            )

            try {
                listState.scrollToItem(
                    index = index,
                    scrollOffset = offset
                )
            } finally {
                isInitialLoading.value = false
                scrollConsumed(ReaderEvent.OnScrollConsumed)
            }
        }
    }

    val currentScrollTarget by rememberUpdatedState(scrollTarget)
    val currentReaderNodes by rememberUpdatedState(readerNodes)
    val currentPaginating by rememberUpdatedState(paginating)

    // Progress tracking
    LaunchedEffect(listState) {
        snapshotFlow {
            Pair(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset)
        }.distinctUntilChanged().debounce(200)
            .collectLatest { (itemIndex, itemOffset) ->
                if (currentScrollTarget != null || isInitialLoading.value) return@collectLatest
                val nodesList = currentReaderNodes
                val currentNode = nodesList.getOrNull(itemIndex) ?: return@collectLatest

                val currentNodeId = currentNode.node.id
                val currentNodeIntraOffset = currentNode.getNodeIntraOffset(
                    nodeVerticalOffsetPx = itemOffset,
                    textMeasurer = textMeasurer,
                    constraints = constraints,
                    paragraphTextStyle = paragraphTextStyle,
                    header1TextStyle = header1TextStyle,
                    header2TextStyle = header2TextStyle,
                    header3TextStyle = header3TextStyle,
                    header4TextStyle = header4TextStyle,
                    header5TextStyle = header5TextStyle,
                    header6TextStyle = header6TextStyle
                )

                updateProgress(ReaderEvent.OnUpdateProgress(currentNodeId, currentNodeIntraOffset))
            }
    }

    // Pagination
    LaunchedEffect(listState, paginating) {
        snapshotFlow {
            listState.firstVisibleItemIndex
        }.distinctUntilChanged()
            .collectLatest { itemIndex ->
                if (currentPaginating || currentScrollTarget != null || isInitialLoading.value) return@collectLatest
                val nodesList = currentReaderNodes
                if (nodesList.isEmpty()) return@collectLatest

                if (itemIndex < PRELOAD_THRESHOLD && !reachedFirst) {
                    val firstNode = nodesList.firstOrNull() ?: return@collectLatest
                    paginateBackward(ReaderEvent.OnPaginateBackward(firstNode.node.id))
                } else if (itemIndex > nodesList.size - PRELOAD_THRESHOLD && !reachedLast) {
                    val lastNode = nodesList.lastOrNull() ?: return@collectLatest
                    paginateForward(ReaderEvent.OnPaginateForward(lastNode.node.id))
                }
            }
    }

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
                    if (!isInitialLoading.value && toolbarHidden) {
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
                    isLoading = isInitialLoading.value
                )
        ) {
            LazyColumnWithScrollbar(
                state = listState,
                enableScrollbar = false,
                parentModifier = Modifier
                    .weight(1f)
                    .graphicsLayer { alpha = contentAlpha },
                modifier = Modifier.fillMaxSize(),
                userScrollEnabled = !horizontalGestureDisableScrolling && !isInitialLoading.value,
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
                    readerNodes,
                    key = { _, readerNode -> readerNode.node.id.id }
                ) { index, readerNode ->
                    Column {
                        AnimatedVisibility(
                            visible = index == 0 && !reachedFirst,
                            enter = fadeIn(tween(0)),
                            exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
                        ) {
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = paragraphHeight),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(36.dp),
                                    color = fontColor,
                                    trackColor = fontColor.copy(0.3f)
                                )
                            }
                        }

                        when {
                            readerNode is ReaderNode.Image && !images -> {}
                            else -> SpacedItem(
                                space = index != readerNodes.lastIndex,
                                spacing = paragraphHeight
                            ) {
                                ReaderLayoutContent(
                                    readerNode = readerNode,
                                    paragraphTextStyle = paragraphTextStyle,
                                    header1TextStyle = header1TextStyle,
                                    header2TextStyle = header2TextStyle,
                                    header3TextStyle = header3TextStyle,
                                    header4TextStyle = header4TextStyle,
                                    header5TextStyle = header5TextStyle,
                                    header6TextStyle = header6TextStyle,
                                    showMenu = showMenu,
                                    imagesCaptions = imagesCaptions,
                                    imagesCornersRoundness = imagesCornersRoundness,
                                    imagesAlignment = imagesAlignment,
                                    imagesWidth = imagesWidth,
                                    imagesColorEffects = imagesColorEffects,
                                    imagesOriginalSize = imagesOriginalSize,
                                    fontColor = fontColor,
                                    horizontalAlignment = horizontalAlignment,
                                    sidePadding = sidePadding,
                                    doubleClickTranslation = doubleClickTranslation,
                                    toolbarHidden = toolbarHidden,
                                    paragraphHeight = paragraphHeight,
                                    openTranslator = openTranslator,
                                    menuVisibility = menuVisibility
                                )
                            }
                        }

                        if (index == readerNodes.lastIndex && !reachedLast) {
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = paragraphHeight),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(36.dp),
                                    color = fontColor,
                                    trackColor = fontColor.copy(0.3f)
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

    DefaultTransition(
        visible = isInitialLoading.value
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .noRippleClickable() {},
        )
    }
}
