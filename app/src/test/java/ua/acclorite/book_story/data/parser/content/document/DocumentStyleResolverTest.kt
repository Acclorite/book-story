/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.parser.content.document

import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DocumentStyleResolverTest {
    @Test
    fun embeddedRulesResolveLiteralBracesAndSelectorSpecificity() {
        val document = Jsoup.parse("""
            <style>p { font-weight: normal } .emphasis { font-weight: bold }
            #target { font-style: italic }</style>
            <p id="target" class="emphasis">Text</p>
        """)
        val styles = DocumentStyleResolver(document, emptyList()).resolve(document.getElementById("target")!!)
        assertEquals("bold", styles["font-weight"])
        assertEquals("italic", styles["font-style"])
    }

    @Test
    fun documentWithoutStylesStillLoads() {
        val document = Jsoup.parse("<p>Text</p>")
        assertTrue(DocumentStyleResolver(document, emptyList()).resolve(document.selectFirst("p")!!).isEmpty())
    }

    @Test
    fun importantRulesOverrideNormalInlineDeclarations() {
        val document = Jsoup.parse("""<p style="font-weight: normal; font-style: normal !important">Text</p>""")
        val resolver = DocumentStyleResolver(document, listOf("p { font-weight: bold !important; font-style: italic !important }"))
        assertEquals("bold", resolver.resolve(document.selectFirst("p")!!)["font-weight"])
        assertEquals("normal", resolver.resolve(document.selectFirst("p")!!)["font-style"])
    }

    @Test
    fun commentsAndNestedAtRulesDoNotLeakIntoUnconditionalRules() {
        val document = Jsoup.parse("<p>Text</p>")
        val resolver = DocumentStyleResolver(document, listOf("""
            /* ignore { font-weight: normal } */
            @charset "UTF-8";
            @media print { p { font-weight: normal } @supports (x: y) { p { font-style: normal } } }
            p { font-weight: bold; font-style: italic }
        """))
        val styles = resolver.resolve(document.selectFirst("p")!!)
        assertEquals("bold", styles["font-weight"])
        assertEquals("italic", styles["font-style"])
    }

    @Test
    fun malformedSelectorDoesNotPreventValidRulesFromResolving() {
        val document = Jsoup.parse("<p class='note'>Text</p>")
        val resolver = DocumentStyleResolver(document, listOf("p[ { font-weight: bold } .note { font-style: italic }"))
        assertEquals("italic", resolver.resolve(document.selectFirst("p")!!)["font-style"])
    }

    @Test
    fun selectorGroupsAndHiddenClassesResolve() {
        val document = Jsoup.parse("<p class='hidden'>Text</p><span>More</span>")
        val resolver = DocumentStyleResolver(document, listOf("p.hidden, span { display: none; text-decoration: underline }"))
        assertEquals("none", resolver.resolve(document.selectFirst("p")!!)["display"])
        assertEquals("underline", resolver.resolve(document.selectFirst("span")!!)["text-decoration"])
    }

    @Test
    fun fictionBookStylesheetIsReadWithoutTreatingNamedInlineStyleAsCss() {
        val document = Jsoup.parse("""
            <FictionBook><stylesheet type="text/css">p { font-weight: bold }</stylesheet>
            <body><p>Text <style name="note">inline text</style></p></body></FictionBook>
        """, "", Parser.xmlParser())
        val resolver = DocumentStyleResolver(document, emptyList())
        assertEquals("bold", resolver.resolve(document.selectFirst("p")!!)["font-weight"])
    }
}
