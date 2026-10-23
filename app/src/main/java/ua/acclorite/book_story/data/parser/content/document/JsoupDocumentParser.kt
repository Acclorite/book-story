/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.parser.content.document

import androidx.core.net.toUri
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import ua.acclorite.book_story.core.helpers.containsVisibleText
import ua.acclorite.book_story.core.helpers.foundMatch
import ua.acclorite.book_story.domain.model.book.NodeContent
import ua.acclorite.book_story.domain.model.file.ImageMetadata
import java.io.File
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject
import kotlin.io.encoding.Base64

data class BinaryImage(
    val name: String,
    val bytes: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BinaryImage

        if (name != other.name) return false
        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + bytes.contentHashCode()
        return result
    }
}

class JsoupDocumentParser @Inject constructor() {
    /**
     * Mirrors [NodeContent] for Jsoup in order to parse it and get a list of
     * formatted [NodeContent].
     *
     * Contains various methods to insert markers and parse them.
     *
     * @see [NodeContent]
     */
    private object DocumentContent {
        object Header {
            fun insertMarker(level: Int) = "{{HEADER-L='$level'}}"
            fun isHeader(text: String): Boolean = text.foundMatch("{{HEADER-L='")
            fun getHeaderLevel(text: String): Int? {
                text.indexOf("{{HEADER-L='").let { startIndex ->
                    if (startIndex == -1) return null
                    // headerLevel is at index 12 of header marker
                    return text.getOrNull(startIndex + 12)?.digitToIntOrNull()
                }
            }

            fun getHeaderTitle(text: String): String = text.substringAfter("'}}")
        }

        object Paragraph {
            data class FormattingMarker(
                val id: String,
                val start: String?,
                val startRegex: Regex?,
                val end: String,
                val insertStart: (args: Map<String, String>?) -> String,
                val insertEnd: () -> String,
                val extractArgs: ((MatchResult) -> Map<String, String>)? = null
            )

            val boldMarker = FormattingMarker(
                id = "bold",
                start = "{{B-START}}",
                startRegex = null,
                end = "{{B-END}}",
                insertStart = { "{{B-START}}" },
                insertEnd = { "{{B-END}}" }
            )
            val italicMarker = FormattingMarker(
                id = "italic",
                start = "{{I-START}}",
                startRegex = null,
                end = "{{I-END}}",
                insertStart = { "{{I-START}}" },
                insertEnd = { "{{I-END}}" }
            )
            val linkMarker = FormattingMarker(
                id = "link",
                start = null,
                startRegex = Regex("""\{\{L-START-L='([^']*)'\}\}"""),
                end = "{{L-END}}",
                insertStart = { args -> "{{L-START-L='${args?.get("link").orEmpty()}'}}" },
                insertEnd = { "{{L-END}}" },
                extractArgs = { match -> mapOf("link" to match.groupValues[1]) }
            )

            val markers = listOf(boldMarker, italicMarker, linkMarker)

            fun clearMarkers(textWithMarkers: String): String {
                var result = textWithMarkers
                for (marker in markers) {
                    result = when {
                        marker.start != null -> {
                            result.replace(marker.start, "")
                        }

                        marker.startRegex != null -> {
                            result.replace(marker.startRegex, "")
                        }

                        else -> result
                    }
                    result = result.replace(marker.end, "")
                }
                return result
            }

            fun resolveStyle(
                marker: FormattingMarker,
                args: Map<String, String>?
            ): NodeContent.TextStyle {
                return when (marker.id) {
                    "bold" -> NodeContent.TextStyle.Bold
                    "italic" -> NodeContent.TextStyle.Italic
                    "link" -> NodeContent.TextStyle.Link(args?.get("link").orEmpty())
                    else -> error("Unknown marker: ${marker.id}")
                }
            }

            fun trimWithStyles(
                text: String,
                styles: List<NodeContent.TextStyleRange>
            ): NodeContent.Paragraph {
                val trimmedString = text.trim()
                if (trimmedString.isBlank()) {
                    return NodeContent.Paragraph("", emptyList())
                }

                val trimmedShift = text.indexOf(trimmedString)
                if (trimmedShift <= 0) return NodeContent.Paragraph(text, styles)

                val trimmedStyles = styles.map { style ->
                    val trimmedStart = (style.start - trimmedShift)
                        .coerceIn(0, trimmedString.length)
                    val trimmedEnd = (style.end - trimmedShift)
                        .coerceIn(0, trimmedString.length)

                    style.copy(start = trimmedStart, end = trimmedEnd)
                }.filter { style -> style.start < style.end }

                return NodeContent.Paragraph(trimmedString, trimmedStyles)
            }

            private data class StackMarkerItem(
                val marker: FormattingMarker,
                val startIndex: Int,
                val args: Map<String, String>?
            )

            fun parseFormatting(textWithMarkers: String): NodeContent.Paragraph {
                val outputText = StringBuilder(textWithMarkers.length)
                val styles = mutableListOf<NodeContent.TextStyleRange>()
                val stackMarkers = ArrayDeque<StackMarkerItem>()

                var index = 0
                while (index < textWithMarkers.length) {
                    // Check for closing marker
                    val lastStackMarker = stackMarkers.lastOrNull()
                    if (lastStackMarker != null && textWithMarkers.startsWith(
                            lastStackMarker.marker.end,
                            index
                        )
                    ) {
                        styles.add(
                            NodeContent.TextStyleRange(
                                start = lastStackMarker.startIndex,
                                end = outputText.length,
                                style = resolveStyle(lastStackMarker.marker, lastStackMarker.args)
                            )
                        )
                        stackMarkers.removeLast()
                        index += lastStackMarker.marker.end.length
                        continue
                    }

                    // Check for opening marker
                    var startMarker = false
                    for (marker in markers) {
                        var args: Map<String, String>? = null
                        var markerLength = 0

                        when {
                            marker.start != null -> {
                                if (textWithMarkers.startsWith(marker.start, index)) {
                                    markerLength = marker.start.length
                                }
                            }

                            marker.startRegex != null -> {
                                val startMatch = marker.startRegex.find(textWithMarkers, index)
                                if (startMatch != null && startMatch.range.first == index) {
                                    args = marker.extractArgs?.invoke(startMatch)
                                    markerLength = startMatch.value.length
                                }
                            }
                        }

                        if (markerLength > 0) {
                            stackMarkers.addLast(StackMarkerItem(marker, outputText.length, args))
                            index += markerLength
                            startMarker = true
                            break
                        }
                    }
                    if (startMarker) continue

                    // Append character if no markers were found
                    outputText.append(textWithMarkers[index])
                    index++
                }

                return trimWithStyles(outputText.toString(), styles)
            }
        }

        object Image {
            fun insertMarker(source: String, alt: String) = "{{IMAGE-S='$source'-A='$alt'}}"
            fun isImage(text: String): Boolean = text.foundMatch("{{IMAGE-S='")
            fun parseImage(
                text: String, images: Map<String, ImageMetadata>
            ): NodeContent.Image? {
                val markerStart = "{{IMAGE-S='"
                val startIndex = text.indexOf(markerStart)
                if (startIndex == -1) return null

                val sourceStart = startIndex + markerStart.length
                val sourceEnd = text.indexOf("'-A='", sourceStart)
                if (sourceEnd == -1) return null
                val source = text.substring(sourceStart, sourceEnd)

                val altStart = sourceEnd + 5
                val altEnd = text.indexOf("'}}", altStart)
                if (altEnd == -1) return null
                val alt = text.substring(altStart, altEnd).takeIf { it.isNotBlank() }

                val image = images[source] ?: return null
                return NodeContent.Image(
                    content = image.path.path,
                    caption = alt,
                    width = image.width,
                    height = image.height,
                    aspectRatio = image.aspectRatio
                )
            }
        }

        object Separator {
            fun insertMarker() = "{{SEPARATOR}}"
            fun isSeparator(text: String): Boolean = text.foundMatch(insertMarker())
        }
    }

    /**
     * Applies various fixes and adds markers to the document.
     *
     * Applied document contains formatting markers for later use, such as:
     * "{{HEADER-L-'1'}} Header text" or "This {{B-START}}is{{B-END}} some example text."
     *
     * @param images Optional map of [ImageMetadata.originalName] and
     * [ImageMetadata] to insert images inside returned list.
     */
    private fun Element.processDocument(
        images: Map<String, ImageMetadata>
    ) {
        // Remove manual line breaks from all <p>, <a>
        select("p").forEach { element ->
            element.html(element.html().replace(Regex("\\n+"), " "))
            element.before(Element("br"))
            element.after(Element("br"))
        }
        select("a").forEach { element ->
            element.html(element.html().replace(Regex("\\n+"), ""))
        }

        // Remove <head>'s title
        select("title").remove()

        select("empty-line").forEach { element ->
            element.after("\n")
        }

        // Header type
        select("h1").forEach { element ->
            element.select("br").before(" ").remove()
            element.before("\n${DocumentContent.Header.insertMarker(1)}")
            element.after("\n")
        }
        select("h2").forEach { element ->
            element.select("br").before(" ").remove()
            element.before("\n${DocumentContent.Header.insertMarker(2)}")
            element.after("\n")
        }
        select("h3").forEach { element ->
            element.select("br").before(" ").remove()
            element.before("\n${DocumentContent.Header.insertMarker(3)}")
            element.after("\n")
        }
        select("h4").forEach { element ->
            element.select("br").before(" ").remove()
            element.before("\n${DocumentContent.Header.insertMarker(4)}")
            element.after("\n")
        }
        select("h5").forEach { element ->
            element.select("br").before(" ").remove()
            element.before("\n${DocumentContent.Header.insertMarker(5)}")
            element.after("\n")
        }
        select("h6").forEach { element ->
            element.select("br").before(" ").remove()
            element.before("\n${DocumentContent.Header.insertMarker(6)}")
            element.after("\n")
        }

        // Paragraph type
        select("strong, b, emphasis").forEach { element ->
            element.before(DocumentContent.Paragraph.boldMarker.insertStart(null))
            element.after(DocumentContent.Paragraph.boldMarker.insertEnd())
        }
        select("em, i, cite, dfn, address").forEach { element ->
            element.before(DocumentContent.Paragraph.italicMarker.insertStart(null))
            element.after(DocumentContent.Paragraph.italicMarker.insertEnd())
        }
        select("a").forEach { element ->
            var link = element.attr("href")
            if (!link.startsWith("http") || element.wholeText().isBlank()) return@forEach

            if (link.startsWith("http://")) {
                link = link.replaceFirst("http://", "https://")
            }

            element.before(DocumentContent.Paragraph.linkMarker.insertStart(mapOf("link" to link)))
            element.after(DocumentContent.Paragraph.linkMarker.insertEnd())
        }

        // Image type
        if (images.isNotEmpty()) {
            select("img").forEach { element ->
                val src = element.attr("src").trim().substringAfterLast(File.separator).lowercase()
                    .let { src -> URLDecoder.decode(src, StandardCharsets.UTF_8.name()) }
                    .let { src -> src.toUri().fragment ?: src }
                    .takeIf { src ->
                        if (!src.containsVisibleText()) return@takeIf false
                        if (images[src] == null) return@takeIf false
                        return@takeIf true
                    } ?: return@forEach
                val alt = element.attr("alt").trim().takeIf { alt ->
                    alt.containsVisibleText()
                } ?: ""

                element.before("\n${DocumentContent.Image.insertMarker(src, alt)}\n")
            }
            select("image").forEach { element ->
                val src =
                    element.attr("xlink:href").trim().substringAfterLast(File.separator).lowercase()
                        .let { src -> URLDecoder.decode(src, StandardCharsets.UTF_8.name()) }
                        .let { src -> src.toUri().fragment ?: src }
                        .takeIf { src ->
                            if (!src.containsVisibleText()) return@takeIf false
                            if (images[src] == null) return@takeIf false
                            return@takeIf true
                        } ?: return@forEach
                val alt = ""

                element.before("\n${DocumentContent.Image.insertMarker(src, alt)}\n")
            }
        }

        // Separator type
        select("hr").forEach { element ->
            element.before("\n${DocumentContent.Separator.insertMarker()}\n")
        }
    }

    /**
     * Processes marked by [processDocument] document.
     *
     * Parses text of the document marked with various symbols (such as {{BOLD}})
     * to retrieve a list of [NodeContent].
     *
     * @param images Optional map of [ImageMetadata.originalName] and
     * [ImageMetadata] to insert images inside returned list.
     */
    private fun List<String>.processMarkedDocument(
        images: Map<String, ImageMetadata>
    ): List<NodeContent> {
        val nodes = mutableListOf<NodeContent>()

        forEach { line ->
            if (line.containsVisibleText()) {
                when {
                    DocumentContent.Header.isHeader(line) -> {
                        val title = DocumentContent.Header.getHeaderTitle(line)
                        val level = DocumentContent.Header.getHeaderLevel(line)
                        if (title.isBlank() || level == null) return@forEach

                        nodes.add(
                            NodeContent.Header(
                                title = title,
                                level = NodeContent.HeaderLevel.forNumber(level)
                            )
                        )
                    }

                    DocumentContent.Image.isImage(line) -> {
                        DocumentContent.Image.parseImage(line, images)?.also { nodes.add(it) }
                    }

                    DocumentContent.Separator.isSeparator(line) -> {
                        nodes.add(NodeContent.Separator)
                    }

                    else -> {
                        if (DocumentContent.Paragraph.clearMarkers(line).containsVisibleText()) {
                            nodes.add(DocumentContent.Paragraph.parseFormatting(line))
                        }
                    }
                }
            }
        }

        return nodes
    }

    /**
     * Parses document, applies formatting and returns a list of [NodeContent].
     *
     * @param document Unedited [org.jsoup.nodes.Document].
     * @param images Optional map of [ImageMetadata.originalName] and
     * [ImageMetadata] to insert images inside returned list.
     */
    fun parseDocument(
        document: Document,
        images: Map<String, ImageMetadata>
    ): List<NodeContent> {
        val documentBody = (document.selectFirst("body") ?: document.body())

        // Apply fixes and markers to the document
        documentBody.processDocument(images = images)

        // Convert marked text to NodeContent
        val nodes = documentBody.wholeText().lines().processMarkedDocument(images = images)

        return nodes
    }

    /**
     * Extracts <binary> images from document.
     * Applicable to .fb2 (they embed such binaries inside documents).
     *
     * @return A list of [BinaryImage], which is a wrapper for name + byteArray.
     *         (empty if none found)
     */
    fun extractBinaryImages(document: Document): List<BinaryImage> {
        val images = mutableListOf<BinaryImage>()

        document.select("binary").forEach { element ->
            if (!element.attr("content-type").startsWith("image", ignoreCase = true)) return@forEach

            val name = URLDecoder.decode(element.attr("id"), StandardCharsets.UTF_8.name())
            if (name.isBlank()) return@forEach

            val base64Data = element.text().filterNot { character ->
                character == ' ' || character == '\t' || character == '\r' || character == '\n'
            }
            val imageBytes = Base64.decode(base64Data)

            images.add(BinaryImage(name, imageBytes))
        }

        return images
    }
}