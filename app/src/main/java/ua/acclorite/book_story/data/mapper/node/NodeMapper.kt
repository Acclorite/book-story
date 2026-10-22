/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.mapper.node

import ua.acclorite.book_story.data.storage.room.entity.content.NodeEntity
import ua.acclorite.book_story.domain.model.book.Node

interface NodeMapper {
    fun toNodeEntity(node: Node): NodeEntity
    fun toNode(nodeEntity: NodeEntity): Node
}