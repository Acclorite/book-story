/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.reader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import ua.acclorite.book_story.domain.model.book.Node
import ua.acclorite.book_story.domain.model.book.NodeContent
import ua.acclorite.book_story.ui.common.components.common.getHighlightedText
import ua.acclorite.book_story.ui.reader.model.ReaderNode

@Composable
internal fun rememberReaderNodes(
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
    highlightedReadingThickness: Int
): List<ReaderNode> {
    val density = LocalDensity.current
    val headerFontWeights = listOf(
        header1TextStyle, header2TextStyle, header3TextStyle,
        header4TextStyle, header5TextStyle, header6TextStyle
    ).map { it.fontWeight ?: FontWeight.Normal }
    val paragraphFontWeight = paragraphTextStyle.fontWeight ?: FontWeight.Normal
    val boldFontWeight = FontWeight((paragraphFontWeight.weight + boldFontThickness).coerceAtMost(900))
    return remember(
        nodes,
        density,
        highlightedReading,
        highlightedReadingThickness,
        paragraphFontWeight,
        boldFontWeight,
        headerFontWeights
    ) {
        nodes.map { node ->
            when (val nodeContent = node.content) {
                is NodeContent.Header -> ReaderNode.Header(
                    originalNode = node,
                    title = getHighlightedText(
                        highlight = highlightedReading,
                        text = buildAnnotatedString { append(nodeContent.title) },
                        highlightThickness = headerFontWeights[nodeContent.level.level - 1],
                        fontWeightIncrease = highlightedReadingThickness
                    ),
                    level = nodeContent.level
                )

                is NodeContent.Paragraph -> ReaderNode.Paragraph(
                    originalNode = node,
                    text = getHighlightedText(
                        highlight = highlightedReading,
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

                                    is NodeContent.TextStyle.Link -> addLink(
                                        LinkAnnotation.Url(
                                            style.style.link,
                                            TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline))
                                        ),
                                        style.start,
                                        style.end
                                    )
                                }
                            }
                        },
                        highlightThickness = paragraphFontWeight,
                        fontWeightIncrease = highlightedReadingThickness
                    )
                )

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
