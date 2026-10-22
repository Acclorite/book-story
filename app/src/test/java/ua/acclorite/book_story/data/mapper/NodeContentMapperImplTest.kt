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
    fun `Separator should survive round trip`() {
        val original = NodeContent.Separator

        val proto = mapper.toNodeContentProto(original)
        val result = mapper.toNodeContent(proto)

        assertEquals(original, result)
    }
}