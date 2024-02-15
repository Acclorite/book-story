package com.acclorite.books_history.data.parser

import com.acclorite.books_history.domain.model.StringWithId
import java.io.File

interface TextParser {
    suspend fun parse(file: File): List<StringWithId>
}