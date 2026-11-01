/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.mapper

import junit.framework.TestCase.assertEquals
import org.junit.Test
import ua.acclorite.book_story.data.mapper.node_content.NodeContentMapperImpl
import ua.acclorite.book_story.domain.model.book.NodeContent

class NodeContentMapperImplTest {

    private val mapper = NodeContentMapperImpl()

    @Test
    fun `Header should survive round trip`() {
        val original = NodeContent.Header(
            title = "Test header title",
            level = NodeContent.HeaderLevel.H2
        )

        val proto = mapper.toNodeContentProto(original)
        val result = mapper.toNodeContent(proto)

        assertEquals(original, result)
    }

    @Test
    fun `Paragraph should survive round trip`() {
        val original = NodeContent.Paragraph(
            text = "Test paragraph text",
            styles = listOf(
                NodeContent.TextStyleRange(0, 5, NodeContent.TextStyle.Bold),
                NodeContent.TextStyleRange(6, 11, NodeContent.TextStyle.Italic)
            )
        )

        val proto = mapper.toNodeContentProto(original)
        val result = mapper.toNodeContent(proto)

        assertEquals(original, result)
    }

    @Test
    fun `Image should survive round trip`() {
        val original = NodeContent.Image(
            content = "Image path",
            caption = "Test image caption",
            width = 100,
            height = 165,
            aspectRatio = 100f / 165f
        )

        val proto = mapper.toNodeContentProto(original)
        val result = mapper.toNodeContent(proto)

        assertEquals(original, result)
    }

    @Test
    fun allSemanticBlocksAndInlineStylesSurviveSerializedRoundTrip() {
        val styles = listOf(
            NodeContent.TextStyle.Bold, NodeContent.TextStyle.Italic,
            NodeContent.TextStyle.Underline, NodeContent.TextStyle.Strikethrough,
            NodeContent.TextStyle.Superscript, NodeContent.TextStyle.Subscript,
            NodeContent.TextStyle.Monospace, NodeContent.TextStyle.Small,
            NodeContent.TextStyle.Highlight, NodeContent.TextStyle.Link("https://example.org")
        ).map { NodeContent.TextStyleRange(0, 4, it) }
        val nodes = listOf(
            NodeContent.Paragraph("text", styles),
            NodeContent.Quote("text", styles, 2),
            NodeContent.CodeBlock("text", styles),
            NodeContent.Verse("text", styles),
            NodeContent.ListItem("text", styles, "3.", 2),
            NodeContent.Subtitle("text", styles),
            NodeContent.TableRow("text", styles, true),
            NodeContent.Caption("text", styles)
        )
        val encoded = mapper.toNodeContentListProto(nodes).toByteArray()
        val decoded = ua.acclorite.book_story.proto.NodeContentListProto.parseFrom(encoded)
        assertEquals(nodes, mapper.toNodeContentList(decoded))
    }

    @Test
    fun invalidStyleRangesAreClampedOrDroppedDuringDecoding() {
        val paragraph = NodeContent.Paragraph("text", listOf(
            NodeContent.TextStyleRange(-1, 20, NodeContent.TextStyle.Bold),
            NodeContent.TextStyleRange(9, 10, NodeContent.TextStyle.Italic),
            NodeContent.TextStyleRange(3, 1, NodeContent.TextStyle.Small)
        ))
        val decoded = mapper.toNodeContent(mapper.toNodeContentProto(paragraph)) as NodeContent.Paragraph
        assertEquals(listOf(NodeContent.TextStyleRange(0, 4, NodeContent.TextStyle.Bold)), decoded.styles)
    }

    @Test
    fun `Separator should survive round trip`() {
        val original = NodeContent.Separator

        val proto = mapper.toNodeContentProto(original)
        val result = mapper.toNodeContent(proto)

        assertEquals(original, result)
    }
}