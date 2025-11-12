/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.file_system

import ua.acclorite.book_story.core.CoverImage
import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.core.log.logI
import ua.acclorite.book_story.domain.model.file.File
import ua.acclorite.book_story.domain.model.library.Book
import ua.acclorite.book_story.domain.repository.FileSystemRepository
import javax.inject.Inject

class GetBookFromFileUseCase @Inject constructor(
    private val fileSystemRepository: FileSystemRepository
) {

    suspend operator fun invoke(file: File): Pair<Book, CoverImage?>? {
        logI("Getting book from [${file.name}] file.")

        return fileSystemRepository.getBookFromFile(file).fold(
            onSuccess = {
                logI("Successfully got [${it.first.title}] book from file.")
                it
            },
            onFailure = {
                logE("Could not get book from file with error: ${it.message}")
                null
            }
        )
    }
}