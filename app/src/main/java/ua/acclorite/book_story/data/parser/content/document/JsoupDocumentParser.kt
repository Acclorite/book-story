/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.parser.content.document

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import ua.acclorite.book_story.core.helpers.cleanHeaderText
import ua.acclorite.book_story.core.helpers.containsVisibleText
import ua.acclorite.book_story.core.log.logW
import ua.acclorite.book_story.domain.model.book.NodeContent
import ua.acclorite.book_story.domain.model.file.ImageMetadata
import javax.inject.Inject
import kotlin.io.encoding.Base64

data class BinaryImage(val name: String, val bytes: ByteArray) {
    override fun equals(other: Any?): Boolean = other is BinaryImage &&
        name == other.name && bytes.contentEquals(other.bytes)

    override fun hashCode(): Int = 31 * name.hashCode() + bytes.contentHashCode()
}

private fun Element.localTag(): String = normalName().substringAfter(':')
private val ignoredTags = setOf("head", "script", "stylesheet", "binary", "template")
private val blockTags = setOf(
    "body", "p", "div", "section", "article", "main", "header", "footer", "aside", "nav",
    "blockquote", "figure", "figcaption", "dl", "dt", "dd", "ul", "ol", "menu", "li",
    "table", "thead", "tbody", "tfoot", "tr", "caption", "pre", "address",
    "epigraph", "poem", "stanza", "v", "text-author", "annotation", "date"
)
private val headerTag = Regex("h[1-6]")
private val htmlWhitespace = Regex("[ \\t\\r\\n\\u000C]+")
private val externalLink = Regex("^(https?|mailto|tel):", RegexOption.IGNORE_CASE)

class JsoupDocumentParser @Inject constructor() {
    suspend fun parseDocument(
        document: Document,
        images: Map<String, ImageMetadata>,
        documentPath: String = "",
        stylesheets: List<String> = emptyList(),
        outline: List<DocumentOutlineEntry> = emptyList()
    ): List<NodeContent> {
        val fictionBook = document.getAllElements().firstOrNull { it.localTag() == "fictionbook" }
        val roots = fictionBook?.children()?.filter { it.localTag() == "body" }
            ?: document.getAllElements().firstOrNull { it.localTag() == "body" }?.let { listOf(it) }
            ?: document.children().toList()
        return DocumentReader(images, documentPath, fictionBook != null, DocumentStyleResolver(document, stylesheets), outline).read(roots)
    }

    fun extractBinaryImages(document: Document): Sequence<BinaryImage> =
        document.getAllElements().asSequence().filter { it.localTag() == "binary" }.mapNotNull { element ->
            if (!element.attr("content-type").startsWith("image/", ignoreCase = true)) {
                return@mapNotNull null
            }
            val name = element.id().trim().takeIf { it.isNotEmpty() } ?: return@mapNotNull null
            try {
                val encoded = element.wholeText().filterNot { it in " \t\r\n" }
                BinaryImage(name, Base64.decode(encoded))
            } catch (exception: IllegalArgumentException) {
                logW("JsoupDocumentParser", "Skipping invalid binary image [$name]: ${exception.message}")
                null
            }
        }

    private class DocumentReader(
        val images: Map<String, ImageMetadata>,
        val documentPath: String,
        val fictionBook: Boolean,
        val styleResolver: DocumentStyleResolver,
        val outline: List<DocumentOutlineEntry>
    ) {
        val nodes = mutableListOf<NodeContent>()
        val text = StringBuilder()
        val ranges = mutableListOf<NodeContent.TextStyleRange>()
        var preformatted = false
        var listPrefix: String? = null
        var blockFactory: (String, List<NodeContent.TextStyleRange>) -> NodeContent.TextBlock =
            { value, styles -> NodeContent.Paragraph(value, styles) }
        var quoteDepth = 0
        var tableRow = false
        var inFootnote = false
        val outlineHeaders = java.util.IdentityHashMap<Element, DocumentOutlineEntry>()
        val outlineInsertions = java.util.IdentityHashMap<Element, DocumentOutlineEntry>()

        suspend fun read(
            roots: List<Element>,
            inherited: List<NodeContent.TextStyle> = emptyList()
        ): List<NodeContent> {
            if (outline.isNotEmpty()) {
                val elements = roots.flatMap { it.getAllElements() }
                for (entry in outline) {
                    val target = if (entry.fragment.isNullOrBlank()) roots.firstOrNull()
                    else elements.firstOrNull { it.id() == entry.fragment || it.attr("name") == entry.fragment }
                    if (target == null) continue
                    fun Element.isHeading() = localTag().matches(headerTag) || attr("role") == "heading"
                    val heading = if (target.isHeading()) target
                    else target.getAllElements().firstOrNull { it.isHeading() }
                        ?: target.nextElementSibling()?.takeIf { target.text().isBlank() && it.isHeading() }
                        ?: target.takeIf {
                            it.localTag() in setOf("p", "div", "span") &&
                                headingText(it) == entry.title.cleanHeaderText()
                        }
                    if (heading != null) outlineHeaders.putIfAbsent(heading, entry)
                    else outlineInsertions.putIfAbsent(target, entry)
                }
            }
            roots.forEach {
                visit(it, inherited, preserveWhitespace = false)
                flush()
            }
            return nodes
        }

        fun append(value: String, styles: List<NodeContent.TextStyle>, preserveWhitespace: Boolean) {
            if (value.isEmpty()) return
            var normalized = if (preserveWhitespace) value.replace("\r\n", "\n").replace('\r', '\n')
            else value.replace(htmlWhitespace, " ")
            if (!preserveWhitespace && (text.isEmpty() || text.last() == ' ')) {
                normalized = normalized.trimStart(' ')
            }
            if (normalized.isEmpty()) return
            if (text.isEmpty()) listPrefix?.let { text.append(it); listPrefix = null }
            val start = text.length
            text.append(normalized)
            preformatted = preformatted || preserveWhitespace
            styles.distinct().forEach { ranges.add(NodeContent.TextStyleRange(start, text.length, it)) }
        }

        fun flush() {
            var start = 0
            var end = text.length
            if (!preformatted) {
                while (start < end && text[start].isWhitespace()) start++
                while (end > start && text[end - 1].isWhitespace()) end--
            }
            val value = text.substring(start, end)
            if (value.containsVisibleText()) {
                if (inFootnote) {
                    ranges.add(0, NodeContent.TextStyleRange(start, end, NodeContent.TextStyle.Small))
                }
                val clipped = ranges.mapNotNull {
                    val from = it.start.coerceIn(start, end) - start
                    val to = it.end.coerceIn(start, end) - start
                    if (from < to) it.copy(start = from, end = to) else null
                }
                val merged = mutableListOf<NodeContent.TextStyleRange>()
                clipped.groupBy { it.style }.forEach { (_, spans) ->
                    var current: NodeContent.TextStyleRange? = null
                    spans.sortedBy { it.start }.forEach { span ->
                        val previous = current
                        if (previous != null && span.start <= previous.end) {
                            current = previous.copy(end = maxOf(previous.end, span.end))
                        } else {
                            current?.let { merged.add(it) }
                            current = span
                        }
                    }
                    current?.let { merged.add(it) }
                }
                nodes.add(blockFactory(value, merged.sortedBy { it.start }))
            }
            text.clear()
            ranges.clear()
            preformatted = false
        }

        fun hidden(element: Element): Boolean {
            if (element.localTag() in ignoredTags || element.hasAttr("hidden") ||
                (!fictionBook && element.localTag() == "style")
            ) return true
            val css = css(element)
            return css["display"].equals("none", true) ||
                css["visibility"].equals("hidden", true) || css["visibility"].equals("collapse", true)
        }

        fun css(element: Element): Map<String, String> = styleResolver.resolve(element)

        fun footnoteContainer(element: Element): Boolean {
            if (element.localTag() !in setOf("div", "aside", "section")) return false
            return "footnote" in element.classNames() ||
                "doc-footnote" in element.attr("role").split(htmlWhitespace) ||
                "footnote" in element.attr("epub:type").split(htmlWhitespace) ||
                "footnote" in element.attr("ops:type").split(htmlWhitespace)
        }

        fun headingLevel(element: Element): Int? {
            outlineHeaders[element]?.let { return it.level.coerceIn(1, 6) }
            val tag = element.localTag()
            if (tag.matches(headerTag)) return tag.drop(1).toInt()
            if (element.attr("role").equals("heading", true)) {
                return (element.attr("aria-level").toIntOrNull() ?: 2).coerceIn(1, 6)
            }
            if (!fictionBook) return null
            if (tag == "title") {
                if (element.parent()?.localTag() in setOf("poem", "stanza", "epigraph", "cite")) return null
                val sections = element.parents().count { it.localTag() == "section" }
                val extra = if (element.parent()?.localTag() in setOf("poem", "stanza")) 1 else 0
                return (sections + extra).coerceIn(1, 6)
            }

            return null
        }

        fun headingText(element: Element): String {
            val result = StringBuilder()
            fun collect(node: Node) {
                when (node) {
                    is TextNode -> result.append(node.wholeText)
                    is Element -> {
                        if (hidden(node)) return
                        val block = node.localTag() in blockTags || node.localTag() == "br"
                        if (block) result.append(' ')
                        node.childNodes().forEach { collect(it) }
                        if (block) result.append(' ')
                    }
                }
            }
            collect(element)
            return result.toString().cleanHeaderText()
        }

        fun headingImages(element: Element) {
            if (hidden(element)) return
            if (element.localTag() in setOf("img", "image", "object") &&
                image(element, emptyList(), preserveWhitespace = false)
            ) return
            element.children().forEach { headingImages(it) }
        }

        fun href(element: Element): String = element.attr("href").ifBlank {
            element.attributes().firstOrNull { it.key.substringAfter(':').equals("href", true) }?.value.orEmpty()
        }

        fun findImage(source: String): ImageMetadata? {
            val key = if (source.startsWith('#')) decodeDocumentPath(source.drop(1))
            else resolveDocumentPath(documentPath, source)
            if (key != null) images[key]?.let { return it }
            images[source]?.let { return it }
            val name = key?.substringAfterLast('/') ?: return null
            val candidates = images.filterKeys { it.substringAfterLast('/').equals(name, true) }.values
            return candidates.singleOrNull()
        }

        fun image(element: Element, styles: List<NodeContent.TextStyle>, preserveWhitespace: Boolean): Boolean {
            val tag = element.localTag()
            val source = when (tag) {
                "img" -> element.attr("src")
                "object" -> element.attr("data")
                else -> href(element)
            }.trim()
            val metadata = findImage(source)
            val alt = element.attr("alt").ifBlank { element.attr("title") }.trim()
            if (metadata == null) {
                if (alt.isNotBlank()) append(alt, styles, preserveWhitespace)
                return false
            }
            flush()
            nodes.add(
                NodeContent.Image(
                    metadata.path.path, alt.takeIf { it.isNotEmpty() },
                    metadata.width, metadata.height, metadata.aspectRatio
                )
            )
            return true
        }

        fun inlineStyles(element: Element, inherited: List<NodeContent.TextStyle>): List<NodeContent.TextStyle> {
            val tag = element.localTag()
            val css = css(element)
            val styles = inherited.toMutableList()
            if (tag in setOf("b", "strong", "th")) styles.add(NodeContent.TextStyle.Bold)
            if (tag in setOf("i", "em", "emphasis", "cite", "dfn")) styles.add(NodeContent.TextStyle.Italic)
            css["font-weight"]?.let { weight ->
                if (weight == "normal" || weight.toIntOrNull()?.let { it < 600 } == true) {
                    styles.removeAll { it is NodeContent.TextStyle.Bold }
                } else if (weight in setOf("bold", "bolder") || weight.toIntOrNull()?.let { it >= 600 } == true) {
                    styles.add(NodeContent.TextStyle.Bold)
                }
            }
            css["font-style"]?.let {
                if (it == "normal") styles.removeAll { style -> style is NodeContent.TextStyle.Italic }
                else if (it.startsWith("italic") || it.startsWith("oblique")) styles.add(NodeContent.TextStyle.Italic)
            }
            if (tag in setOf("u", "ins")) styles.add(NodeContent.TextStyle.Underline)
            if (tag in setOf("s", "strike", "del", "strikethrough")) styles.add(NodeContent.TextStyle.Strikethrough)
            if (tag == "sup") {
                styles.removeAll { it is NodeContent.TextStyle.Subscript }
                styles.add(NodeContent.TextStyle.Superscript)
            }
            if (tag == "sub") {
                styles.removeAll { it is NodeContent.TextStyle.Superscript }
                styles.add(NodeContent.TextStyle.Subscript)
            }
            if (tag in setOf("code", "kbd", "samp", "tt")) styles.add(NodeContent.TextStyle.Monospace)
            val inlineSmall = tag !in blockTags && tag !in setOf("td", "th") &&
                css["display"] !in setOf("block", "list-item", "table", "table-cell") &&
                css["font-size"] in setOf("small", "smaller", "x-small", "xx-small")
            if (tag == "small" || inlineSmall) styles.add(NodeContent.TextStyle.Small)
            if (tag == "mark") styles.add(NodeContent.TextStyle.Highlight)
            val decoration = css["text-decoration-line"] ?: css["text-decoration"].orEmpty()
            if ("underline" in decoration) styles.add(NodeContent.TextStyle.Underline)
            if ("line-through" in decoration) styles.add(NodeContent.TextStyle.Strikethrough)
            if (decoration == "none") styles.removeAll {
                it is NodeContent.TextStyle.Underline || it is NodeContent.TextStyle.Strikethrough
            }
            when (css["vertical-align"]) {
                "super" -> styles.add(NodeContent.TextStyle.Superscript)
                "sub" -> styles.add(NodeContent.TextStyle.Subscript)
            }
            if (css["font-family"]?.contains("monospace") == true) styles.add(NodeContent.TextStyle.Monospace)
            if (tag == "a") {
                val link = href(element).trim()
                if (externalLink.containsMatchIn(link)) styles.add(NodeContent.TextStyle.Link(link))
            }

            return styles
        }

        suspend fun readTable(element: Element, inherited: List<NodeContent.TextStyle>) {
            element.children().filter { it.localTag() == "caption" }.forEach { visit(it, inherited, false) }
            val rows = element.getAllElements().filter { row ->
                row.localTag() == "tr" && row.parents().firstOrNull { it.localTag() == "table" } === element &&
                    row.parents().takeWhile { it !== element }.none { hidden(it) } && !hidden(row)
            }
            data class Span(val column: Int, val width: Int, val endRow: Int, val header: Boolean)
            val active = mutableListOf<Span>()
            val parsed = mutableListOf<NodeContent.TableRow>()
            val tableImages = mutableListOf<NodeContent.Image>()
            var columns = 1
            rows.forEachIndexed { rowIndex, row ->
                currentCoroutineContext().ensureActive()
                active.removeAll { it.endRow <= rowIndex }
                val value = StringBuilder()
                val styles = mutableListOf<NodeContent.TextStyleRange>()
                val cells = active.map { span ->
                    NodeContent.TableCell(0, 0, span.column, span.width, span.endRow - rowIndex, span.header, true)
                }.toMutableList()
                var column = 0
                var rowStyles = inherited
                row.parents().takeWhile { it !== element }.asReversed().forEach { parent ->
                    rowStyles = inlineStyles(parent, rowStyles)
                }
                rowStyles = inlineStyles(row, rowStyles)
                val groupRowsRemaining = rows.drop(rowIndex).takeWhile { it.parent() === row.parent() }.size
                row.children().filter { it.localTag() in setOf("td", "th") && !hidden(it) }.forEach { cell ->
                    val width = (cell.attr("colspan").toIntOrNull() ?: 1).coerceIn(1, 1000)
                    while (active.any { column < it.column + it.width && column + width > it.column }) column++
                    if (column >= 1000) return@forEach
                    val spanWidth = width.coerceAtMost(1000 - column)
                    val requestedHeight = cell.attr("rowspan").toIntOrNull() ?: 1
                    val height = (if (requestedHeight == 0) groupRowsRemaining else requestedHeight)
                        .coerceIn(1, minOf(65534, groupRowsRemaining))
                    val reader = DocumentReader(images, documentPath, fictionBook, styleResolver, emptyList())
                    reader.tableRow = true
                    val content = reader.read(listOf(cell), rowStyles)
                    if (value.isNotEmpty()) value.append(" | ")
                    val start = value.length
                    content.forEach { block ->
                        when (block) {
                            is NodeContent.TextBlock -> {
                                if (value.length > start) value.append('\n')
                                val offset = value.length
                                value.append(block.text)
                                styles.addAll(block.styles.map { it.copy(start = it.start + offset, end = it.end + offset) })
                            }
                            is NodeContent.Image -> {
                                tableImages.add(block)
                                block.caption?.let { caption ->
                                    if (value.length > start) value.append('\n')
                                    value.append(caption)
                                }
                            }
                            else -> Unit
                        }
                    }
                    val header = cell.localTag() == "th"
                    cells.add(NodeContent.TableCell(start, value.length, column, spanWidth, height, header))
                    if (height > 1) active.add(Span(column, spanWidth, rowIndex + height, header))
                    column += spanWidth
                }
                if (cells.isNotEmpty()) {
                    columns = maxOf(columns, cells.maxOf { it.column + it.columnSpan })
                    parsed.add(NodeContent.TableRow(value.toString(), styles, cells.any { it.header }, cells.sortedBy { it.column }))
                }
            }
            parsed.forEachIndexed { index, row ->
                nodes.add(row.copy(columns = columns, firstRow = index == 0, lastRow = index == parsed.lastIndex))
            }
            nodes.addAll(tableImages)
        }

        suspend fun visit(node: Node, inherited: List<NodeContent.TextStyle>, preserveWhitespace: Boolean) {
            currentCoroutineContext().ensureActive()
            if (node is TextNode) {
                append(node.wholeText, inherited, preserveWhitespace)
                return
            }
            if (node !is Element || hidden(node)) return
            if (!tableRow && !inFootnote && footnoteContainer(node)) {
                flush()
                val start = nodes.size
                inFootnote = true
                try {
                    visit(node, listOf(NodeContent.TextStyle.Small) + inherited, preserveWhitespace)
                    flush()
                } finally {
                    inFootnote = false
                }
                if (nodes.size > start && nodes[start] !is NodeContent.Separator &&
                    nodes.getOrNull(start - 1) !is NodeContent.Separator
                ) {
                    nodes.add(start, NodeContent.Separator)
                }
                return
            }
            val tag = node.localTag()
            outlineInsertions[node]?.let { entry ->
                flush()
                val title = entry.title.cleanHeaderText()
                if (title.isNotEmpty()) nodes.add(NodeContent.Header(title, NodeContent.HeaderLevel.forNumber(entry.level.coerceIn(1, 6))))
            }
            if (tag == "title" && !fictionBook || tag == "desc") return

            val heading = if (tableRow) null else headingLevel(node)
            if (heading != null) {
                flush()
                val title = headingText(node)
                if (title.containsVisibleText()) {
                    nodes.add(NodeContent.Header(title, NodeContent.HeaderLevel.forNumber(heading)))
                }
                headingImages(node)
                flush()
                return
            }
            when (tag) {
                "br" -> {
                    if (tableRow) append(" / ", inherited, false)
                    else if (preserveWhitespace) append("\n", inherited, true) else flush()
                    return
                }
                "hr", "empty-line" -> {
                    flush()
                    nodes.add(NodeContent.Separator)
                    return
                }
                "img", "image", "object" -> {
                    if (image(node, inherited, preserveWhitespace) || tag != "object") return
                }
            }

            val css = css(node)
            val previousFactory = blockFactory
            val previousQuoteDepth = quoteDepth
            val previousTableRow = tableRow
            val subtitle = fictionBook && (tag == "subtitle" ||
                tag == "title" && node.parent()?.localTag() in setOf("poem", "stanza", "epigraph", "cite"))
            val block = !tableRow && (tag in blockTags || subtitle || (fictionBook && tag == "cite") ||
                css["display"] in setOf("block", "list-item", "table"))
            if (block) flush()
            else if (tableRow && tag in blockTags && text.isNotEmpty() && !text.last().isWhitespace()) {
                append(" / ", inherited, false)
            }
            when {
                tableRow -> Unit
                tag == "blockquote" || tag == "epigraph" || fictionBook && tag == "cite" -> {
                    quoteDepth++
                    val depth = quoteDepth
                    blockFactory = { value, spans -> NodeContent.Quote(value, spans, depth) }
                }
                tag == "pre" -> blockFactory = { value, spans -> NodeContent.CodeBlock(value, spans) }
                tag == "v" && fictionBook -> blockFactory = { value, spans -> NodeContent.Verse(value, spans) }
                subtitle -> blockFactory = { value, spans -> NodeContent.Subtitle(value, spans) }
                tag in setOf("figcaption", "caption", "text-author") || fictionBook && tag == "date" -> {
                    blockFactory = { value, spans -> NodeContent.Caption(value, spans) }
                }
                tag == "tr" -> {
                    tableRow = true
                    val header = node.children().any { it.localTag() == "th" }
                    blockFactory = { value, spans -> NodeContent.TableRow(value, spans, header) }
                }
            }
            val styles = inlineStyles(node, inherited)
            if (tag == "table" && !tableRow) {
                readTable(node, styles)
                return
            }

            if (tag == "li" && !tableRow) {
                val parent = node.parent()
                val prefix = if (parent?.localTag() == "ol") {
                    val siblings = parent.children().filter { it.localTag() == "li" }
                    val step = if (parent.hasAttr("reversed")) -1 else 1
                    var number = parent.attr("start").toIntOrNull()
                        ?: if (step < 0) siblings.size else 1
                    for (sibling in siblings) {
                        number = sibling.attr("value").toIntOrNull() ?: number
                        if (sibling === node) break
                        number += step
                    }
                    "$number. "
                } else "• "
                listPrefix = prefix
                val marker = prefix.trimEnd()
                val depth = node.parents().count { it.localTag() in setOf("ul", "ol", "menu") }.coerceAtLeast(1)
                blockFactory = { value, spans -> NodeContent.ListItem(value, spans, marker, depth) }
            }
            if (text.isNotEmpty() && tag in setOf("td", "th") &&
                node.previousElementSibling()?.localTag() in setOf("td", "th")
            ) append(" | ", inherited, false)

            if (tag == "q") append("“", styles, preserveWhitespace)
            if (tag == "rt") append(" (", styles, preserveWhitespace)
            if (tag != "rp") node.childNodes().forEach {
                visit(it, styles, preserveWhitespace || tag == "pre" || fictionBook && tag == "v")
            }
            if (tag == "rt") append(")", styles, preserveWhitespace)
            if (tag == "q") append("”", styles, preserveWhitespace)
            if (block) flush()
            blockFactory = previousFactory
            quoteDepth = previousQuoteDepth
            tableRow = previousTableRow
            if (tag == "li") listPrefix = null
        }
    }
}
