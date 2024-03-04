package ua.acclorite.book_story.data.parser

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.acclorite.book_story.domain.model.StringWithId
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import javax.inject.Inject

class TxtTextParser @Inject constructor() : TextParser {

    override suspend fun parse(file: File): List<StringWithId> {
        if (!file.name.endsWith(".txt")) {
            return emptyList()
        }

        return try {
            val formattedLines = mutableListOf<StringWithId>()

            withContext(Dispatchers.IO) {
                BufferedReader(FileReader(file)).forEachLine { line ->
                    formattedLines.add(
                        StringWithId(line)
                    )
                }
            }

            formattedLines
        } catch (e: Exception) {
            emptyList()
        }
    }
}