/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.parser.text

import kotlinx.coroutines.yield
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.core.log.logI
import ua.acclorite.book_story.data.model.file.CachedFile
import ua.acclorite.book_story.data.parser.document.DocumentParser
import ua.acclorite.book_story.domain.model.reader.ReaderText
import javax.inject.Inject

private const val TAG = "XmlTextParser"

class XmlTextParser @Inject constructor(
    private val documentParser: DocumentParser
) : TextParser {

    override suspend fun parse(cachedFile: CachedFile): List<ReaderText> {
        logI(TAG, "Started XML parsing: ${cachedFile.name}.")

        return try {
            val readerText = cachedFile.openInputStream()?.use { stream ->
                documentParser.parseDocument(Jsoup.parse(stream, null, "", Parser.xmlParser()))
            }

            yield()

            if (
                readerText.isNullOrEmpty() ||
                readerText.filterIsInstance<ReaderText.Text>().isEmpty() ||
                readerText.filterIsInstance<ReaderText.Chapter>().isEmpty()
            ) {
                logE(TAG, "Could not extract text from XML.")
                return emptyList()
            }

            logI(TAG, "Successfully finished XML parsing.")
            readerText
        } catch (e: Exception) {
            logE(TAG, "Could not parse text with message: ${e.message}.")
            emptyList()
        }
    }
}