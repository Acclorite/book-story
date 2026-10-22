/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.parser.content

import android.app.Application
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
import kotlin.coroutines.cancellation.CancellationException

private const val TAG = "PdfContentParser"

/**
 * ! THIS IS CURRENTLY BAD !
 *
 * It will be improved in the future, for now the previous implementation was left.
 */
class PdfContentParser @Inject constructor(
    private val application: Application
) : ContentParser {

    override fun parse(
        cachedFile: CachedFile,
        cacheImage: suspend (filename: String, inputStream: InputStream) -> ImageMetadata?,
        cacheNodes: suspend (List<NodeContent>) -> FilePath?
    ): Flow<ContentParserState> = flow {
        try {
            logI(TAG, "Started PDF parsing of [${cachedFile.name}].")
            emit(ContentParserState.Progress(0.0f))

            PDFBoxResourceLoader.init(application)

            currentCoroutineContext().ensureActive()
            emit(ContentParserState.Progress(0.25f))

            val text = cachedFile.openInputStream()?.use { inputStream ->
                getText(inputStream)
            } ?: throw IllegalStateException("Could not open Input Stream in XML.")

            currentCoroutineContext().ensureActive()
            emit(ContentParserState.Progress(0.5f))

            val processedText = processText(text)

            currentCoroutineContext().ensureActive()
            emit(ContentParserState.Progress(0.75f))

            val nodePath = cacheNodes(parseNodes(processedText))

            currentCoroutineContext().ensureActive()

            if (nodePath == null) {
                emit(ContentParserState.Error(UIText.StringResource(R.string.error_empty_file)))
                logE(TAG, "Could not extract nodes from PDF (file is empty).")
                return@flow
            }

            emit(ContentParserState.Progress(1.0f))
            emit(ContentParserState.Success(listOf(nodePath)))

            logI(TAG, "Successfully finished PDF parsing.")
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
            logE(TAG, "Could not extract nodes from PDF with error: ${e.message}.")
            return@flow
        }
    }

    /**
     * Strips all text from PDF document. Has broken line breaks.
     */
    private fun getText(inputStream: InputStream): List<String> {
        val pdfStripper = PDFTextStripper()
        pdfStripper.paragraphStart = "</br>"

        return PDDocument.load(inputStream).use { document ->
            pdfStripper.getText(document)
                .replace("\r", "")
                .let { text ->
                    text.filterIndexed { index, c ->
                        if (c == ' ') {
                            text[index - 1] != ' '
                        } else {
                            true
                        }
                    }
                }
                .split("${pdfStripper.paragraphStart}|\\n".toRegex())
                .filter { line -> line.isNotBlank() }
        }
    }

    /**
     * Processed stripped from PDF document text with hope to fix
     * broken line breaks.
     */
    private fun processText(text: List<String>): List<String> {
        val processedText = mutableListOf<String>()
        text.forEachIndexed { index, line ->
            val line = line.trim()

            if (index == 0) {
                processedText.add(line)
                return@forEachIndexed
            }

            if (line.all { it.isDigit() }) {
                return@forEachIndexed
            }

            if (line.first().isLowerCase()) {
                val currentLine = processedText[processedText.lastIndex]

                if (currentLine.last() == '-') {
                    if (currentLine[currentLine.lastIndex - 1].isLowerCase()) {
                        processedText[processedText.lastIndex] = currentLine.dropLast(1) + line
                        return@forEachIndexed
                    }
                }

                processedText[processedText.lastIndex] += " $line"
                return@forEachIndexed
            }

            if (line.first().isUpperCase() || line.first().isDigit()) {
                processedText.add(line)
                return@forEachIndexed
            }

            if (line.first().isLetter()) {
                processedText[processedText.lastIndex] += " $line"
                return@forEachIndexed
            }
        }
        return processedText
    }

    /**
     * Parses processed text that was stripped from PDF document
     * and maps it to list of [NodeContent].
     */
    private fun parseNodes(text: List<String>): List<NodeContent> {
        return text.mapNotNull { line ->
            when {
                line == "***" || line == "---" -> {
                    NodeContent.Separator
                }

                else -> {
                    if (line.containsVisibleText()) {
                        NodeContent.Paragraph(line.trim(), styles = emptyList())
                    } else {
                        null
                    }
                }
            }
        }
    }
}