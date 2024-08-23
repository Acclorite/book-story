package ua.acclorite.book_story.data.parser

import ua.acclorite.book_story.R
import ua.acclorite.book_story.data.parser.fb2.Fb2TextParser
import ua.acclorite.book_story.data.parser.htm.HtmTextParser
import ua.acclorite.book_story.data.parser.html.HtmlTextParser
import ua.acclorite.book_story.data.parser.pdf.PdfTextParser
import ua.acclorite.book_story.data.parser.txt.TxtTextParser
import ua.acclorite.book_story.data.parser.zip.ZipTextParser
import ua.acclorite.book_story.domain.util.Resource
import ua.acclorite.book_story.domain.util.UIText
import java.io.File
import javax.inject.Inject

class TextParserImpl @Inject constructor(
    private val txtTextParser: TxtTextParser,
    private val pdfTextParser: PdfTextParser,
    private val epubTextParser: HtmlTextParser,
    private val fb2TextParser: Fb2TextParser,
    private val zipTextParser: ZipTextParser,
    private val htmlTextParser: HtmlTextParser,
    private val htmTextParser: HtmTextParser,
) : TextParser {
    override suspend fun parse(file: File): Resource<List<String>> {
        if (!file.exists()) {
            return Resource.Error(
                UIText.StringResource(R.string.error_something_went_wrong_with_file)
            )
        }

        val fileFormat = ".${file.extension}".lowercase().trim()
        return when (fileFormat) {
            ".pdf" -> {
                pdfTextParser.parse(file)
            }

            ".epub" -> {
                epubTextParser.parse(file)
            }

            ".txt" -> {
                txtTextParser.parse(file)
            }

            ".fb2" -> {
                fb2TextParser.parse(file)
            }

            ".zip" -> {
                zipTextParser.parse(file)
            }

            ".html" -> {
                htmlTextParser.parse(file)
            }

            ".htm" -> {
                htmTextParser.parse(file)
            }

            else -> Resource.Error(UIText.StringResource(R.string.error_wrong_file_format))
        }
    }
}








