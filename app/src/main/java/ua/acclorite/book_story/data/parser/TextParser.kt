package ua.acclorite.book_story.data.parser

import ua.acclorite.book_story.domain.model.ChapterWithText
import ua.acclorite.book_story.domain.util.Resource
import java.io.File

interface TextParser {
    suspend fun parse(file: File): Resource<List<ChapterWithText>>
}