/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.domain.file

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import com.anggrayudi.storage.file.DocumentFileCompat
import com.anggrayudi.storage.file.getAbsolutePath

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
            val storageId = DocumentFileCompat.getStorageId(context, path)
            if (storageId.isBlank()) throw NullPointerException("Could not get storageId.")

            val basePath = DocumentFileCompat.getBasePath(context, path)
            if (basePath.isBlank()) throw NullPointerException("Could not get basePath.")

            val parentUri = context.contentResolver.persistedUriPermissions.find {
                try {
                    val persistedUri = DocumentFileCompat.fromUri(context, it.uri)
                    val persistedUriPath = persistedUri?.getAbsolutePath(context)

                    return@find !(persistedUri == null ||
                            !persistedUri.canRead() ||
                            persistedUriPath.isNullOrBlank() ||
                            !path.startsWith(persistedUriPath, ignoreCase = true))
                } catch (e: Exception) {
                    e.printStackTrace()
                    return@find false
                }
            }?.uri
            if (parentUri == null) throw NullPointerException("Could not get parentUri.")

            DocumentsContract.buildDocumentUriUsingTree(parentUri, "$storageId:$basePath")
        } catch (e: Exception) {
            e.printStackTrace()
            return null
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