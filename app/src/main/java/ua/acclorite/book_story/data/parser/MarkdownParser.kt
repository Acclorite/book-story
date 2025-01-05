package ua.acclorite.book_story.data.parser

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import org.commonmark.node.Emphasis
import org.commonmark.node.Heading
import org.commonmark.node.Link
import org.commonmark.node.Node
import org.commonmark.node.StrongEmphasis
import org.commonmark.node.Text
import org.commonmark.parser.Parser
import ua.acclorite.book_story.presentation.core.util.clearMarkdown
import javax.inject.Inject

class MarkdownParser @Inject constructor(
    private val commonmarkParser: Parser
) {
    /**
     * Parses markdown text to [androidx.compose.ui.text.AnnotatedString].
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
                append(node.literal.clearMarkdown())
                parseChildren(node)
            }

            else -> {
                parseChildren(node)
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