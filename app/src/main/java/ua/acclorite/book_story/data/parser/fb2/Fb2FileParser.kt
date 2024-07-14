package ua.acclorite.book_story.data.parser.fb2

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.w3c.dom.Document
import org.w3c.dom.Element
import ua.acclorite.book_story.R
import ua.acclorite.book_story.data.parser.FileParser
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.model.Category
import ua.acclorite.book_story.domain.util.CoverImage
import ua.acclorite.book_story.domain.util.UIText
import java.io.File
import javax.inject.Inject
import javax.xml.parsers.DocumentBuilderFactory

class Fb2FileParser @Inject constructor() : FileParser {

    override suspend fun parse(file: File): Pair<Book, CoverImage?>? {
        if (!file.name.endsWith(".fb2", true) || !file.exists()) {
            return null
        }

        try {
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val document = withContext(Dispatchers.IO) {
                builder.parse(file)
            }

            val titleFromFile = extractElementContent(document, "book-title")
            val title = titleFromFile ?: file.name.dropLast(4).trim()

            val authorFirstName = extractElementContent(document, "first-name")
            val authorLastName = extractElementContent(document, "last-name")
            val authorFromFile = StringBuilder()

            if (authorFirstName != null) {
                authorFromFile.append(
                    "$authorFirstName "
                )
            }
            if (authorLastName != null) {
                authorFromFile.append(
                    authorLastName
                )
            }

            val author = if (authorFromFile.isNotBlank()) {
                UIText.StringValue(authorFromFile.toString().trim())
            } else {
                UIText.StringResource(R.string.unknown_author)
            }

            val descriptionFromFile = extractElementContent(document, "annotation")

            return Book(
                title = title,
                author = author,
                description = descriptionFromFile,
                textPath = "",
                scrollIndex = 0,
                scrollOffset = 0,
                progress = 0f,
                filePath = file.path,
                lastOpened = null,
                category = Category.entries[0],
                coverImage = null
            ) to null
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun extractElementContent(document: Document, tagName: String): String? {
        val nodeList = document.getElementsByTagName(tagName)
        if (nodeList.length > 0) {
            val element = nodeList.item(0) as Element
            return element.textContent.trim()
        }
        return null
    }
}