/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.reader

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.unit.em
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import ua.acclorite.book_story.core.helpers.cleanHeaderText
import ua.acclorite.book_story.domain.model.book.Node
import ua.acclorite.book_story.domain.model.book.NodeContent
import ua.acclorite.book_story.ui.common.components.common.getHighlightedText
import ua.acclorite.book_story.ui.reader.model.ReaderNode
import ua.acclorite.book_story.ui.reader.model.ReaderLinkUrl
import ua.acclorite.book_story.ui.reader.model.withLinkUrls

@Composable
fun rememberReaderNodes(
    nodes: List<Node>,
    paragraphTextStyle: TextStyle,
    boldFontThickness: Int,
    header1TextStyle: TextStyle,
    header2TextStyle: TextStyle,
    header3TextStyle: TextStyle,
    header4TextStyle: TextStyle,
    header5TextStyle: TextStyle,
    header6TextStyle: TextStyle,
    highlightedReading: Boolean,
    highlightedReadingThickness: Int,
    backgroundColor: Color,
    showLinkUrls: Boolean = false
): List<ReaderNode> {
    val density = LocalDensity.current
    val colors = MaterialTheme.colorScheme
    val textColor = paragraphTextStyle.color.takeOrElse { colors.onBackground }
    val textLuminance = textColor.luminance()
    val highlightColor = listOf(colors.primaryContainer, colors.primary).maxBy { candidate ->
        val backgroundLuminance = candidate.luminance()
        (maxOf(textLuminance, backgroundLuminance) + 0.05f) /
            (minOf(textLuminance, backgroundLuminance) + 0.05f)
    }
    val backgroundLuminance = backgroundColor.takeOrElse { colors.background }.luminance()
    val linkColor = listOf(colors.secondary, colors.secondaryContainer).maxBy { candidate ->
        val luminance = candidate.luminance()
        (maxOf(backgroundLuminance, luminance) + 0.05f) /
            (minOf(backgroundLuminance, luminance) + 0.05f)
    }
    val headerFontWeights = listOf(
        header1TextStyle, header2TextStyle, header3TextStyle,
        header4TextStyle, header5TextStyle, header6TextStyle
    ).map { it.fontWeight ?: FontWeight.Normal }
    val paragraphFontWeight = paragraphTextStyle.fontWeight ?: FontWeight.Normal

    return remember(
        nodes,
        density,
        highlightedReading,
        highlightedReadingThickness,
        paragraphFontWeight,
        highlightColor,
        linkColor,
        boldFontThickness,
        headerFontWeights,
        showLinkUrls
    ) {
        nodes.map { node ->
            when (val nodeContent = node.content) {
                is NodeContent.Header -> ReaderNode.Header(
                    originalNode = node,
                    title = getHighlightedText(
                        highlight = highlightedReading,
                        text = buildAnnotatedString { append(nodeContent.title.cleanHeaderText()) },
                        highlightThickness = headerFontWeights[nodeContent.level.level - 1],
                        fontWeightIncrease = highlightedReadingThickness
                    ),
                    level = nodeContent.level
                )

                is NodeContent.TextBlock -> {
                    val blockFontWeight = nodeContent.textBlockStyle(paragraphTextStyle).fontWeight
                        ?: FontWeight.Normal
                    val boldFontWeight = FontWeight(
                        (blockFontWeight.weight + boldFontThickness).coerceAtMost(900)
                    )
                    val linkUrls = if (showLinkUrls) nodeContent.styles.mapNotNull { range ->
                        val link = range.style as? NodeContent.TextStyle.Link ?: return@mapNotNull null
                        if (range.start < 0 || range.end > nodeContent.text.length || range.start >= range.end) {
                            return@mapNotNull null
                        }
                        ReaderLinkUrl(range.start, range.end, link.link)
                    }.distinct().sortedBy { it.end } else emptyList()
                    ReaderNode.Paragraph(
                        originalNode = node,
                        linkUrls = linkUrls,
                        text = getHighlightedText(
                            highlight = highlightedReading && nodeContent !is NodeContent.CodeBlock,
                            text = buildAnnotatedString {
                                append(nodeContent.text)

                                nodeContent.styles.forEach { style ->
                                    when (style.style) {
                                        is NodeContent.TextStyle.Bold -> addStyle(
                                            SpanStyle(fontWeight = boldFontWeight),
                                            style.start,
                                            style.end
                                        )

                                        is NodeContent.TextStyle.Italic -> addStyle(
                                            SpanStyle(fontStyle = FontStyle.Italic),
                                            style.start,
                                            style.end
                                        )

                                        is NodeContent.TextStyle.Underline,
                                        is NodeContent.TextStyle.Strikethrough -> Unit
                                        is NodeContent.TextStyle.Superscript -> addStyle(
                                            SpanStyle(baselineShift = BaselineShift.Superscript, fontSize = 0.75.em),
                                            style.start, style.end
                                        )
                                        is NodeContent.TextStyle.Subscript -> addStyle(
                                            SpanStyle(baselineShift = BaselineShift.Subscript, fontSize = 0.75.em),
                                            style.start, style.end
                                        )
                                        is NodeContent.TextStyle.Monospace -> addStyle(
                                            SpanStyle(fontFamily = FontFamily.Monospace), style.start, style.end
                                        )
                                        is NodeContent.TextStyle.Small -> addStyle(
                                            SpanStyle(fontSize = 0.8.em), style.start, style.end
                                        )
                                        is NodeContent.TextStyle.Highlight -> addStyle(
                                            SpanStyle(background = highlightColor), style.start, style.end
                                        )
                                        is NodeContent.TextStyle.Link -> addLink(
                                            LinkAnnotation.Url(
                                                style.style.link,
                                                TextLinkStyles(SpanStyle(color = linkColor, textDecoration = TextDecoration.Underline))
                                            ),
                                            style.start,
                                            style.end
                                        )
                                    }
                                }
                                val decorations = nodeContent.styles.filter {
                                    it.style is NodeContent.TextStyle.Underline || it.style is NodeContent.TextStyle.Strikethrough
                                }
                                val boundaries = decorations.flatMap { listOf(it.start, it.end) }.distinct().sorted()
                                boundaries.zipWithNext { start, end ->
                                    val active = decorations.filter { it.start <= start && it.end >= end }
                                    val values = buildList {
                                        if (active.any { it.style is NodeContent.TextStyle.Underline }) add(TextDecoration.Underline)
                                        if (active.any { it.style is NodeContent.TextStyle.Strikethrough }) add(TextDecoration.LineThrough)
                                    }
                                    if (values.isNotEmpty()) addStyle(SpanStyle(textDecoration = TextDecoration.combine(values)), start, end)
                                }
                            }.withLinkUrls(linkUrls),
                            highlightThickness = blockFontWeight,
                            fontWeightIncrease = highlightedReadingThickness
                        )
                    )
                }

                is NodeContent.Image -> ReaderNode.Image(
                    originalNode = node,
                    content = nodeContent.content,
                    caption = nodeContent.caption,
                    originalWidth = with(density) { nodeContent.width.toDp() },
                    originalHeight = with(density) { nodeContent.height.toDp() },
                    aspectRatio = nodeContent.aspectRatio
                )

                is NodeContent.Separator -> ReaderNode.Separator(originalNode = node)
            }
        }
    }
}
