package ua.acclorite.book_story.data.parser.txt

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import ua.acclorite.book_story.R
import ua.acclorite.book_story.data.parser.TextParser
import ua.acclorite.book_story.domain.model.ChapterWithText
import ua.acclorite.book_story.domain.util.Resource
import ua.acclorite.book_story.domain.util.UIText
import ua.acclorite.book_story.presentation.core.constants.Constants
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import javax.inject.Inject

private const val TXT_TAG = "TXT Parser"

class TxtTextParser @Inject constructor() : TextParser {

    override suspend fun parse(file: File): Resource<List<ChapterWithText>> {
        Log.i(TXT_TAG, "Started TXT parsing: ${file.name}.")

        return try {
            val lines = mutableListOf<String>()

            withContext(Dispatchers.IO) {
                BufferedReader(FileReader(file)).forEachLine { line ->
                    if (line.isNotBlank()) {
                        lines.add(
                            line.trim()
                        )
                    }
                }
            }

            yield()

            if (lines.isEmpty()) {
                return Resource.Error(UIText.StringResource(R.string.error_file_empty))
            }

            Log.i(TXT_TAG, "Successfully finished TXT parsing.")
            Resource.Success(
                listOf(
                    ChapterWithText(
                        chapter = Constants.EMPTY_CHAPTER,
                        text = lines
                    )
                )
            )
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