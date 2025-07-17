/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.repository

import ua.acclorite.book_story.data.model.common.BookWithCover
import ua.acclorite.book_story.domain.model.file.File

interface FileSystemRepository {
    suspend fun searchFiles(
        query: String = ""
    ): Result<List<File>>

    suspend fun getBookFromFile(
        file: File
    ): Result<BookWithCover>
}