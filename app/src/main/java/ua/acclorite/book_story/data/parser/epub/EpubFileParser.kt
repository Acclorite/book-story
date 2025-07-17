/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.parser.epub

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import ua.acclorite.book_story.R
import ua.acclorite.book_story.core.ui.UIText
import ua.acclorite.book_story.data.model.common.BookWithCover
import ua.acclorite.book_story.data.model.file.CachedFile
import ua.acclorite.book_story.data.parser.FileParser
import ua.acclorite.book_story.domain.model.library.Book
import java.io.File
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.zip.ZipFile
import javax.inject.Inject

class EpubFileParser @Inject constructor() : FileParser {

    override suspend fun parse(cachedFile: CachedFile): BookWithCover? {
        return try {
            var book: BookWithCover? = null

            val rawFile = cachedFile.rawFile
            if (rawFile == null || !rawFile.exists() || !rawFile.canRead()) return null

            withContext(Dispatchers.IO) {
                ZipFile(rawFile).use { zip ->
                    val opfEntry = zip.entries().asSequence().find { entry ->
                        entry.name.endsWith(".opf", ignoreCase = true)
                    } ?: return@withContext

                    val opfContent = zip
                        .getInputStream(opfEntry)
                        .bufferedReader()
                        .use { it.readText() }
                    val document = Jsoup.parse(opfContent, Parser.xmlParser())

                    val title = document.select("metadata > dc|title").text().trim().run {
                        ifBlank {
                            cachedFile.name.substringBeforeLast(".").trim()
                        }
                    }

                    val author = document.select("metadata > dc|creator").text().trim().run {
                        if (isBlank()) {
                            UIText.StringResource(R.string.unknown_author)
                        } else {
                            UIText.StringValue(this)
                        }
                    }

                    val description = Jsoup.parse(
                        document.select("metadata > dc|description").text()
                    ).text().run {
                        ifBlank {
                            null
                        }
                    }

                    val coverImage = document
                        .select("metadata > meta[name=cover]")
                        .attr("content")
                        .let { coverId ->
                            if (coverId.isNotBlank()) {
                                document
                                    .select("manifest > item[id=$coverId]")
                                    .attr("href")
                                    .also { src ->
                                        if (src.isBlank()) return@also
                                        return@let src
                                    }
                            }

                            document
                                .select("manifest > item[media-type*=image]")
                                .firstOrNull()
                                ?.attr("href")
                                ?.also { src -> if (src.isBlank()) return@let null }
                        }
                        ?.let { src -> URLDecoder.decode(src, StandardCharsets.UTF_8.name()) }

                    book = BookWithCover(
                        book = Book(
                            title = title,
                            author = author,
                            description = description,
                            scrollIndex = 0,
                            scrollOffset = 0,
                            progress = 0f,
                            filePath = cachedFile.path,
                            lastOpened = null,
                            categories = emptyList(),
                            coverImage = null
                        ),
                        coverImage = extractCoverImageBitmap(rawFile, coverImage)
                    )
                }
            }
            book
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun extractCoverImageBitmap(file: File, coverImagePath: String?): Bitmap? {
        if (coverImagePath.isNullOrBlank()) {
            return null
        }

        ZipFile(file).use { zip ->
            zip.entries().asSequence().forEach { entry ->
                if (entry.name.endsWith(coverImagePath)) {
                    val imageBytes = zip.getInputStream(entry).readBytes()
                    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                }
            }
        }

        return null
    }
}