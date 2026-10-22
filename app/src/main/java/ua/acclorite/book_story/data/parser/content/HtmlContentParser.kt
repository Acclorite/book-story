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
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import ua.acclorite.book_story.R
import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.core.log.logI
import ua.acclorite.book_story.core.ui.UIText
import ua.acclorite.book_story.data.parser.content.document.JsoupDocumentParser
import ua.acclorite.book_story.data.storage.file.CachedFile
import ua.acclorite.book_story.domain.model.book.NodeContent
import ua.acclorite.book_story.domain.model.file.FilePath
import ua.acclorite.book_story.domain.model.file.ImageMetadata
import java.io.InputStream
import javax.inject.Inject

private const val TAG = "HtmlContentParser"

class HtmlContentParser @Inject constructor(
    private val jsoupDocumentParser: JsoupDocumentParser
) : ContentParser {

    override fun parse(
        cachedFile: CachedFile,
        cacheImage: suspend (filename: String, inputStream: InputStream) -> ImageMetadata?,
        cacheNodes: suspend (List<NodeContent>) -> FilePath?
    ): Flow<ContentParserState> = flow {
        try {
            logI(TAG, "Started HTML parsing of [${cachedFile.name}].")
            emit(ContentParserState.Progress(0.0f))

            currentCoroutineContext().ensureActive()

            val nodePath = cacheNodes(cachedFile.openInputStream()?.use { stream ->
                emit(ContentParserState.Progress(0.5f))
                jsoupDocumentParser.parseDocument(
                    document = Jsoup.parse(
                        stream,
                        null,
                        "",
                        Parser.htmlParser()
                    ),
                    images = emptyMap() // HTML has no images
                )
            } ?: throw IllegalStateException("Could not open Input Stream in HTML."))

            currentCoroutineContext().ensureActive()

            if (nodePath == null) {
                emit(ContentParserState.Error(UIText.StringResource(R.string.error_empty_file)))
                logE(TAG, "Could not extract nodes from HTML (file is empty).")
                return@flow
            }

            currentCoroutineContext().ensureActive()

            emit(ContentParserState.Progress(1.0f))
            emit(ContentParserState.Success(listOf(nodePath)))

            logI(TAG, "Successfully finished HTML parsing.")
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
            logE(TAG, "Could not extract nodes from HTML with error: ${e.message}).")
        }
    }.flowOn(Dispatchers.IO)
}