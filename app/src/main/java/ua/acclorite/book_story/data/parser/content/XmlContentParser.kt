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
import org.jsoup.nodes.Document
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

private const val TAG = "XmlContentParser"

class XmlContentParser @Inject constructor(
    private val jsoupDocumentParser: JsoupDocumentParser
) : ContentParser {

    override fun parse(
        cachedFile: CachedFile,
        cacheImage: suspend (filename: String, inputStream: InputStream) -> ImageMetadata?,
        cacheNodes: suspend (List<NodeContent>) -> FilePath?
    ): Flow<ContentParserState> = flow {
        try {
            logI(TAG, "Started XML parsing of [${cachedFile.name}].")
            emit(ContentParserState.Progress(0.0f))

            currentCoroutineContext().ensureActive()

            val nodePath = cacheNodes(cachedFile.openInputStream()?.use { stream ->
                emit(ContentParserState.Progress(0.25f))

                val document = Jsoup.parse(stream, null, "", Parser.xmlParser())

                val images = getImagesFromDocument(document, cacheImage)
                logI(TAG, "Images, size: ${images.size}")

                emit(ContentParserState.Progress(0.5f))

                jsoupDocumentParser.parseDocument(
                    document,
                    images = images // FB2 embeds images as binary inside
                )
            } ?: throw IllegalStateException("Could not open Input Stream in XML."))

            currentCoroutineContext().ensureActive()

            if (nodePath == null) {
                emit(ContentParserState.Error(UIText.StringResource(R.string.error_empty_file)))
                logE(TAG, "Could not extract nodes from XML (file is empty).")
                return@flow
            }

            currentCoroutineContext().ensureActive()

            emit(ContentParserState.Progress(1.0f))
            emit(ContentParserState.Success(listOf(nodePath)))

            logI(TAG, "Successfully finished XML parsing.")
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
            logE(TAG, "Could not extract nodes from XML with error: ${e.message}.")
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Gets images (and saves them to cache for further use) from Jsoup Document. Also extracts
     * metadata from them (such as width and height).
     */
    private suspend fun getImagesFromDocument(
        document: Document,
        cacheImage: suspend (filename: String, inputStream: InputStream) -> ImageMetadata?
    ): Map<String, ImageMetadata> {
        return jsoupDocumentParser.extractBinaryImages(document)
            .mapNotNull { binaryImage ->
                binaryImage.bytes.inputStream().use { inputStream ->
                    cacheImage(
                        binaryImage.name,
                        inputStream
                    ).let { if (it != null) it.originalName to it else null }
                }
            }.associate { it }
    }
}