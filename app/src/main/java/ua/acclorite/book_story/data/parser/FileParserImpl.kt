package ua.acclorite.book_story.data.parser

import ua.acclorite.book_story.data.parser.epub.EpubFileParser
import ua.acclorite.book_story.data.parser.fb2.Fb2FileParser
import ua.acclorite.book_story.data.parser.htm.HtmFileParser
import ua.acclorite.book_story.data.parser.html.HtmlFileParser
import ua.acclorite.book_story.data.parser.pdf.PdfFileParser
import ua.acclorite.book_story.data.parser.txt.TxtFileParser
import ua.acclorite.book_story.data.parser.zip.ZipFileParser
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.util.CoverImage
import java.io.File
import javax.inject.Inject

class FileParserImpl @Inject constructor(
    private val txtFileParser: TxtFileParser,
    private val pdfFileParser: PdfFileParser,
    private val epubFileParser: EpubFileParser,
    private val fb2FileParser: Fb2FileParser,
    private val zipFileParser: ZipFileParser,
    private val htmlFileParser: HtmlFileParser,
    private val htmFileParser: HtmFileParser,
) : FileParser {
    override suspend fun parse(file: File): Pair<Book, CoverImage?>? {
        if (!file.exists()) {
            return null
        }

        val fileFormat = ".${file.extension}".lowercase().trim()
        return when (fileFormat) {
            ".pdf" -> {
                pdfFileParser.parse(file)
            }

            ".epub" -> {
                epubFileParser.parse(file)
            }

            ".txt" -> {
                txtFileParser.parse(file)
            }

            ".fb2" -> {
                fb2FileParser.parse(file)
            }

            ".zip" -> {
                zipFileParser.parse(file)
            }

            ".html" -> {
                htmlFileParser.parse(file)
            }

            ".htm" -> {
                htmFileParser.parse(file)
            }

            else -> null
        }
    }
}








