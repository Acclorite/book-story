package com.acclorite.books_history.data.parser

import com.acclorite.books_history.domain.model.StringWithId
import nl.siegmann.epublib.epub.EpubReader
import org.jsoup.Jsoup
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.nio.charset.Charset
import javax.inject.Inject

class EpubTextParser @Inject constructor() : TextParser {

    override suspend fun parse(file: File): List<StringWithId> {
        if (!file.name.endsWith(".epub")) {
            return emptyList()
        }

        try {
            val book = EpubReader().readEpub(FileInputStream(file))
            val unformattedText = StringBuilder()

            for (spineReference in book.spine.spineReferences) {
                val resource = spineReference.resource
                val inputStream = resource.inputStream
                val reader =
                    BufferedReader(InputStreamReader(inputStream, Charset.forName("UTF-8")))
                var line: String?

                while (reader.readLine().also { line = it } != null) {
                    unformattedText.append(line).append("\n")
                }
            }

            val stringWithIds = mutableListOf<StringWithId>()

            Jsoup.parse(unformattedText.toString()).wholeText().split("\n").forEach {
                if (it.isNotBlank()) {
                    stringWithIds.add(StringWithId(it.trim()))
                }
            }

            return stringWithIds
        } catch (e: Exception) {
            return emptyList()
        }
    }
}