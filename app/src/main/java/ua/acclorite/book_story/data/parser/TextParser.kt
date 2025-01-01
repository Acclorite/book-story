package ua.acclorite.book_story.data.parser

import ua.acclorite.book_story.domain.reader.ReaderText
import java.io.File

interface TextParser {
    suspend fun parse(file: File): List<ReaderText>
}