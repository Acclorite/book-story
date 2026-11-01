/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.parser.content.document

import java.net.URLDecoder
import java.nio.charset.StandardCharsets

fun decodeDocumentPath(path: String): String = try {
    URLDecoder.decode(path.replace("+", "%2B"), StandardCharsets.UTF_8.name())
} catch (_: IllegalArgumentException) {
    path
}

fun resolveDocumentPath(documentPath: String, reference: String): String? {
    val referencePath = reference.trim().substringBefore('#').substringBefore('?')
    if (referencePath.isBlank() || referencePath.startsWith("//") ||
        Regex("^[a-zA-Z][a-zA-Z0-9+.-]*:").containsMatchIn(referencePath)
    ) return null

    val decoded = decodeDocumentPath(referencePath).replace('\\', '/')
    val parent = documentPath.substringBeforeLast('/', "")
    val combined = if (decoded.startsWith('/')) decoded.drop(1)
    else if (parent.isEmpty()) decoded else "$parent/$decoded"
    val segments = mutableListOf<String>()
    for (segment in combined.split('/')) {
        when (segment) {
            "", "." -> Unit
            ".." -> if (segments.isEmpty()) return null else segments.removeAt(segments.lastIndex)
            else -> segments.add(segment)
        }
    }
    return segments.joinToString("/").takeIf { it.isNotEmpty() }
}
