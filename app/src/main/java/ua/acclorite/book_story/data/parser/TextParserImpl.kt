package ua.acclorite.book_story.data.parser

import ua.acclorite.book_story.R
import ua.acclorite.book_story.data.parser.epub.EpubTextParser
import ua.acclorite.book_story.data.parser.pdf.PdfTextParser
import ua.acclorite.book_story.data.parser.txt.TxtTextParser
import ua.acclorite.book_story.domain.model.StringWithId
import ua.acclorite.book_story.domain.util.Resource
import ua.acclorite.book_story.domain.util.UIText
import java.io.File
import javax.inject.Inject

class TextParserImpl @Inject constructor(
    private val txtTextParser: TxtTextParser,
    private val pdfTextParser: PdfTextParser,
    private val epubTextParser: EpubTextParser,
) : TextParser {
    override suspend fun parse(file: File): Resource<List<StringWithId>> {
        val fileFormat = ".${file.name.substringAfterLast(".")}"

        if (fileFormat == ".pdf") {
            return pdfTextParser.parse(file)
        }

        if (fileFormat == ".epub") {
            return epubTextParser.parse(file)
        }

        if (fileFormat == ".txt") {
            return txtTextParser.parse(file)
        }

        return Resource.Error(UIText.StringResource(R.string.error_wrong_file_format))
    }
}








