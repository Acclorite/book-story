/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.reader.paged

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.Density
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import ua.acclorite.book_story.domain.model.book.NodeId
import ua.acclorite.book_story.ui.reader.model.ReaderNode
import kotlin.math.min
import kotlin.math.roundToInt

internal data class ReaderPagePosition(val nodeId: NodeId, val offset: Int)

internal data class ReaderPageItem(
    val node: ReaderNode,
    val startOffset: Int,
    val endOffset: Int,
    val height: Int,
    val spaceBefore: Int,
    val text: AnnotatedString? = null,
    val style: TextStyle? = null,
    val width: Int = 0,
    val caption: Boolean = false
)

internal data class ReaderPage(val items: List<ReaderPageItem>) {
    val position: ReaderPagePosition
        get() = items.first().let { ReaderPagePosition(it.node.node.id, it.startOffset) }
}

internal data class ReaderPageConfig(
    val width: Int,
    val height: Int,
    val gap: Int,
    val separatorHeight: Int,
    val paragraphStyle: TextStyle,
    val headerStyles: List<TextStyle>,
    val images: Boolean,
    val imagesCaptions: Boolean,
    val imagesOriginalSize: Boolean,
    val imagesWidth: Float,
    val density: Density
)

internal data class ReaderTextMeasurement(
    val height: Int,
    val lineEnds: List<Int>,
    val lineBottoms: List<Int>
)

internal suspend fun paginateReaderPages(
    nodes: List<ReaderNode>,
    config: ReaderPageConfig,
    measure: (AnnotatedString, TextStyle) -> ReaderTextMeasurement
): List<ReaderPage> {
    if (config.width <= 0 || config.height <= 0) return emptyList()
    val pages = mutableListOf<ReaderPage>()
    val items = mutableListOf<ReaderPageItem>()
    var usedHeight = 0
    var previousWasHeader = false

    fun finishPage() {
        if (items.isEmpty()) return
        pages.add(ReaderPage(items.toList()))
        items.clear()
        usedHeight = 0
    }

    fun add(item: ReaderPageItem) {
        items.add(item)
        usedHeight += item.spaceBefore + item.height
    }

    suspend fun addText(
        node: ReaderNode,
        text: AnnotatedString,
        baseStyle: TextStyle,
        caption: Boolean = false
    ) {
        if (text.isEmpty()) return
        val rangeEnd = text.length
        run {
            var offset = 0
            while (offset < rangeEnd) {
                currentCoroutineContext().ensureActive()
                var style = if (offset == 0) baseStyle else baseStyle.copy(textIndent = TextIndent.None)
                val gap = if (items.isEmpty() || offset > 0) 0 else config.gap
                val available = config.height - usedHeight - gap
                if (available <= 0) {
                    finishPage()
                    continue
                }

                val remainingText = text.subSequence(offset, rangeEnd)
                val remaining = measure(remainingText, style)
                var end = rangeEnd
                var measured = remaining
                if (remaining.height > available) {
                    var lines = remaining.lineBottoms.takeWhile { it <= available }.size
                    if (lines == 0 && items.isNotEmpty()) {
                        finishPage()
                        continue
                    }
                    lines = lines.coerceAtLeast(1).coerceAtMost(remaining.lineEnds.size)
                    if (lines == 0) break
                    do {
                        end = (offset + remaining.lineEnds[lines - 1]).coerceIn(offset + 1, rangeEnd)
                        measured = measure(text.subSequence(offset, end), style)
                        if (measured.height <= available || lines == 1) break
                        lines--
                    } while (true)
                    if (measured.height > available && items.isNotEmpty()) {
                        finishPage()
                        continue
                    }
                    // An extremely short window must still fit one line rather than losing text.
                    if (measured.height > config.height) {
                        val scale = config.height.toFloat() / measured.height * 0.95f
                        style = style.copy(
                            fontSize = style.fontSize * scale,
                            lineHeight = style.lineHeight * scale
                        )
                        measured = measure(text.subSequence(offset, end), style)
                    }
                }

                add(
                    ReaderPageItem(
                        node = node,
                        startOffset = if (caption) 0 else offset,
                        endOffset = if (caption) 1 else end,
                        height = measured.height.coerceAtMost(config.height),
                        spaceBefore = gap,
                        text = text.subSequence(offset, end),
                        style = style,
                        caption = caption
                    )
                )
                offset = end
                if (offset < rangeEnd) finishPage()
            }
        }
    }

    nodes.forEach { node ->
        currentCoroutineContext().ensureActive()
        if (node is ReaderNode.Image && !config.images) return@forEach

        when (node) {
            is ReaderNode.Header -> {
                if (!previousWasHeader) finishPage()
                addText(node, node.title, config.headerStyles[node.level.level - 1])
            }

            is ReaderNode.Paragraph -> addText(node, node.text, config.paragraphStyle)

            is ReaderNode.Image -> {
                val originalWidth = with(config.density) { node.originalWidth.toPx() }
                val originalHeight = with(config.density) { node.originalHeight.toPx() }
                val ratio = node.aspectRatio.takeIf { it.isFinite() && it > 0f } ?: 1f
                val desiredWidth = if (config.imagesOriginalSize && originalWidth > 0) {
                    originalWidth
                } else config.width * config.imagesWidth.coerceIn(0.01f, 1f)
                val desiredHeight = if (config.imagesOriginalSize && originalHeight > 0) {
                    originalHeight
                } else desiredWidth / ratio
                val scale = min(
                    1f,
                    min(config.width / desiredWidth, config.height / desiredHeight)
                )
                val width = (desiredWidth * scale).roundToInt().coerceIn(1, config.width)
                val height = (desiredHeight * scale).roundToInt().coerceIn(1, config.height)
                var gap = if (items.isEmpty()) 0 else config.gap
                if (usedHeight + gap + height > config.height) {
                    finishPage()
                    gap = 0
                }
                add(ReaderPageItem(node, 0, 1, height, gap, width = width))
                if (config.imagesCaptions && !node.caption.isNullOrBlank()) {
                    addText(
                        node,
                        AnnotatedString(node.caption),
                        config.paragraphStyle.copy(fontStyle = FontStyle.Italic),
                        caption = true
                    )
                }
            }

            is ReaderNode.Separator -> {
                val height = config.separatorHeight.coerceIn(1, config.height)
                var gap = if (items.isEmpty()) 0 else config.gap
                if (usedHeight + gap + height > config.height) {
                    finishPage()
                    gap = 0
                }
                add(ReaderPageItem(node, 0, 1, height, gap))
            }
        }
        previousWasHeader = node is ReaderNode.Header
    }
    finishPage()
    return pages
}

internal fun findReaderPage(pages: List<ReaderPage>, position: ReaderPagePosition): Int {
    val exact = pages.indexOfFirst { page ->
        page.items.any {
            it.node.node.id == position.nodeId &&
                    position.offset >= it.startOffset && position.offset < it.endOffset
        }
    }
    if (exact >= 0) return exact
    return pages.indexOfLast {
        it.position.nodeId.id < position.nodeId.id ||
                it.position.nodeId == position.nodeId && it.position.offset <= position.offset
    }.coerceAtLeast(0)
}
