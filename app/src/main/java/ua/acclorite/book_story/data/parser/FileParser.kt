package ua.acclorite.book_story.data.parser

import ua.acclorite.book_story.domain.model.BookWithCover
import java.io.File


interface FileParser {

    suspend fun parse(file: File): BookWithCover?
}