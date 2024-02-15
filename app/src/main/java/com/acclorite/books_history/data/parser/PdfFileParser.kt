package com.acclorite.books_history.data.parser

import android.app.Application
import com.acclorite.books_history.R
import com.acclorite.books_history.domain.model.Book
import com.acclorite.books_history.domain.model.Category
import com.acclorite.books_history.domain.model.StringWithId
import com.acclorite.books_history.util.UIText
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import java.io.File
import javax.inject.Inject

class PdfFileParser @Inject constructor(private val application: Application) : FileParser {

    override suspend fun parse(file: File): Book? {
        if (!file.name.endsWith(".pdf")) {
            return null
        }

        try {
            PDFBoxResourceLoader.init(application)

            val document = PDDocument.load(file)

            val title = document.documentInformation.title ?: file.name.dropLast(4).trim()
            val fileAuthor = document.documentInformation.author
            val author = if (fileAuthor != null) UIText.StringValue(fileAuthor)
            else UIText.StringResource(R.string.unknown_author)

            val text = emptyList<StringWithId>()
            val description = document.documentInformation.subject ?: null

            document.close()

            return Book(
                id = null,
                title = title,
                author = author,
                description = description,
                text = text,
                progress = 0f,
                file = file,
                lastOpened = null,
                category = Category.entries[0],
                coverImage = null
            )
        } catch (e: Exception) {
            return null
        }
    }
}