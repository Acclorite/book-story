/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.reader.paged

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import ua.acclorite.book_story.domain.model.book.Node
import ua.acclorite.book_story.domain.model.book.NodeContent
import ua.acclorite.book_story.domain.model.book.NodeId
import ua.acclorite.book_story.domain.model.book.NodePosition
import ua.acclorite.book_story.ui.reader.model.ReaderNode
import kotlin.math.ceil

class ReaderPagePaginatorTest {
    private val style = TextStyle(fontSize = 10.sp, lineHeight = 10.sp)
    private val config = ReaderPageConfig(
        width = 4, height = 20, gap = 0, separatorHeight = 1,
        paragraphStyle = style, headerStyles = List(6) { style },
        images = true, imagesCaptions = true, imagesOriginalSize = true,
        imagesWidth = 1f, density = Density(1f)
    )

    private fun measure(text: AnnotatedString, style: TextStyle): ReaderTextMeasurement {
        val count = ceil(text.length / 4.0).toInt().coerceAtLeast(1)
        return ReaderTextMeasurement(
            height = count * 10,
            lineEnds = List(count) { ((it + 1) * 4).coerceAtMost(text.length) },
            lineBottoms = List(count) { (it + 1) * 10 }
        )
    }

    private fun paragraph(id: Int, text: String): ReaderNode.Paragraph {
        val node = Node(NodeId(id), NodePosition(text.length, 0), NodeContent.Paragraph(text, emptyList()))
        return ReaderNode.Paragraph(node, AnnotatedString(text))
    }

    private fun header(id: Int, level: Int): ReaderNode.Header {
        val content = NodeContent.Header("H$level", NodeContent.HeaderLevel.forNumber(level))
        return ReaderNode.Header(Node(NodeId(id), NodePosition(2, 0), content), AnnotatedString(content.title), content.level)
    }

    @Test
    fun `Long paragraphs split without losing or duplicating characters`() = runBlocking {
        val text = "abcd efgh ijkl mnop"
        val pages = paginateReaderPages(listOf(paragraph(0, text)), config, ::measure)

        assertEquals(text, pages.flatMap { it.items }.joinToString("") { it.text!!.text })
        assertEquals(ReaderPagePosition(NodeId(0), 0), pages.first().position)
        assertEquals(ReaderPagePosition(NodeId(0), 8), pages[1].position)
        assertTrue(pages.all { page -> page.items.sumOf { it.height + it.spaceBefore } <= config.height })
    }

    @Test
    fun `Headers start a new page and consecutive headers stack`() = runBlocking {
        val nodes = listOf(paragraph(0, "text"), header(1, 2), header(2, 3), paragraph(3, "body"))
        val pages = paginateReaderPages(nodes, config.copy(height = 40), ::measure)

        assertEquals(2, pages.size)
        assertEquals(listOf(0), pages[0].items.map { it.node.node.id.id })
        assertEquals(listOf(1, 2, 3), pages[1].items.map { it.node.node.id.id })
        assertTrue(pages.none { it.items.isEmpty() })
    }

    @Test
    fun `H5 and H6 start a new page and stack`() = runBlocking {
        val pages = paginateReaderPages(
            listOf(paragraph(0, "text"), header(1, 5), header(2, 6)),
            config.copy(height = 40), ::measure
        )

        assertEquals(2, pages.size)
        assertEquals(listOf(1, 2), pages[1].items.map { it.node.node.id.id })
    }

    @Test
    fun `A header at the start does not create a blank page`() = runBlocking {
        val pages = paginateReaderPages(listOf(header(0, 1), header(1, 2)), config, ::measure)

        assertEquals(1, pages.size)
        assertEquals(2, pages.single().items.size)
    }

    @Test
    fun `Seeking within a page selects its natural start without changing pagination`() = runBlocking {
        val nodes = listOf(paragraph(0, "text"), paragraph(1, "abcdefghijklmnop"))
        val original = paginateReaderPages(nodes, config, ::measure)
        val target = ReaderPagePosition(NodeId(1), 2)
        val page = original[findReaderPage(original, target)]

        assertEquals(ReaderPagePosition(NodeId(0), 0), page.position)
        val repeated = paginateReaderPages(nodes, config, ::measure)
        assertEquals(original, repeated)
        assertEquals(page, repeated[findReaderPage(repeated, target)])
        assertEquals("abcdefghijklmnop", repeated.flatMap { it.items }.filter { it.node.node.id.id == 1 }
            .joinToString("") { it.text!!.text })
    }

    @Test
    fun `Text formatting is preserved across page boundaries`() = runBlocking {
        val paragraph = paragraph(0, "abcdefghijklmnop")
        val annotated = buildAnnotatedString {
            append(paragraph.text)
            addStyle(SpanStyle(fontWeight = FontWeight.Bold), 6, 10)
        }
        val pages = paginateReaderPages(listOf(paragraph.copy(text = annotated)), config, ::measure)
        val first = pages[0].items.single().text!!
        val second = pages[1].items.single().text!!

        assertTrue(first.spanStyles.any { it.start == 6 && it.end == 8 && it.item.fontWeight == FontWeight.Bold })
        assertTrue(second.spanStyles.any { it.start == 0 && it.end == 2 && it.item.fontWeight == FontWeight.Bold })
    }

    @Test
    fun `Large images fit within a page with their aspect ratio preserved`() = runBlocking {
        val content = NodeContent.Image("image", null, 100, 200, 0.5f)
        val image = ReaderNode.Image(
            Node(NodeId(0), NodePosition(1, 0), content),
            "image", null, 100.dp, 200.dp, 0.5f
        )
        val pages = paginateReaderPages(listOf(image), config.copy(width = 100, height = 100), ::measure)
        val item = pages.single().items.single()

        assertEquals(50, item.width)
        assertEquals(100, item.height)
    }

    @Test
    fun `Disabled images produce no blank pages or spacing`() = runBlocking {
        val content = NodeContent.Image("image", null, 10, 10, 1f)
        val image = ReaderNode.Image(Node(NodeId(1), NodePosition(1, 4), content), "image", null, 10.dp, 10.dp, 1f)
        val pages = paginateReaderPages(
            listOf(paragraph(0, "text"), image, paragraph(2, "more")),
            config.copy(images = false), ::measure
        )

        assertEquals(1, pages.size)
        assertEquals(listOf(0, 2), pages.single().items.map { it.node.node.id.id })
        assertFalse(pages.single().items.any { it.node is ReaderNode.Image })
    }
}
