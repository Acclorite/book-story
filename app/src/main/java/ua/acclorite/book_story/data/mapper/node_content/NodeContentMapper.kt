/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.mapper.node_content

import ua.acclorite.book_story.domain.model.book.NodeContent
import ua.acclorite.book_story.proto.NodeContentListProto
import ua.acclorite.book_story.proto.NodeContentProto

interface NodeContentMapper {
    fun toNodeContentProto(nodeContent: NodeContent): NodeContentProto
    fun toNodeContent(nodeContentProto: NodeContentProto): NodeContent

    fun toNodeContentListProto(nodeContentList: List<NodeContent>): NodeContentListProto
    fun toNodeContentList(nodeContentListProto: NodeContentListProto): List<NodeContent>
}