package ua.acclorite.book_story.domain.repository

import ua.acclorite.book_story.domain.browse.SelectableFile
import ua.acclorite.book_story.domain.library.book.NullableBook
import java.io.File

interface FileSystemRepository {

    suspend fun getFilesFromDevice(
        query: String = ""
    ): List<SelectableFile>

    suspend fun getBookFromFile(
        file: File
    ): NullableBook
}