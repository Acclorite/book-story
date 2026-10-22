/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.mapper

import junit.framework.TestCase.assertEquals
import org.junit.Test
import ua.acclorite.book_story.data.mapper.node.NodeMapperImpl
import ua.acclorite.book_story.data.mapper.node_content.NodeContentMapperImpl
import ua.acclorite.book_story.data.service.GZIPCompressionHandlerImpl
import ua.acclorite.book_story.data.storage.room.entity.content.NodeEntity
import ua.acclorite.book_story.domain.model.book.Node
import ua.acclorite.book_story.domain.model.book.NodeContent
import ua.acclorite.book_story.domain.model.book.NodeId
import ua.acclorite.book_story.domain.model.book.NodePosition
import kotlin.test.assertFailsWith

class NodeMapperImplTest {

    private val compressionHandler = GZIPCompressionHandlerImpl()
    private val nodeContentMapper = NodeContentMapperImpl()
    private val nodeMapper = NodeMapperImpl(compressionHandler, nodeContentMapper)

    @Test
    fun `Node should survive round trip`() {
        val original = Node(
            id = NodeId(4),
            position = NodePosition(
                length = 10,
                offset = 43
            ),
            content = NodeContent.Header(
                title = "Node header",
                level = NodeContent.HeaderLevel.H4
            )
        )

        val entity = nodeMapper.toNodeEntity(original)
        val result = nodeMapper.toNode(entity)

        assertEquals(original, result)
    }

    @Test
    fun `Incorrect Node should throw`() {
        val node = Node(
            id = NodeId(4),
            position = NodePosition(
                length = 0, // Length is zero
                offset = 43
            ),
            content = NodeContent.Header(
                title = "Node header",
                level = NodeContent.HeaderLevel.H4
            )
        )

        val exception = assertFailsWith<IllegalStateException> {
            nodeMapper.toNodeEntity(node)
        }

        assertEquals("Incorrect Node instance.", exception.message)
    }

    @Test
    fun `Incorrect NodeEntity should throw`() {
        val node = NodeEntity(
            id = -1, // ID is less than 0
            length = 5,
            offset = 43,
            contentBlob = byteArrayOf()
        )

        val exception = assertFailsWith<IllegalStateException> {
            nodeMapper.toNode(node)
        }

        assertEquals("Incorrect NodeEntity instance.", exception.message)
    }
}