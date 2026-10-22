/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.mapper.node

import ua.acclorite.book_story.data.mapper.node_content.NodeContentMapper
import ua.acclorite.book_story.data.storage.room.entity.content.NodeEntity
import ua.acclorite.book_story.domain.model.book.Node
import ua.acclorite.book_story.domain.model.book.NodeId
import ua.acclorite.book_story.domain.model.book.NodePosition
import ua.acclorite.book_story.domain.service.CompressionHandler
import ua.acclorite.book_story.proto.NodeContentProto
import javax.inject.Inject

class NodeMapperImpl @Inject constructor(
    private val compressionHandler: CompressionHandler,
    private val nodeContentMapper: NodeContentMapper
) : NodeMapper {

    override fun toNodeEntity(node: Node): NodeEntity {
        if (node.id.id <= -1
            || node.position.length <= 0
            || node.position.offset < 0
            || node.position.endOffset < 0
            || node.position.endOffset < node.position.offset
        ) {
            throw IllegalStateException("Incorrect Node instance.")
        }

        val nodeContentProto = nodeContentMapper.toNodeContentProto(node.content)
        val nodeContentProtoBlob = nodeContentProto.toByteArray()
        val nodeContentProtoCompressedBlob = compressionHandler.compressBlob(nodeContentProtoBlob)

        return NodeEntity(
            id = node.id.id,
            length = node.position.length,
            offset = node.position.offset,
            contentBlob = nodeContentProtoCompressedBlob
        )
    }

    override fun toNode(nodeEntity: NodeEntity): Node {
        if (nodeEntity.id <= -1
            || nodeEntity.offset < 0
            || nodeEntity.length <= 0
        ) {
            throw IllegalStateException("Incorrect NodeEntity instance.")
        }

        val nodeContentProtoBlob = compressionHandler.decompressBlob(nodeEntity.contentBlob)
        val nodeContentProto = NodeContentProto.parseFrom(nodeContentProtoBlob)
        val nodeContent = nodeContentMapper.toNodeContent(nodeContentProto)

        return Node(
            id = NodeId(nodeEntity.id),
            position = NodePosition(
                length = nodeEntity.length,
                offset = nodeEntity.offset
            ),
            content = nodeContent
        )
    }
}