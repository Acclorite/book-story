/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.repository

import ua.acclorite.book_story.data.model.common.NullableBook
import ua.acclorite.book_story.domain.file.File

interface FileSystemRepository {

    suspend fun getFiles(
        query: String = ""
    ): List<File>

    suspend fun getBookFromFile(
        file: File
    ): NullableBook
}