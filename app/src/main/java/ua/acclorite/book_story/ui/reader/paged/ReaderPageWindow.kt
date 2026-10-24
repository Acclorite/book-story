/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.reader.paged

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import ua.acclorite.book_story.domain.model.book.Node
import ua.acclorite.book_story.domain.model.book.NodeContent
import ua.acclorite.book_story.domain.model.book.NodeId

internal suspend fun loadReaderPageWindow(
    nodes: List<Node>,
    cached: List<Node>,
    images: Boolean,
    loadBefore: suspend (NodeId, Int) -> List<Node>
): List<Node> {
    if (nodes.isEmpty()) return emptyList()
    val firstId = nodes.first().id.id
    val preceding = cached.takeWhile { it.id.id < firstId }
    var result = if (preceding.lastOrNull()?.id?.id == firstId - 1) preceding + nodes else nodes
    while (true) {
        currentCoroutineContext().ensureActive()
        var previousWasHeader = false
        var boundary: Int? = null
        result.forEachIndexed { index, node ->
            if (node.id.id > firstId) return@forEachIndexed
            if (!images && node.content is NodeContent.Image) return@forEachIndexed
            val header = node.content is NodeContent.Header
            if (header && !previousWasHeader) boundary = index
            previousWasHeader = header
        }
        boundary?.takeIf { it > 0 }?.let { return result.drop(it) }
        if (result.first().id.id == 0) return result
        val earlier = loadBefore(result.first().id, 100)
        if (earlier.isEmpty() || earlier.last().id.id != result.first().id.id - 1) {
            return emptyList()
        }
        result = earlier + result
    }
}
