package ua.acclorite.book_story.data.mapper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import ua.acclorite.book_story.R
import ua.acclorite.book_story.data.local.dto.BookEntity
import ua.acclorite.book_story.data.parser.EpubFileParser
import ua.acclorite.book_story.data.parser.PdfFileParser
import ua.acclorite.book_story.data.parser.TxtFileParser
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.util.UIText
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject

class BookMapperImpl @Inject constructor(
    private val txtFileParser: TxtFileParser,
    private val pdfFileParser: PdfFileParser,
    private val epubFileParser: EpubFileParser
) : BookMapper {
    override suspend fun toBookEntity(book: Book): BookEntity {
        val stream = ByteArrayOutputStream()
        book.coverImage?.compress(Bitmap.CompressFormat.JPEG, 30, stream)

        return BookEntity(
            id = book.id,
            title = book.title,
            filePath = book.filePath,
            progress = book.progress,
            lastOpened = book.lastOpened,
            image = stream.toByteArray(),
            category = book.category
        )
    }

    override suspend fun toBook(bookEntity: BookEntity): Book {
        val file = File(bookEntity.filePath)
        val inputStream = ByteArrayInputStream(bookEntity.image)
        val image = BitmapFactory.decodeStream(inputStream)

        val parsedBook = if (file.name.endsWith(".txt")) {
            txtFileParser.parse(file)
        } else if (file.name.endsWith(".pdf")) {
            pdfFileParser.parse(file)
        } else if (file.name.endsWith(".epub")) {
            epubFileParser.parse(file)
        } else {
            null
        }

        return if (parsedBook != null)
            Book(
                id = bookEntity.id,
                title = bookEntity.title,
                author = parsedBook.author,
                description = parsedBook.description,
                text = emptyList(),
                progress = bookEntity.progress,
                file = file,
                filePath = bookEntity.filePath,
                lastOpened = bookEntity.lastOpened,
                category = bookEntity.category,
                coverImage = if (bookEntity.image.isNotEmpty()) image else null
            )
        else
            Book(
                id = bookEntity.id,
                title = bookEntity.title,
                author = UIText.StringResource(R.string.unknown_author),
                text = emptyList(),
                description = null,
                progress = bookEntity.progress,
                file = null,
                filePath = bookEntity.filePath,
                lastOpened = bookEntity.lastOpened,
                category = bookEntity.category,
                coverImage = if (bookEntity.image.isNotEmpty()) image else null
            )
    }
}