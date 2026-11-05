/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.reader.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString

@Immutable
data class ReaderLinkUrl(val start: Int, val end: Int, val url: String) {
    val suffix: String = " ($url)"
}

fun AnnotatedString.withLinkUrls(links: List<ReaderLinkUrl>): AnnotatedString {
    if (links.isEmpty()) return this
    return buildAnnotatedString {
        var offset = 0
        links.forEach { link ->
            append(this@withLinkUrls.subSequence(offset, link.end))
            val start = length
            append(link.suffix)
            spanStylesAtLinkEnd(this@withLinkUrls, link.end).forEach {
                addStyle(it.item, start, length)
            }
            val annotation = this@withLinkUrls.getLinkAnnotations(link.start, link.end)
                .firstOrNull { (it.item as? LinkAnnotation.Url)?.url == link.url }?.item
                ?: LinkAnnotation.Url(link.url)
            addLink(annotation as LinkAnnotation.Url, start, length)
            offset = link.end
        }
        append(this@withLinkUrls.subSequence(offset, this@withLinkUrls.length))
    }
}

private fun spanStylesAtLinkEnd(text: AnnotatedString, end: Int) =
    text.spanStyles.filter { it.start < end && it.end >= end }
