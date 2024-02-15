package com.acclorite.books_history.data.parser

import com.acclorite.books_history.domain.model.Book
import java.io.File

interface FileParser {

    suspend fun parse(file: File): Book?
}