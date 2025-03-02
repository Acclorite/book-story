/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.file

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import com.anggrayudi.storage.file.DocumentFileCompat

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

    fun fromFullPath(
        context: Context,
        path: String,
        builder: CachedFileBuilder? = null
    ): CachedFile? {
        val uri = try {
            val fullPathUri = DocumentFileCompat.fromFullPath(context, path)?.uri
            if (fullPathUri == null) throw NullPointerException("Could not get URI from full path.")

            fullPathUri
        } catch (e: Exception) {
            e.printStackTrace()

            try {
                val parentUri = DocumentFileCompat.getAccessibleRootDocumentFile(
                    context = context,
                    fullPath = path
                )?.uri
                if (parentUri == null) throw NullPointerException("Could not get parent URI.")

                val storageId = DocumentFileCompat.getStorageId(context, path)
                if (storageId.isBlank()) throw NullPointerException("Could not get storageId.")

                val basePath = DocumentFileCompat.getBasePath(context, path)
                if (basePath.isBlank()) throw NullPointerException("Could not get basePath.")

                val documentUri = DocumentsContract.buildDocumentUriUsingTree(
                    parentUri,
                    "$storageId:$basePath"
                )
                if (documentUri == null) throw NullPointerException("Could not get document URI.")

                documentUri
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }

        val cachedFile = CachedFile(
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

        if (!cachedFile.canAccess()) return null
        return cachedFile
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