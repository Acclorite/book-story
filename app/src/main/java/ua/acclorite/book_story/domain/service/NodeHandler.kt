/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.service

import ua.acclorite.book_story.domain.model.book.NodeContent
import ua.acclorite.book_story.domain.model.file.FilePath

interface NodeHandler {
    suspend fun cacheNodes(nodes: List<NodeContent>, tempDirectory: String): Result<FilePath?>
    suspend fun getNodesFromFile(file: FilePath): Result<List<NodeContent>>
}