package ua.acclorite.book_story.data.parser.txt

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.acclorite.book_story.R
import ua.acclorite.book_story.data.parser.TextParser
import ua.acclorite.book_story.domain.model.StringWithId
import ua.acclorite.book_story.util.Resource
import ua.acclorite.book_story.util.UIText
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import javax.inject.Inject

class TxtTextParser @Inject constructor() : TextParser {

    override suspend fun parse(file: File): Resource<List<StringWithId>> {
        if (!file.name.endsWith(".txt")) {
            return Resource.Error(UIText.StringResource(R.string.error_wrong_file_format))
        }

        return try {
            val formattedLines = mutableListOf<StringWithId>()

            withContext(Dispatchers.IO) {
                BufferedReader(FileReader(file)).forEachLine { line ->
                    if (line.isNotBlank()) {
                        formattedLines.add(
                            StringWithId(line)
                        )
                    }
                }
            }

            if (formattedLines.isEmpty()) {
                return Resource.Error(UIText.StringResource(R.string.error_file_empty))
            }

            Resource.Success(formattedLines)
        } catch (e: Exception) {
            Resource.Error(
                UIText.StringResource(
                    R.string.error_query,
                    e.message?.take(40)?.trim() ?: ""
                )
            )
        }
    }
}