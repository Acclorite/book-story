package ua.acclorite.book_story.data.mapper.book

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import ua.acclorite.book_story.R
import ua.acclorite.book_story.data.local.dto.BookEntity
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.util.UIText
import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject

class BookMapperImpl @Inject constructor() : BookMapper {
    override suspend fun toBookEntity(book: Book): BookEntity {
        val legacyAPI = Build.VERSION.SDK_INT < Build.VERSION_CODES.R

        val stream = ByteArrayOutputStream()
        book.coverImage?.asAndroidBitmap()?.compress(
            if (legacyAPI) Bitmap.CompressFormat.WEBP
            else Bitmap.CompressFormat.WEBP_LOSSY,
            0,
            stream
        )

        val textAsString = book.text.joinToString("\n") {
            it.line.trim()
        }

        return BookEntity(
            id = book.id,
            title = book.title,
            filePath = book.filePath,
            progress = book.progress,
            author = book.author.string,
            text = textAsString,
            description = book.description,
            image = stream.toByteArray(),
            category = book.category
        )
    }

    override suspend fun toBook(bookEntity: BookEntity): Book {
        val file = File(bookEntity.filePath)
        var image: ImageBitmap? = null

        if (bookEntity.image.isNotEmpty()) {
            image = BitmapFactory
                .decodeByteArray(bookEntity.image, 0, bookEntity.image.size)
                .asImageBitmap()
            image.prepareToDraw()
        }

        return Book(
            id = bookEntity.id,
            title = bookEntity.title,
            author = bookEntity.author?.let { UIText.StringValue(it) } ?: UIText.StringResource(
                R.string.unknown_author
            ),
            description = bookEntity.description,
            progress = bookEntity.progress,
            file = if (file.exists()) file else null,
            text = emptyList(),
            filePath = bookEntity.filePath,
            lastOpened = null,
            category = bookEntity.category,
            coverImage = image
        )
    }
}