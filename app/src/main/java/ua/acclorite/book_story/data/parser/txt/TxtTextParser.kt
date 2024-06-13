package ua.acclorite.book_story.data.parser.txt

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.acclorite.book_story.R
import ua.acclorite.book_story.data.parser.TextParser
import ua.acclorite.book_story.domain.util.Resource
import ua.acclorite.book_story.domain.util.UIText
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import javax.inject.Inject

class TxtTextParser @Inject constructor() : TextParser {

    override suspend fun parse(file: File): Resource<List<String>> {
        if (!file.name.endsWith(".txt", true) || !file.exists()) {
            return Resource.Error(UIText.StringResource(R.string.error_wrong_file_format))
        }

        return try {
            val formattedLines = mutableListOf<String>()

            withContext(Dispatchers.IO) {
                BufferedReader(FileReader(file)).forEachLine { line ->
                    if (line.isNotBlank()) {
                        formattedLines.add(
                            line.trim()
                        )
                    }
                }
            }

            if (formattedLines.isEmpty()) {
                return Resource.Error(UIText.StringResource(R.string.error_file_empty))
            }

            Resource.Success(formattedLines)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                UIText.StringResource(
                    R.string.error_query,
                    e.message?.take(40)?.trim() ?: ""
                )
            )
        }
    }
}