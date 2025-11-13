/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.parser.cover

import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import ua.acclorite.book_story.core.CoverImage
import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.data.model.file.CachedFile
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.zip.ZipFile
import javax.inject.Inject

class EpubCoverParser @Inject constructor() : CoverParser {

    override suspend fun parse(cachedFile: CachedFile): CoverImage? {
        return try {
            var coverImage: CoverImage? = null

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

                    val coverImagePath = document
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
                        ?: return@use

                    zip.entries().asSequence().first { entry ->
                        if (entry.name.endsWith(coverImagePath)) {
                            val imageBytes = zip.getInputStream(entry).readBytes()
                            coverImage = BitmapFactory.decodeByteArray(
                                imageBytes, 0, imageBytes.size
                            )
                            return@first true
                        } else return@first false
                    }
                }
            }
            coverImage
        } catch (_: Exception) {
            logE("Could not parse cover image.")
            null
        }
    }
}