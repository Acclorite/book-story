/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.parser.content.document

import org.jsoup.nodes.Document
import ua.acclorite.book_story.core.log.logW
import ua.acclorite.book_story.domain.model.file.ImageMetadata
import java.io.InputStream
import kotlin.io.encoding.Base64

suspend fun cacheEmbeddedDocumentImages(
    document: Document,
    cacheImage: suspend (String, InputStream) -> ImageMetadata?
): Map<String, ImageMetadata> {
    val images = mutableMapOf<String, ImageMetadata>()
    val sources = document.getAllElements().filter {
        it.normalName().substringAfter(':') in setOf("img", "image")
    }.map { element ->
        element.attr("src").ifBlank {
            element.attr("href").ifBlank {
                element.attributes().firstOrNull { it.key.endsWith(":href") }?.value.orEmpty()
            }
        }.trim()
    }.filter { it.startsWith("data:image/", true) }.distinct()

    for ((index, source) in sources.withIndex()) {
        val comma = source.indexOf(',')
        if (comma < 0) continue
        val header = source.substring(5, comma)
        if (header.split(';').none { it.equals("base64", true) }) continue
        val extension = when (header.substringBefore(';').lowercase()) {
            "image/png" -> "png"
            "image/jpeg" -> "jpg"
            "image/gif" -> "gif"
            "image/webp" -> "webp"
            "image/bmp" -> "bmp"
            "image/avif" -> "avif"
            else -> continue
        }
        val bytes = try {
            Base64.decode(source.substring(comma + 1).filterNot { it in " \t\r\n" })
        } catch (exception: IllegalArgumentException) {
            logW("EmbeddedDocumentImages", "Skipping invalid embedded image: ${exception.message}")
            continue
        }
        bytes.inputStream().use {
            cacheImage("embedded-$index.$extension", it)?.let { metadata -> images[source] = metadata }
        }
    }
    return images
}
