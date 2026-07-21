/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.parser.document

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import org.commonmark.node.Code
import org.commonmark.node.Emphasis
import org.commonmark.node.Heading
import org.commonmark.node.Link
import org.commonmark.node.Node
import org.commonmark.node.StrongEmphasis
import org.commonmark.node.Text
import org.commonmark.parser.Parser
import ua.acclorite.book_story.core.helpers.clearMarkdown
import javax.inject.Inject

class MarkdownParser @Inject constructor(
    private val commonmarkParser: Parser
) {
    /**
     * Parses markdown text to [AnnotatedString].
     *
     * @return Parsed annotated string.
     */
    fun parse(markdown: String): AnnotatedString {
        return try {
            val annotatedString = buildAnnotatedString {
                parseNode(commonmarkParser.parse(markdown))
            }.ifBlank { buildAnnotatedString { append(markdown) } }
                .trim() as AnnotatedString

            annotatedString
        } catch (e: Exception) {
            e.printStackTrace()
            buildAnnotatedString { append(markdown) }
        }
    }

    /**
     * Parses [Node].
     * Appends text and applies styles to the target [AnnotatedString.Builder].
     */
    private fun AnnotatedString.Builder.parseNode(node: Node) {
        when (node) {
            is Heading, is StrongEmphasis -> {
                withStyle(SpanStyle(fontWeight = FontWeight.Medium)) {
                    parseChildren(node)
                }
            }

            is Emphasis -> {
                withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                    parseChildren(node)
                }
            }

            is Code -> {
                withStyle(SpanStyle(fontFamily = FontFamily.Monospace)) {
                    append(node.literal)
                }
            }

            is Link -> {
                withLink(
                    LinkAnnotation.Url(
                        node.destination,
                        styles = TextLinkStyles(style = SpanStyle(textDecoration = TextDecoration.Underline))
                    )
                ) {
                    parseChildren(node)
                    append(" (${node.destination})")
                }
            }

            is Text -> {
                appendStrikethrough(node.literal.clearMarkdown())
                parseChildren(node)
            }

            else -> {
                parseChildren(node)
            }
        }
    }

    /**
     * Appends [text], turning any run wrapped in [STRIKETHROUGH_MARK] into a
     * strike-through span. Each mark toggles the state, so the closing style
     * composes with whatever emphasis the surrounding nodes already applied.
     */
    private fun AnnotatedString.Builder.appendStrikethrough(text: String) {
        if (!text.contains(STRIKETHROUGH_MARK)) {
            append(text)
            return
        }
        var struck = false
        text.split(STRIKETHROUGH_MARK).forEachIndexed { index, segment ->
            if (index > 0) struck = !struck
            if (segment.isEmpty()) return@forEachIndexed
            if (struck) {
                withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                    append(segment)
                }
            } else {
                append(segment)
            }
        }
    }

    private fun AnnotatedString.Builder.parseChildren(node: Node) {
        var child = node.firstChild
        while (child != null) {
            parseNode(child)
            child = child.next
        }
    }
}