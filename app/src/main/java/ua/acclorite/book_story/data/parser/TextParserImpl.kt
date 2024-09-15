package ua.acclorite.book_story.data.parser

import android.util.Log
import ua.acclorite.book_story.R
import ua.acclorite.book_story.data.parser.epub.EpubTextParser
import ua.acclorite.book_story.data.parser.fb2.Fb2TextParser
import ua.acclorite.book_story.data.parser.htm.HtmTextParser
import ua.acclorite.book_story.data.parser.html.HtmlTextParser
import ua.acclorite.book_story.data.parser.pdf.PdfTextParser
import ua.acclorite.book_story.data.parser.txt.TxtTextParser
import ua.acclorite.book_story.domain.model.ChapterWithText
import ua.acclorite.book_story.domain.util.Resource
import ua.acclorite.book_story.domain.util.UIText
import java.io.File
import javax.inject.Inject

private const val TEXT_PARSER = "Text Parser"

class TextParserImpl @Inject constructor(
    private val txtTextParser: TxtTextParser,
    private val pdfTextParser: PdfTextParser,
    private val epubTextParser: EpubTextParser,
    private val fb2TextParser: Fb2TextParser,
    private val htmlTextParser: HtmlTextParser,
    private val htmTextParser: HtmTextParser,
) : TextParser {
    override suspend fun parse(file: File): Resource<List<ChapterWithText>> {
        if (!file.exists()) {
            Log.e(TEXT_PARSER, "File does not exist.")
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
                epubTextParser.parse(file)
            }

            ".html" -> {
                htmlTextParser.parse(file)
            }

            ".htm" -> {
                htmTextParser.parse(file)
            }

            else -> {
                Log.e(TEXT_PARSER, "Wrong file format, could not find supported extension.")
                Resource.Error(UIText.StringResource(R.string.error_wrong_file_format))
            }
        }
    }
}








