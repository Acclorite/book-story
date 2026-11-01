/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.parser.cover

import android.graphics.BitmapFactory
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.parser.Parser
import ua.acclorite.book_story.core.CoverImage
import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.core.log.logW
import ua.acclorite.book_story.data.parser.content.document.decodeDocumentPath
import ua.acclorite.book_story.data.parser.content.document.resolveDocumentPath
import ua.acclorite.book_story.data.storage.file.CachedFile
import java.io.InputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import javax.inject.Inject
import kotlin.io.encoding.Base64

private const val TAG = "EpubCoverParser"
private const val MAX_COVER_DIMENSION = 1024
private const val MAX_COVER_RESOURCES = 5
private val coverWhitespace = Regex("\\s+")

private data class CoverReference(val path: String, val fragment: String?)

private fun Element.localTag(): String = normalName().substringAfter(':')

private fun Element.hasToken(attribute: String, token: String): Boolean =
    token in attr(attribute).trim().split(coverWhitespace)

private fun Element.hasSemantic(token: String): Boolean =
    hasToken("epub:type", token) || hasToken("ops:type", token)

private fun coverReference(base: String, href: String): CoverReference? {
    val path = resolveDocumentPath(base, href) ?: return null
    val fragment = href.substringAfter('#', "").takeIf { it.isNotEmpty() }?.let(::decodeDocumentPath)
    return CoverReference(path, fragment)
}

class EpubCoverParser @Inject constructor() : CoverParser {

    override suspend fun parse(cachedFile: CachedFile): CoverImage? = withContext(Dispatchers.IO) {
        try {
            currentCoroutineContext().ensureActive()
            val rawFile = cachedFile.rawFile
            if (rawFile == null || !rawFile.exists() || !rawFile.canRead()) return@withContext null
            ZipFile(rawFile).use { zip ->
                val packageEntry = findPackageEntry(zip) ?: return@use null
                val document = readDocument(zip, packageEntry)
                val manifest = document.getAllElements().filter {
                    it.localTag() == "item" && it.parent()?.localTag() == "manifest"
                }
                val mediaTypes = manifest.mapNotNull { item ->
                    resolveDocumentPath(packageEntry.name, item.attr("href"))?.let {
                        it to item.attr("media-type").lowercase()
                    }
                }.toMap()
                val references = buildList {
                    manifest.filter { it.hasToken("properties", "cover-image") }.forEach {
                        coverReference(packageEntry.name, it.attr("href"))?.let(::add)
                    }
                    document.getAllElements().filter {
                        it.localTag() == "meta" && it.parent()?.localTag() == "metadata" &&
                            it.attr("name").equals("cover", true)
                    }.forEach { meta ->
                        manifest.firstOrNull { it.id() == meta.attr("content").trim() }?.let {
                            coverReference(packageEntry.name, it.attr("href"))?.let(::add)
                        }
                    }
                    document.getAllElements().filter {
                        it.localTag() == "reference" && it.parent()?.localTag() == "guide" &&
                            it.hasToken("type", "cover")
                    }.forEach {
                        coverReference(packageEntry.name, it.attr("href"))?.let(::add)
                    }
                }
                for (reference in references.distinct()) {
                    readCover(zip, reference, mediaTypes, mutableSetOf())?.let { return@use it }
                }
                for (nav in manifest.filter { it.hasToken("properties", "nav") }) {
                    currentCoroutineContext().ensureActive()
                    val path = resolveDocumentPath(packageEntry.name, nav.attr("href")) ?: continue
                    val entry = zip.getEntry(path)?.takeUnless { it.isDirectory } ?: continue
                    val navigation = readDocument(zip, entry)
                    val links = navigation.getAllElements().filter { element ->
                        element.localTag() == "a" && element.hasSemantic("cover") &&
                            element.parents().any { it.localTag() == "nav" && it.hasSemantic("landmarks") }
                    }
                    for (link in links) {
                        val reference = coverReference(path, link.attr("href")) ?: continue
                        readCover(zip, reference, mediaTypes, mutableSetOf())?.let { return@use it }
                    }
                }
                null
            }
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (exception: Exception) {
            logE(TAG, "Could not parse cover image with message: " + exception.message + ".")
            null
        }
    }

    private fun findPackageEntry(zip: ZipFile): ZipEntry? {
        zip.getEntry("META-INF/container.xml")?.takeUnless { it.isDirectory }?.let { container ->
            try {
                val roots = readDocument(zip, container).getAllElements().filter { it.localTag() == "rootfile" }
                val root = roots.firstOrNull { it.attr("media-type") == "application/oebps-package+xml" }
                    ?: roots.firstOrNull()
                val path = root?.attr("full-path")?.let { resolveDocumentPath("", it) }
                path?.let(zip::getEntry)?.takeUnless { it.isDirectory }?.let { return it }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (exception: Exception) {
                logW(TAG, "Could not read EPUB container: " + exception.message)
            }
        }
        return zip.entries().asSequence().firstOrNull {
            !it.isDirectory && it.name.endsWith(".opf", true)
        }
    }

    private fun readDocument(zip: ZipFile, entry: ZipEntry): Document =
        zip.getInputStream(entry).use { Jsoup.parse(it, null, "", Parser.xmlParser()) }

    private suspend fun readCover(
        zip: ZipFile,
        reference: CoverReference,
        mediaTypes: Map<String, String>,
        visited: MutableSet<CoverReference>
    ): CoverImage? {
        currentCoroutineContext().ensureActive()
        if (visited.size >= MAX_COVER_RESOURCES || !visited.add(reference)) return null
        return try {
            val entry = zip.getEntry(reference.path)?.takeUnless { it.isDirectory } ?: return null
            val mediaType = mediaTypes[reference.path]
            val extension = reference.path.substringAfterLast('.').lowercase()
            if (mediaType in setOf("application/xhtml+xml", "text/html", "image/svg+xml") ||
                extension in setOf("html", "htm", "xhtml", "svg")
            ) {
                val document = readDocument(zip, entry)
                val root = if (reference.fragment == null) document
                    else document.getAllElements().firstOrNull { it.id() == reference.fragment } ?: return null
                val sources = root.getAllElements().filter { element ->
                    element.localTag() in setOf("img", "image", "object") &&
                        (listOf(element) + element.parents()).none {
                            it.hasAttr("hidden") || it.localTag() in setOf("head", "script", "template")
                        }
                }.mapNotNull { element ->
                    when (element.localTag()) {
                        "img" -> element.attr("src")
                        "object" -> element.attr("data")
                        else -> element.attr("href").ifBlank {
                            element.attributes().firstOrNull { it.key.substringAfter(':') == "href" }?.value.orEmpty()
                        }
                    }.trim().takeIf { it.isNotEmpty() }
                }.distinct()
                val source = sources.singleOrNull() ?: return null
                if (source.startsWith("data:image/", true)) {
                    val comma = source.indexOf(',')
                    if (comma < 0 || "base64" !in source.substring(0, comma).lowercase().split(';')) return null
                    val bytes = Base64.decode(source.substring(comma + 1).filterNot { it in " \t\r\n" })
                    decodeRaster { bytes.inputStream() }
                } else {
                    val nested = coverReference(reference.path, source) ?: return null
                    readCover(zip, nested, mediaTypes, visited)
                }
            } else {
                decodeRaster { zip.getInputStream(entry) }
            }
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (exception: Exception) {
            logW(TAG, "Could not read declared cover [" + reference.path + "]: " + exception.message)
            null
        }
    }

    private suspend fun decodeRaster(openStream: () -> InputStream): CoverImage? {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        openStream().use { BitmapFactory.decodeStream(it, null, bounds) }
        if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null
        currentCoroutineContext().ensureActive()
        var sampleSize = 1
        val longestSide = maxOf(bounds.outWidth, bounds.outHeight)
        while (longestSide.toLong() > MAX_COVER_DIMENSION.toLong() * sampleSize) sampleSize *= 2
        val options = BitmapFactory.Options().apply { inSampleSize = sampleSize }
        return openStream().use { BitmapFactory.decodeStream(it, null, options) }
    }
}
