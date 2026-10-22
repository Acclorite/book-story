/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.mapper.file

import android.app.Application
import androidx.core.net.toUri
import ua.acclorite.book_story.data.storage.file.CachedFile
import ua.acclorite.book_story.data.storage.file.CachedFileCompat
import ua.acclorite.book_story.domain.model.file.File
import javax.inject.Inject

class FileMapperImpl @Inject constructor(
    private val application: Application
) : FileMapper {
    override fun toCachedFile(file: File): CachedFile {
        if (file.name.isBlank()
            || file.path.isBlank()
            || file.uri.isBlank()
        ) {
            throw IllegalStateException("Incorrect File instance")
        }

        return CachedFileCompat.fromUri(
            context = application,
            uri = file.uri.toUri(),
            builder = CachedFileCompat.build(
                name = file.name,
                path = file.path,
                size = file.size,
                lastModified = file.lastModified,
                isDirectory = file.isDirectory
            )
        )
    }

    override fun toFile(cachedFile: CachedFile): File {
        if (cachedFile.name.isBlank()
            || cachedFile.path.isBlank()
            || cachedFile.uri.toString().isBlank()
        ) {
            throw IllegalStateException("Incorrect CachedFile instance")
        }

        return File(
            name = cachedFile.name,
            uri = cachedFile.uri.toString(),
            path = cachedFile.path,
            size = cachedFile.size,
            lastModified = cachedFile.lastModified,
            isDirectory = cachedFile.isDirectory
        )
    }
}