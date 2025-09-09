/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.data.parser.epub

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import ua.blindmint.codex.R
import ua.blindmint.codex.data.parser.FileParser
import ua.blindmint.codex.domain.file.CachedFile
import ua.blindmint.codex.domain.library.book.Book
import ua.blindmint.codex.domain.library.book.BookWithCover
import ua.blindmint.codex.domain.library.category.Category
import ua.blindmint.codex.domain.ui.UIText
import java.io.File
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
                    val document = Jsoup.parse(opfContent)

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
                        .run {
                            if (isNotBlank()) {
                                document
                                    .select("manifest > item[id=$this]")
                                    .attr("href")
                                    .apply { if (isNotBlank()) return@run this }
                            }

                            document
                                .select("manifest > item[media-type*=image]")
                                .firstOrNull()?.attr("href")
                        }

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
                            category = Category.entries[0],
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