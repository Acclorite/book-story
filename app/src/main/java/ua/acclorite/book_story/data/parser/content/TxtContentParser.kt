/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.parser.content

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ua.acclorite.book_story.R
import ua.acclorite.book_story.core.helpers.containsVisibleText
import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.core.log.logI
import ua.acclorite.book_story.core.ui.UIText
import ua.acclorite.book_story.data.storage.file.CachedFile
import ua.acclorite.book_story.domain.model.book.NodeContent
import ua.acclorite.book_story.domain.model.file.FilePath
import ua.acclorite.book_story.domain.model.file.ImageMetadata
import java.io.InputStream
import javax.inject.Inject

private const val TAG = "TxtContentParser"
private const val NODES_CHUNK_SIZE = 30000 // Each file is ~50 KB

class TxtContentParser @Inject constructor() : ContentParser {

    override fun parse(
        cachedFile: CachedFile,
        cacheImage: suspend (filename: String, inputStream: InputStream) -> ImageMetadata?,
        cacheNodes: suspend (List<NodeContent>) -> FilePath?
    ): Flow<ContentParserState> = flow {
        try {
            logI(TAG, "Started TXT parsing of [${cachedFile.name}].")
            emit(ContentParserState.Progress(0.0f))

            val nodePaths = mutableListOf<FilePath>()

            currentCoroutineContext().ensureActive()

            cachedFile.openInputStream()?.bufferedReader()?.use { reader ->
                emit(ContentParserState.Progress(0.5f))

                reader.lineSequence().chunked(NODES_CHUNK_SIZE).forEach { chunk ->
                    cacheNodes(
                        chunk.mapNotNull { line ->
                            if (!line.containsVisibleText()) return@mapNotNull null
                            NodeContent.Paragraph(text = line, styles = emptyList())
                        }
                    )?.let { path -> nodePaths.add(path) }
                }
            } ?: throw IllegalStateException("Could not open Input Stream in TXT.")

            currentCoroutineContext().ensureActive()

            if (nodePaths.isEmpty()) {
                emit(ContentParserState.Error(UIText.StringResource(R.string.error_empty_file)))
                logE(TAG, "Could not extract nodes from TXT (file is empty).")
                return@flow
            }

            emit(ContentParserState.Progress(1.0f))
            emit(ContentParserState.Success(nodePaths))

            logI(TAG, "Successfully finished TXT parsing.")
        } catch (c: CancellationException) {
            throw c
        } catch (e: Exception) {
            emit(
                ContentParserState.Error(
                    UIText.StringResource(
                        R.string.error_could_not_parse_file,
                        e.message ?: ""
                    )
                )
            )
            logE(TAG, "Could not extract nodes from TXT with error: ${e.message}.")
            return@flow
        }
    }.flowOn(Dispatchers.IO)
}