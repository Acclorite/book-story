package ua.acclorite.book_story.data.parser

import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.util.CoverImage
import java.io.File


interface FileParser {

    suspend fun parse(file: File): Pair<Book, CoverImage?>?
}