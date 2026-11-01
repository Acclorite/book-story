/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.parser.content.document

import io.mockk.every
import io.mockk.just
import io.mockk.Runs
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ua.acclorite.book_story.core.log.logW
import ua.acclorite.book_story.domain.model.book.NodeContent
import ua.acclorite.book_story.domain.model.file.FilePath
import ua.acclorite.book_story.domain.model.file.ImageMetadata

class JsoupDocumentParserTest {
    private val parser = JsoupDocumentParser()

    @Before
    fun stubAndroidLogging() {
        mockkStatic("ua.acclorite.book_story.core.log.LogKt")
        every { logW(any(), any()) } just Runs
    }

    @After
    fun restoreLogging() {
        unmockkStatic("ua.acclorite.book_story.core.log.LogKt")
    }

    private fun parse(html: String): List<NodeContent> = runBlocking {
        parser.parseDocument(Jsoup.parse(html), emptyMap())
    }

    private fun List<NodeContent>.texts() = filterIsInstance<NodeContent.TextBlock>().map { it.text }

    @Test
    fun plainBookAndBookWithStylesBothLoad() {
        assertEquals(listOf("Hello world"), parse("<p>Hello world</p>").texts())
        val nodes = parse("<style>p {font-weight: bold}</style><p>Hello world</p>")
        assertEquals(listOf("Hello world"), nodes.texts())
        assertEquals(NodeContent.TextStyle.Bold, (nodes.single() as NodeContent.TextBlock).styles.single().style)
    }

    @Test
    fun nestedInlineFormattingUsesValidOffsetsAndPreservesLiteralBraces() {
        val paragraph = parse("<p> A <b>bold <i>both</i></b> {{literal}} </p>").single() as NodeContent.Paragraph
        assertEquals("A bold both {{literal}}", paragraph.text)
        assertEquals("bold both", paragraph.styles.single { it.style == NodeContent.TextStyle.Bold }.let {
            paragraph.text.substring(it.start, it.end)
        })
        assertEquals("both", paragraph.styles.single { it.style == NodeContent.TextStyle.Italic }.let {
            paragraph.text.substring(it.start, it.end)
        })
        assertTrue(paragraph.styles.all { it.start >= 0 && it.end <= paragraph.text.length && it.start < it.end })
    }

    @Test
    fun newInlineStylesAndExternalLinksAreRetained() {
        val paragraph = parse("""
            <p><u>u</u><del>d</del><sup>s</sup><sub>b</sub><code>c</code><small>z</small><mark>m</mark>
            <a href="https://example.org">link</a></p>
        """).single() as NodeContent.TextBlock
        val styles = paragraph.styles.map { it.style }.toSet()
        assertTrue(styles.containsAll(listOf(
            NodeContent.TextStyle.Underline, NodeContent.TextStyle.Strikethrough,
            NodeContent.TextStyle.Superscript, NodeContent.TextStyle.Subscript,
            NodeContent.TextStyle.Monospace, NodeContent.TextStyle.Small, NodeContent.TextStyle.Highlight,
            NodeContent.TextStyle.Link("https://example.org")
        )))
    }

    @Test
    fun headersRemoveStyleTagsAndMarkers() {
        val header = parse("<h2><b>Chapter</b><br>One {{BOLD}}</h2>").single() as NodeContent.Header
        assertEquals("Chapter One", header.title)
        assertEquals(NodeContent.HeaderLevel.H2, header.level)
    }

    @Test
    fun headingImagesAreRetainedAndHiddenImagesRemainHidden() = runBlocking {
        val metadata = ImageMetadata("pic", FilePath("cached"), 10, 20, 0.5f)
        val nodes = parser.parseDocument(
            Jsoup.parse("""<h1><span><img src="pic.png"></span><span hidden><img src="pic.png"></span>Title</h1><p>tail</p>"""),
            mapOf("pic.png" to metadata)
        )
        assertEquals(NodeContent.Header("Title", NodeContent.HeaderLevel.H1), nodes.first())
        assertEquals(1, nodes.filterIsInstance<NodeContent.Image>().size)
        assertEquals(listOf("tail"), nodes.texts())
        val imageOnly = parser.parseDocument(Jsoup.parse("<h1><img src='pic.png'></h1>"), mapOf("pic.png" to metadata))
        assertTrue(imageOnly.single() is NodeContent.Image)
    }

    @Test
    fun nestedQuotesPreserveDepthAndFollowingParagraphs() {
        val nodes = parse("<blockquote><p>outer</p><blockquote><p>inner</p></blockquote><p>end</p></blockquote><p>plain</p>")
        assertEquals(listOf("outer", "inner", "end", "plain"), nodes.texts())
        assertEquals(listOf(1, 2, 1), nodes.filterIsInstance<NodeContent.Quote>().map { it.depth })
        assertTrue(nodes.last() is NodeContent.Paragraph)
    }

    @Test
    fun preformattedCodeKeepsIndentationAndLineBreaks() {
        val block = parse("<pre>  a\n    b &lt; c\n</pre>").single() as NodeContent.CodeBlock
        assertEquals("  a\n    b < c\n", block.text)
    }

    @Test
    fun listsKeepNumberingNestedDepthAndFollowingText() {
        val nodes = parse("<ol start='3'><li>one<ul><li>nested</li></ul></li><li value='8'>two</li></ol><p>tail</p>")
        assertEquals(listOf("3. one", "• nested", "8. two", "tail"), nodes.texts())
        assertEquals(listOf(1, 2, 1), nodes.filterIsInstance<NodeContent.ListItem>().map { it.depth })
        assertTrue(nodes.last() is NodeContent.Paragraph)
    }

    @Test
    fun tableCellsAndParagraphsDoNotRunTogetherOrLoseRowIdentity() {
        val nodes = parse("<table><tr><td><p>A</p><p>B</p></td><td>C</td></tr></table><p>tail</p>")
        assertEquals("A / B | C", (nodes.first() as NodeContent.TableRow).text)
        assertTrue(nodes.last() is NodeContent.Paragraph)
    }

    @Test
    fun cssHiddenTextIsExcludedAndNormalOverridesInheritedBold() {
        val paragraph = parse("""
            <style>.hide {display:none}</style><p><b>bold<span style="font-weight:normal">plain</span></b>
            <span class="hide">hidden</span></p>
        """).single() as NodeContent.TextBlock
        assertFalse(paragraph.text.contains("hidden"))
        assertEquals("bold", paragraph.styles.single { it.style == NodeContent.TextStyle.Bold }.let {
            paragraph.text.substring(it.start, it.end)
        })
    }

    @Test
    fun fictionBookBodiesPoemsQuotesAndSubtitlesKeepTheirSemantics() = runBlocking {
        val document = Jsoup.parse("""
            <FictionBook><description><title-info><book-title>Metadata</book-title></title-info></description>
            <body><section><title><p>Chapter</p></title><subtitle>Subtitle</subtitle>
            <cite><p>Quote</p></cite><poem><title><p>Poem</p></title><stanza><v>  verse</v></stanza></poem>
            </section></body><body name="notes"><section><title>Notes</title><p>Note</p></section></body></FictionBook>
        """, "", Parser.xmlParser())
        val nodes = parser.parseDocument(document, emptyMap())
        assertEquals(listOf("Chapter", "Notes"), nodes.filterIsInstance<NodeContent.Header>().map { it.title })
        assertEquals(listOf("Subtitle", "Poem"), nodes.filterIsInstance<NodeContent.Subtitle>().map { it.text })
        assertEquals("Quote", nodes.filterIsInstance<NodeContent.Quote>().single().text)
        assertEquals("  verse", nodes.filterIsInstance<NodeContent.Verse>().single().text)
        assertEquals("Note", nodes.filterIsInstance<NodeContent.Paragraph>().single().text)
    }

    @Test
    fun navigationPromotesExistingHeadingWithoutDuplicatingIt() = runBlocking {
        val nodes = parser.parseDocument(
            Jsoup.parse("<p id='chapter'>Chapter</p><p>Body</p>"), emptyMap(),
            outline = listOf(DocumentOutlineEntry("chapter", "Chapter", 2))
        )
        assertEquals(2, nodes.size)
        assertEquals(NodeContent.Header("Chapter", NodeContent.HeaderLevel.H2), nodes.first())
    }

    @Test
    fun namespacedImageReferencesResolveAndUnknownImagesKeepAltText() = runBlocking {
        val metadata = ImageMetadata("pic", FilePath("cached"), 10, 20, 0.5f)
        val document = Jsoup.parse("""
            <FictionBook xmlns:l="http://www.w3.org/1999/xlink"><body><image l:href="#pic"/>
            <p>After</p><image l:href="#missing" alt="Missing image"/></body></FictionBook>
        """, "", Parser.xmlParser())
        val nodes = parser.parseDocument(document, mapOf("pic" to metadata))
        assertTrue(nodes.first() is NodeContent.Image)
        assertEquals(listOf("After", "Missing image"), nodes.texts())
    }

    @Test
    fun malformedOptionalBinaryDoesNotHideValidImages() {
        val document = Jsoup.parse("""
            <FictionBook><binary id="broken" content-type="image/png">?</binary>
            <binary id="valid" content-type="image/png">aG Vs
            bG8=</binary></FictionBook>
        """, "", Parser.xmlParser())
        val images = parser.extractBinaryImages(document)
        assertEquals("valid", images.single().name)
        assertEquals("hello", images.single().bytes.decodeToString())
    }
}
