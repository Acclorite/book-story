package ua.acclorite.book_story.data.parser

import ua.acclorite.book_story.data.parser.epub.EpubFileParser
import ua.acclorite.book_story.data.parser.pdf.PdfFileParser
import ua.acclorite.book_story.data.parser.txt.TxtFileParser
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.util.CoverImage
import java.io.File
import javax.inject.Inject

class FileParserImpl @Inject constructor(
    private val txtFileParser: TxtFileParser,
    private val pdfFileParser: PdfFileParser,
    private val epubFileParser: EpubFileParser
) : FileParser {
    override suspend fun parse(file: File): Pair<Book, CoverImage?>? {
        val fileFormat = ".${file.name.substringAfterLast(".")}"

        if (fileFormat == ".pdf") {
            return pdfFileParser.parse(file)
        }

        if (fileFormat == ".epub") {
            return epubFileParser.parse(file)
        }

        if (fileFormat == ".txt") {
            return txtFileParser.parse(file)
        }

        return null
    }
}








