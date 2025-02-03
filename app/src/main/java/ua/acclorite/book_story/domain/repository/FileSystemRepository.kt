/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

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