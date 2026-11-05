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
import ua.acclorite.book_story.domain.model.book.HeaderSequence
import ua.acclorite.book_story.domain.model.book.NodeContent
import ua.acclorite.book_story.domain.model.book.NodeId
import ua.acclorite.book_story.ui.reader.model.ReaderTableCell
import ua.acclorite.book_story.ui.reader.model.ReaderTableCellPadding
import ua.acclorite.book_story.ui.reader.model.tableCells
import ua.acclorite.book_story.ui.reader.model.tableColumns
import ua.acclorite.book_story.ui.reader.model.tableCellTextWidth
import ua.acclorite.book_story.ui.reader.model.ReaderNode
import ua.acclorite.book_story.ui.reader.textBlockStyle
import ua.acclorite.book_story.ui.reader.textBlockPadding
import androidx.compose.ui.unit.sp
import kotlin.math.min
import kotlin.math.roundToInt

data class ReaderPagePosition(val nodeId: NodeId, val offset: Int)

data class ReaderPageItem(
    val node: ReaderNode,
    val startOffset: Int,
    val endOffset: Int,
    val height: Int,
    val spaceBefore: Int,
    val text: AnnotatedString? = null,
    val style: TextStyle? = null,
    val width: Int = 0,
    val caption: Boolean = false,
    val tableCells: List<ReaderTableCell>? = null
)

data class ReaderPage(val items: List<ReaderPageItem>) {
    val position: ReaderPagePosition
        get() = items.first().let { ReaderPagePosition(it.node.node.id, it.node.toSourceOffset(it.startOffset)) }
}

data class ReaderPageConfig(
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

data class ReaderTextMeasurement(
    val height: Int,
    val lineEnds: List<Int>,
    val lineBottoms: List<Int>
)

suspend fun paginateReaderPages(
    nodes: List<ReaderNode>,
    config: ReaderPageConfig,
    measure: (AnnotatedString, TextStyle, Int) -> ReaderTextMeasurement
): List<ReaderPage> {
    if (config.width <= 0 || config.height <= 0) return emptyList()
    val pages = mutableListOf<ReaderPage>()
    val items = mutableListOf<ReaderPageItem>()
    var usedHeight = 0
    val headerSequence = HeaderSequence()

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
        val padding = with(config.density) { node.node.content.textBlockPadding().roundToPx() }
        val textWidth = (config.width - padding * 2).coerceAtLeast(1)
        val pageTextHeight = (config.height - padding * 2).coerceAtLeast(1)
        run {
            var offset = 0
            while (offset < rangeEnd) {
                currentCoroutineContext().ensureActive()
                var style = if (offset == 0) baseStyle else baseStyle.copy(textIndent = TextIndent(baseStyle.textIndent?.restLine ?: 0.sp, baseStyle.textIndent?.restLine ?: 0.sp))
                val gap = if (items.isEmpty() || offset > 0) 0 else config.gap
                val remainingHeight = config.height - usedHeight - gap - padding * 2
                if (remainingHeight <= 0 && items.isNotEmpty()) {
                    finishPage()
                    continue
                }
                val available = remainingHeight.coerceAtLeast(1)

                val remainingText = text.subSequence(offset, rangeEnd)
                val remaining = measure(remainingText, style, textWidth)
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
                        measured = measure(text.subSequence(offset, end), style, textWidth)
                        if (measured.height <= available || lines == 1) break
                        lines--
                    } while (true)
                    if (measured.height > available && items.isNotEmpty()) {
                        finishPage()
                        continue
                    }
                    if (measured.height > pageTextHeight) {
                        val scale = pageTextHeight.toFloat() / measured.height * 0.95f
                        style = style.copy(
                            fontSize = style.fontSize * scale,
                            lineHeight = style.lineHeight * scale
                        )
                        measured = measure(text.subSequence(offset, end), style, textWidth)
                    }
                }

                add(
                    ReaderPageItem(
                        node = node,
                        startOffset = if (caption) 0 else offset,
                        endOffset = if (caption) 1 else end,
                        height = (measured.height + padding * 2).coerceAtMost(config.height),
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

    suspend fun addTable(node: ReaderNode.Paragraph) {
        val row = node.node.content as NodeContent.TableRow
        val cells = node.tableCells()
        val columns = node.tableColumns()
        val padding = with(config.density) { ReaderTableCellPadding.roundToPx() }
        val offsets = IntArray(cells.size)
        var first = true
        while (first || cells.indices.any { offsets[it] < cells[it].text.length }) {
            currentCoroutineContext().ensureActive()
            val previousRow = items.lastOrNull()?.node?.node?.content as? NodeContent.TableRow
            val gap = if (items.isEmpty() || previousRow != null && !row.firstRow) 0 else config.gap
            val available = config.height - usedHeight - gap - padding * 2
            if (available <= 0 && items.isNotEmpty()) {
                finishPage()
                continue
            }
            var style = row.textBlockStyle(config.paragraphStyle)
            fun measurements() = cells.mapIndexed { index, cell ->
                measure(
                    cell.text.subSequence(offsets[index], cell.text.length), style,
                    tableCellTextWidth(cell.cell, columns, config.width, config.density)
                )
            }
            var measured = measurements()
            val firstLine = measured.maxOfOrNull { it.lineBottoms.firstOrNull() ?: it.height } ?: 1
            if (firstLine > available && items.isNotEmpty()) {
                finishPage()
                continue
            }
            val pageTextHeight = (config.height - padding * 2).coerceAtLeast(1)
            if (firstLine > pageTextHeight) {
                val scale = pageTextHeight.toFloat() / firstLine * 0.95f
                style = style.copy(fontSize = style.fontSize * scale, lineHeight = style.lineHeight * scale)
                measured = measurements()
            }
            var height = 0
            val fragments = cells.mapIndexed { index, cell ->
                val start = offsets[index]
                val remaining = cell.text.length - start
                val layout = measured[index]
                val lines = layout.lineBottoms.takeWhile { it <= available.coerceAtLeast(1) }.size
                    .coerceAtLeast(1).coerceAtMost(layout.lineEnds.size)
                var end = if (remaining == 0) start else if (lines == 0) cell.text.length
                    else (start + layout.lineEnds[lines - 1]).coerceIn(start + 1, cell.text.length)
                var fragment = cell.text.subSequence(start, end)
                var fragmentLayout = measure(fragment, style, tableCellTextWidth(cell.cell, columns, config.width, config.density))
                var lineCount = lines
                while (fragmentLayout.height > available && lineCount > 1) {
                    lineCount--
                    end = (start + layout.lineEnds[lineCount - 1]).coerceIn(start + 1, cell.text.length)
                    fragment = cell.text.subSequence(start, end)
                    fragmentLayout = measure(fragment, style, tableCellTextWidth(cell.cell, columns, config.width, config.density))
                }
                height = maxOf(height, fragmentLayout.height)
                offsets[index] = end
                cell.copy(text = fragment, startOffset = cell.startOffset + start, endOffset = cell.startOffset + end)
            }
            val visible = fragments.filter { it.text.isNotEmpty() }
            add(ReaderPageItem(
                node = node,
                startOffset = visible.minOfOrNull { it.startOffset } ?: 0,
                endOffset = visible.maxOfOrNull { it.endOffset } ?: row.computeLength(),
                height = (height + padding * 2).coerceAtMost(config.height),
                spaceBefore = gap,
                style = style,
                tableCells = fragments
            ))
            first = false
            if (cells.indices.any { offsets[it] < cells[it].text.length }) finishPage()
        }
    }

    nodes.forEach { node ->
        currentCoroutineContext().ensureActive()
        val continuesHeaderGroup = headerSequence.append(node.node.content)
        if (node is ReaderNode.Image && !config.images) return@forEach

        when (node) {
            is ReaderNode.Header -> {
                if (!continuesHeaderGroup) finishPage()
                addText(node, node.title, config.headerStyles[node.level.level - 1])
            }

            is ReaderNode.Paragraph -> if (node.node.content is NodeContent.TableRow) {
                addTable(node)
            } else addText(node, node.text, node.node.content.textBlockStyle(config.paragraphStyle))

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
    }
    finishPage()
    return pages
}

fun findReaderPage(pages: List<ReaderPage>, position: ReaderPagePosition): Int {
    val exact = pages.indexOfFirst { page ->
        page.items.any {
            val offset = it.node.toDisplayOffset(position.offset)
            it.node.node.id == position.nodeId && if (it.tableCells != null) {
                it.tableCells.any { cell -> offset >= cell.startOffset && offset < cell.endOffset }
            } else offset >= it.startOffset && offset < it.endOffset
        }
    }
    if (exact >= 0) return exact
    val separator = pages.indexOfFirst { page -> page.items.any {
        val offset = it.node.toDisplayOffset(position.offset)
        it.node.node.id == position.nodeId && offset >= it.startOffset && offset < it.endOffset
    } }
    if (separator >= 0) return separator
    return pages.indexOfLast {
        it.position.nodeId.id < position.nodeId.id ||
                it.position.nodeId == position.nodeId && it.position.offset <= position.offset
    }.coerceAtLeast(0)
}
