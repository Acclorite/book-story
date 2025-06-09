/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.model.file

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract

object CachedFileCompat {
    fun fromUri(context: Context, uri: Uri, builder: CachedFileBuilder? = null): CachedFile {
        return CachedFile(
            context = context,
            uri = when {
                DocumentsContract.isDocumentUri(context, uri) -> uri

                DocumentsContract.isTreeUri(uri) -> {
                    DocumentsContract.buildDocumentUriUsingTree(
                        uri,
                        DocumentsContract.getTreeDocumentId(uri)
                    )
                }

                else -> uri
            },
            builder = builder
        )
    }

    fun build(
        name: String? = null,
        path: String? = null,
        size: Long? = null,
        lastModified: Long? = null,
        isDirectory: Boolean? = null
    ): CachedFileBuilder {
        return CachedFileBuilder(
            name = name,
            path = path,
            size = size,
            lastModified = lastModified,
            isDirectory = isDirectory
        )
    }
}