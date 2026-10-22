/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.parser.content

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ua.acclorite.book_story.R
import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.core.log.logI
import ua.acclorite.book_story.core.ui.UIText
import ua.acclorite.book_story.data.storage.file.CachedFile
import ua.acclorite.book_story.domain.model.book.NodeContent
import ua.acclorite.book_story.domain.model.file.FilePath
import ua.acclorite.book_story.domain.model.file.ImageMetadata
import java.io.InputStream
import javax.inject.Inject

private const val TAG = "ContentParser"

class ContentParserImpl @Inject constructor(
    private val txtContentParser: TxtContentParser,
    private val htmlContentParser: HtmlContentParser,
    private val xmlContentParser: XmlContentParser,
    private val epubContentParser: EpubContentParser,
    private val pdfContentParser: PdfContentParser
) : ContentParser {

    override fun parse(
        cachedFile: CachedFile,
        cacheImage: suspend (filename: String, inputStream: InputStream) -> ImageMetadata?,
        cacheNodes: suspend (List<NodeContent>) -> FilePath?
    ): Flow<ContentParserState> = flow {
        if (!cachedFile.canAccess()) {
            logE(TAG, "File does not exist or no read access is granted.")
            emit(ContentParserState.Error(UIText.StringResource(R.string.error_could_not_access_file)))
            return@flow
        }

        logI(TAG, "Parsing content of [${cachedFile.name}].")
        when (cachedFile.extension) {
            ".pdf" -> {
                emitAll(pdfContentParser.parse(cachedFile, cacheImage, cacheNodes))
            }

            ".epub" -> {
                emitAll(epubContentParser.parse(cachedFile, cacheImage, cacheNodes))
            }

            ".txt" -> {
                emitAll(txtContentParser.parse(cachedFile, cacheImage, cacheNodes))
            }

            ".fb2" -> {
                emitAll(xmlContentParser.parse(cachedFile, cacheImage, cacheNodes))
            }

            ".html" -> {
                emitAll(htmlContentParser.parse(cachedFile, cacheImage, cacheNodes))
            }

            ".htm" -> {
                emitAll(htmlContentParser.parse(cachedFile, cacheImage, cacheNodes))
            }

            else -> {
                logE(TAG, "Wrong file format, could not find supported extension.")
                emit(ContentParserState.Error(UIText.StringResource(R.string.error_wrong_file_format)))
            }
        }
    }.flowOn(Dispatchers.IO)
}