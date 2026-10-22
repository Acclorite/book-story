/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.parser.content

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.flow.Flow
import ua.acclorite.book_story.core.ui.UIText
import ua.acclorite.book_story.data.storage.file.CachedFile
import ua.acclorite.book_story.domain.model.book.NodeContent
import ua.acclorite.book_story.domain.model.file.FilePath
import ua.acclorite.book_story.domain.model.file.ImageMetadata
import java.io.InputStream

@Immutable
sealed class ContentParserState {
    @Immutable
    data class Progress(val progress: Float) : ContentParserState()

    @Immutable
    data class Success(val nodePaths: List<FilePath>) : ContentParserState()

    @Immutable
    data class Error(val message: UIText) : ContentParserState()
}

interface ContentParser {
    fun parse(
        cachedFile: CachedFile,
        cacheImage: suspend (filename: String, inputStream: InputStream) -> ImageMetadata?,
        cacheNodes: suspend (List<NodeContent>) -> FilePath?
    ): Flow<ContentParserState>
}