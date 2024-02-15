package com.acclorite.books_history.data.parser

import android.app.Application
import com.acclorite.books_history.domain.model.StringWithId
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import java.io.File
import javax.inject.Inject

class PdfTextParser @Inject constructor(private val application: Application) : TextParser {

    override suspend fun parse(file: File): List<StringWithId> {
        if (!file.name.endsWith(".pdf")) {
            return emptyList()
        }

        try {
            PDFBoxResourceLoader.init(application)

            val document = PDDocument.load(file)
            val stringWithIds = mutableListOf<StringWithId>()

            val pdfStripper = PDFTextStripper()
            pdfStripper.paragraphStart = "_new_line_"

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
                .filter { it.isNotEmpty() }

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

            return stringWithIds

        } catch (e: Exception) {
            return emptyList()
        }
    }
}
















