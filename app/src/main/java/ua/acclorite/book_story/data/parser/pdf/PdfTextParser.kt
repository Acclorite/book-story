package ua.acclorite.book_story.data.parser.pdf

import android.app.Application
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import ua.acclorite.book_story.R
import ua.acclorite.book_story.data.parser.TextParser
import ua.acclorite.book_story.domain.model.StringWithId
import ua.acclorite.book_story.util.Resource
import ua.acclorite.book_story.util.UIText
import java.io.File
import javax.inject.Inject

class PdfTextParser @Inject constructor(private val application: Application) : TextParser {

    override suspend fun parse(file: File): Resource<List<StringWithId>> {
        if (!file.name.endsWith(".pdf")) {
            return Resource.Error(UIText.StringResource(R.string.error_wrong_file_format))
        }

        try {
            PDFBoxResourceLoader.init(application)

            val document = PDDocument.load(file)
            val stringWithIds = mutableListOf<StringWithId>()

            val pdfStripper = PDFTextStripper()
            pdfStripper.paragraphStart = "_new_line_"

            if (document.isEncrypted) {
                document.close()
                return Resource.Error(UIText.StringResource(R.string.error_file_encrypted))
            }

            val oldText = pdfStripper.getText(document)
                .replace("\r", "")

            document.close()

            val text = oldText.filterIndexed { index, c ->
                if (c == ' ') {
                    oldText[index - 1] != ' '
                } else {
                    true
                }
            }

            val unformattedLines = text.split("${pdfStripper.paragraphStart}|\\n".toRegex())
                .filter { it.isNotBlank() }

            val lines = mutableListOf<String>()
            unformattedLines.forEachIndexed { index, string ->
                if (string.trim().first().isLowerCase() && index > 0) {
                    lines[lines.lastIndex] += " ${string.trim()}"
                } else {
                    lines.add(string.trim())
                }
            }
            lines.forEach { line ->
                stringWithIds.add(StringWithId(line.trim()))
            }

            if (stringWithIds.isEmpty()) {
                return Resource.Error(UIText.StringResource(R.string.error_file_empty))
            }

            return Resource.Success(stringWithIds)
        } catch (e: Exception) {
            return Resource.Error(
                UIText.StringResource(
                    R.string.error_query,
                    e.message?.take(40)?.trim() ?: ""
                )
            )
        }
    }
}
















