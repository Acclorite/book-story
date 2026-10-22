/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.service

import ua.acclorite.book_story.data.mapper.node_content.NodeContentMapper
import ua.acclorite.book_story.domain.model.book.NodeContent
import ua.acclorite.book_story.domain.model.file.FilePath
import ua.acclorite.book_story.domain.service.FileHandler
import ua.acclorite.book_story.domain.service.NodeHandler
import ua.acclorite.book_story.proto.NodeContentListProto
import java.io.File
import javax.inject.Inject

class NodeHandlerImpl @Inject constructor(
    private val fileHandler: FileHandler,
    private val nodeContentMapper: NodeContentMapper
) : NodeHandler {
    override suspend fun cacheNodes(
        nodes: List<NodeContent>,
        tempDirectory: String
    ): Result<FilePath?> = runCatching {
        if (nodes.isEmpty()) return@runCatching null
        val nodesProto = nodeContentMapper.toNodeContentListProto(nodes)
        return fileHandler.saveCustomToCacheWithRandomName(
            save = { outputStream -> nodesProto.writeTo(outputStream) },
            extension = "node",
            directory = tempDirectory
        )
    }

    override suspend fun getNodesFromFile(
        file: FilePath
    ): Result<List<NodeContent>> = runCatching {
        val file = File(file.path)
        if (!file.exists()) return@runCatching emptyList()

        file.inputStream().buffered().use { inputStream ->
            val nodeContentListProto = NodeContentListProto.parseFrom(inputStream)
            nodeContentListProto.nodesList.map { nodeContentProto ->
                nodeContentMapper.toNodeContent(nodeContentProto)
            }
        }
    }
}