/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.parser.content

import io.mockk.every
import io.mockk.just
import io.mockk.Runs
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.core.log.logI
import ua.acclorite.book_story.core.log.logW
import ua.acclorite.book_story.data.parser.content.document.JsoupDocumentParser
import ua.acclorite.book_story.data.parser.content.document.readEpubNavigation
import ua.acclorite.book_story.data.storage.file.CachedFile
import ua.acclorite.book_story.domain.model.book.NodeContent
import ua.acclorite.book_story.domain.model.file.FilePath
import ua.acclorite.book_story.domain.model.file.ImageMetadata

class ContentParserRegressionTest {
    @get:Rule
    val temporaryFolder = TemporaryFolder()
    private val documentParser = JsoupDocumentParser()

    @Before
    fun stubAndroidLogging() {
        mockkStatic("ua.acclorite.book_story.core.log.LogKt")
        every { logI(any(), any()) } just Runs
        every { logW(any(), any()) } just Runs
        every { logE(any(), any()) } just Runs
    }

    @After
    fun restoreLogging() {
        unmockkStatic("ua.acclorite.book_story.core.log.LogKt")
    }

    private fun cachedFile(name: String, bytes: ByteArray = byteArrayOf(), archive: File? = null): CachedFile =
        mockk<CachedFile> {
            every { this@mockk.name } returns name
            every { openInputStream() } answers { bytes.inputStream() }
            every { rawFile } returns archive
        }

    private fun parse(parser: ContentParser, file: CachedFile): List<NodeContent> = runBlocking {
        val chunks = ConcurrentHashMap<FilePath, List<NodeContent>>()
        val count = AtomicInteger()
        val states = parser.parse(
            file,
            cacheImage = { name, _ -> ImageMetadata(name, FilePath("cached/" + name), 10, 20, 0.5f) },
            cacheNodes = { nodes ->
                if (nodes.isEmpty()) null
                else FilePath("chunk-" + count.incrementAndGet()).also { chunks[it] = nodes }
            }
        ).toList()
        val result = states.last()
        assertTrue("Expected success, received $result", result is ContentParserState.Success)
        (result as ContentParserState.Success).nodePaths.flatMap { chunks.getValue(it) }
    }

    private fun archive(vararg entries: Pair<String, String>): File {
        val file = temporaryFolder.newFile()
        ZipOutputStream(file.outputStream()).use { output ->
            entries.forEach { (path, text) ->
                output.putNextEntry(ZipEntry(path))
                output.write(text.toByteArray())
                output.closeEntry()
            }
        }
        return file
    }

    @Test
    fun htmlWithCssAndMalformedOptionalImageStillParses() {
        val nodes = parse(HtmlContentParser(documentParser), cachedFile("book.html", """
            <style>p { font-weight: bold }</style><h2>Chapter {{BOLD}}</h2>
            <p>Text <i>italic</i></p><img src="data:image/png;base64,?" alt="Image unavailable">
        """.toByteArray()))
        assertEquals("Chapter", (nodes.first() as NodeContent.Header).title)
        assertTrue(nodes.filterIsInstance<NodeContent.TextBlock>().any { it.text == "Image unavailable" })
        assertTrue(nodes.filterIsInstance<NodeContent.TextBlock>().first().styles.any { it.style == NodeContent.TextStyle.Bold })
    }

    @Test
    fun fictionBookParsesXmlEncodingAndAllBodies() {
        val text = """
            <?xml version="1.0" encoding="UTF-16"?>
            <FictionBook><body><section><title><p>Розділ</p></title><p>Текст</p></section></body>
            <body name="notes"><p>Примітка</p></body></FictionBook>
        """.trimIndent().trim()
        val nodes = parse(XmlContentParser(documentParser), cachedFile("book.fb2", text.toByteArray(Charsets.UTF_16)))
        assertEquals("Розділ", (nodes.first() as NodeContent.Header).title)
        assertEquals(listOf("Текст", "Примітка"), nodes.filterIsInstance<NodeContent.TextBlock>().map { it.text })
    }

    @Test
    fun txtAcceptsUtf8AndUtf16BomsAndKeepsNumbersAndPunctuation() {
        listOf(Charsets.UTF_8, Charsets.UTF_16LE, Charsets.UTF_16BE).forEach { charset ->
            val bom = when (charset) {
                Charsets.UTF_8 -> byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte())
                Charsets.UTF_16LE -> byteArrayOf(0xFF.toByte(), 0xFE.toByte())
                else -> byteArrayOf(0xFE.toByte(), 0xFF.toByte())
            }
            val nodes = parse(TxtContentParser(), cachedFile("book.txt", bom + "Текст\r\n123\n***\n\n".toByteArray(charset)))
            assertEquals(listOf("Текст", "123", "***"), nodes.filterIsInstance<NodeContent.TextBlock>().map { it.text })
        }
    }

    @Test
    fun epubUsesContainerSpineNavigationRelativeCssAndImages() {
        val file = archive(
            "META-INF/container.xml" to """<container><rootfiles><rootfile full-path="OEBPS/package.opf" media-type="application/oebps-package+xml"/></rootfiles></container>""",
            "OEBPS/package.opf" to """
                <package><manifest>
                <item id="second" href="Text/chapter2.xhtml" media-type="application/xhtml+xml"/>
                <item id="first" href="Text/chapter%20one.xhtml" media-type="application/xhtml+xml"/>
                <item id="nav" href="nav.xhtml" properties="nav" media-type="application/xhtml+xml"/>
                </manifest><spine><itemref idref="first"/><itemref idref="second"/></spine></package>
            """,
            "OEBPS/nav.xhtml" to """
                <nav epub:type="toc" xmlns:epub="http://www.idpf.org/2007/ops"><ol>
                <li><a href="Text/chapter%20one.xhtml#one">First</a><ol>
                <li><a href="Text/chapter2.xhtml#two">Second</a></li></ol></li></ol></nav>
            """,
            "OEBPS/Styles/book.css" to "p {font-weight:bold}",
            "OEBPS/Images/pic.png" to "fake image bytes handled by cache callback",
            "OEBPS/Text/chapter one.xhtml" to """
                <link rel="stylesheet" href="../Styles/book.css"><h3 id="one">First</h3>
                <p>First body</p><img src="../Images/pic.png">
            """,
            "OEBPS/Text/chapter2.xhtml" to """<h3 id="two">Second</h3><p>Second body</p>"""
        )
        val nodes = parse(EpubContentParser(documentParser), cachedFile("book.epub", archive = file))
        val headers = nodes.filterIsInstance<NodeContent.Header>()
        assertEquals(listOf("First", "Second"), headers.map { it.title })
        assertEquals(listOf(1, 2), headers.map { it.level.level })
        assertEquals(listOf("First body", "Second body"), nodes.filterIsInstance<NodeContent.TextBlock>().map { it.text })
        assertTrue(nodes.filterIsInstance<NodeContent.TextBlock>().first().styles.any { it.style == NodeContent.TextStyle.Bold })
        assertEquals("cached/OEBPS/Images/pic.png", nodes.filterIsInstance<NodeContent.Image>().single().content)
    }

    @Test
    fun epub2NcxResolvesNestedChapters() {
        val file = archive(
            "book.opf" to """<package><manifest><item id="ncx" href="toc.ncx" media-type="application/x-dtbncx+xml"/></manifest><spine toc="ncx"/></package>""",
            "toc.ncx" to """<ncx><navMap><navPoint><navLabel><text>One</text></navLabel><content src="one.xhtml#one"/>
                <navPoint><navLabel><text>Two</text></navLabel><content src="one.xhtml#two"/></navPoint>
                </navPoint></navMap></ncx>"""
        )
        ZipFile(file).use { zip ->
            val entries = readEpubNavigation(zip, zip.getEntry("book.opf")).getValue("one.xhtml")
            assertEquals(listOf("One", "Two"), entries.map { it.title })
            assertEquals(listOf(1, 2), entries.map { it.level })
            assertEquals(listOf("one", "two"), entries.map { it.fragment })
        }
    }
}
