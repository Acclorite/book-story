/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.parser.content.document

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.parser.Parser
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import ua.acclorite.book_story.core.helpers.cleanHeaderText

private fun Element.localName() = normalName().substringAfter(':')

fun readEpubNavigation(zip: ZipFile, opf: ZipEntry?): Map<String, List<DocumentOutlineEntry>> {
    if (opf == null) return emptyMap()
    val packageDocument = zip.getInputStream(opf).use { Jsoup.parse(it, null, "", Parser.xmlParser()) }
    val manifest = packageDocument.getAllElements().filter {
        it.localName() == "item" && it.parent()?.localName() == "manifest"
    }
    val result = linkedMapOf<String, MutableList<DocumentOutlineEntry>>()
    fun add(base: String, href: String, title: String, level: Int) {
        if (href.isBlank()) return
        val path = if (href.trim().startsWith('#')) base else resolveDocumentPath(base, href) ?: return
        val label = title.cleanHeaderText()
        if (label.isEmpty()) return
        val fragment = href.substringAfter('#', "").takeIf { it.isNotBlank() }?.let { decodeDocumentPath(it) }
        val entries = result.getOrPut(path) { mutableListOf() }
        if (entries.none { it.fragment == fragment }) entries.add(DocumentOutlineEntry(fragment, label, level.coerceIn(1, 6)))
    }
    val navigationItem = manifest.firstOrNull { "nav" in it.attr("properties").split(Regex("\\s+")) }
    val navigationPath = navigationItem?.attr("href")?.let { resolveDocumentPath(opf.name, it) }
    navigationPath?.let { path -> zip.getEntry(path)?.let { entry ->
        val document = zip.getInputStream(entry).use { Jsoup.parse(it, null, "", Parser.htmlParser()) }
        val toc = document.getAllElements().firstOrNull { element ->
            element.localName() == "nav" && (
                element.attributes().any { it.key.substringAfter(':') == "type" && "toc" in it.value.split(Regex("\\s+")) } ||
                    element.attr("role") == "doc-toc"
                )
        }
        toc?.getAllElements()?.filter { it.localName() == "a" }?.forEach { anchor ->
            val level = anchor.parents().takeWhile { it !== toc }.count { it.localName() == "li" }.coerceAtLeast(1)
            add(path, anchor.attr("href"), anchor.text(), level)
        }
    } }
    if (result.isNotEmpty()) return result

    val spine = packageDocument.getAllElements().firstOrNull { it.localName() == "spine" }
    val ncxItem = manifest.firstOrNull { it.id() == spine?.attr("toc") }
        ?: manifest.firstOrNull { it.attr("media-type") == "application/x-dtbncx+xml" }
    val ncxPath = ncxItem?.attr("href")?.let { resolveDocumentPath(opf.name, it) } ?: return emptyMap()
    val ncx = zip.getEntry(ncxPath) ?: return emptyMap()
    val document = zip.getInputStream(ncx).use { Jsoup.parse(it, null, "", Parser.xmlParser()) }
    document.getAllElements().filter { it.localName() == "navpoint" }.forEach { point ->
        val label = point.children().firstOrNull { it.localName() == "navlabel" }?.text().orEmpty()
        val source = point.children().firstOrNull { it.localName() == "content" }?.attr("src").orEmpty()
        val level = point.parents().count { it.localName() == "navpoint" } + 1
        add(ncxPath, source, label, level)
    }
    return result
}
