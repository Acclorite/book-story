/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.model.file

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import androidx.compose.runtime.Immutable
import com.anggrayudi.storage.file.DocumentFileCompat
import com.anggrayudi.storage.file.getAbsolutePath
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

/**
 * Cached File.
 * Faster than [androidx.documentfile.provider.DocumentFile].
 * Saves all it's variables after initialized.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
@Immutable
class CachedFile(
    private val context: Context,
    val uri: Uri,
    private val builder: CachedFileBuilder? = null
) {
    @Immutable
    private data class QueryParams(
        val name: String,
        val size: Long,
        val lastModified: Long,
        val isDirectory: Boolean
    )

    private val queryParams by lazy {
        getFileQueryParams()
    }
    val path: String by lazy { builder?.path ?: getFilePath() }
    val rawFile: File? by lazy { storeInCache() }

    val name: String get() = builder?.name ?: queryParams.name
    val size: Long get() = builder?.size ?: queryParams.size
    val lastModified: Long get() = builder?.lastModified ?: queryParams.lastModified
    val isDirectory: Boolean get() = builder?.isDirectory ?: queryParams.isDirectory

    fun canAccess(): Boolean {
        return try {
            context.contentResolver.query(uri, null, null, null, null)?.let {
                it.close()
                return true
            }
            throw Exception("Could not access URI: $uri")
        } catch (e: Exception) {
            false
        }
    }

    fun openInputStream(): InputStream? {
        return try {
            context.contentResolver.openInputStream(uri)
                ?: throw Exception("Failed to open InputStream for URI: $uri")
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun listFiles(forEach: ((CachedFile) -> Unit)? = null): List<CachedFile> {
        if (!isDirectory || !canAccess()) return emptyList()

        val cachedFiles = mutableListOf<CachedFile>()

        val nameColumn = DocumentsContract.Document.COLUMN_DISPLAY_NAME
        val uriColumn = DocumentsContract.Document.COLUMN_DOCUMENT_ID
        val sizeColumn = DocumentsContract.Document.COLUMN_SIZE
        val lastModifiedColumn = DocumentsContract.Document.COLUMN_LAST_MODIFIED
        val isDirectoryColumn = DocumentsContract.Document.COLUMN_MIME_TYPE

        val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(
            uri,
            DocumentsContract.getDocumentId(uri)
        )

        context.contentResolver.query(
            childrenUri,
            arrayOf(
                nameColumn,
                uriColumn,
                sizeColumn,
                lastModifiedColumn,
                isDirectoryColumn
            ),
            null, null, null
        )?.use { cursor ->
            if (cursor.count == 0) {
                return emptyList()
            }

            try {
                val nameIndex = cursor.getColumnIndexOrThrow(nameColumn)
                val uriIndex = cursor.getColumnIndexOrThrow(uriColumn)
                val sizeIndex = cursor.getColumnIndexOrThrow(sizeColumn)
                val lastModifiedIndex = cursor.getColumnIndexOrThrow(lastModifiedColumn)
                val isDirectoryIndex = cursor.getColumnIndexOrThrow(isDirectoryColumn)

                while (cursor.moveToNext()) {
                    val nameQuery = cursor.getString(nameIndex)
                    val pathQuery = "$path/$nameQuery"
                    val uriQuery = DocumentsContract.buildDocumentUriUsingTree(
                        uri,
                        cursor.getString(uriIndex)
                    )
                    val sizeQuery = cursor.getLong(sizeIndex)
                    val lastModifiedQuery = cursor.getLong(lastModifiedIndex)
                    val isDirectoryQuery = cursor.getString(isDirectoryIndex) ==
                            DocumentsContract.Document.MIME_TYPE_DIR

                    val queryFile = CachedFileCompat.fromUri(
                        context = context,
                        uri = uriQuery,
                        builder = CachedFileCompat.build(
                            name = nameQuery,
                            path = pathQuery,
                            size = sizeQuery,
                            lastModified = lastModifiedQuery,
                            isDirectory = isDirectoryQuery
                        )
                    )

                    forEach?.invoke(queryFile)
                    cachedFiles.add(queryFile)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return cachedFiles
    }

    fun walk(
        includeDirectories: Boolean = false,
        forEach: ((CachedFile) -> Unit)? = null
    ): List<CachedFile> {
        val cachedFiles = mutableListOf<CachedFile>()

        listFiles {
            when (it.isDirectory) {
                false -> {
                    forEach?.invoke(it)
                    cachedFiles.add(it)
                }

                true -> {
                    if (includeDirectories) cachedFiles.add(it)
                    cachedFiles.addAll(it.walk(includeDirectories, forEach))
                }
            }
        }

        return cachedFiles
    }

    /**
     * Copies the file to [context].cacheDir and provides [File].
     *
     * @return null if the file is directory or failed
     */
    private fun storeInCache(): File? {
        if (isDirectory) return null

        val cacheDir = context.cacheDir
        val fileName = path.replace("_", "-").replace("/", "_").take(80) + "_${path.length}"
        val cacheFile = File(cacheDir, fileName)
        val tempFile = File(cacheDir, "$fileName.tmp")

        try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                BufferedOutputStream(FileOutputStream(tempFile)).use { output ->
                    input.copyTo(output)
                }
            } ?: throw IllegalStateException("Failed to open InputStream.")

            if (!tempFile.renameTo(cacheFile)) {
                throw IllegalStateException("Failed to rename .tmp file.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            tempFile.delete()
            return null
        }

        return cacheFile
    }

    private fun getFileQueryParams(): QueryParams {
        val nameColumn = DocumentsContract.Document.COLUMN_DISPLAY_NAME
        val sizeColumn = DocumentsContract.Document.COLUMN_SIZE
        val lastModifiedColumn = DocumentsContract.Document.COLUMN_LAST_MODIFIED
        val isDirectoryColumn = DocumentsContract.Document.COLUMN_MIME_TYPE

        val projection = mutableListOf<String>().apply {
            if (builder?.name == null) add(nameColumn)
            if (builder?.size == null) add(sizeColumn)
            if (builder?.lastModified == null) add(lastModifiedColumn)
            if (builder?.isDirectory == null) add(isDirectoryColumn)
        }

        if (projection.isEmpty() && builder != null) {
            return QueryParams(
                name = builder.name!!,
                size = builder.size!!,
                lastModified = builder.lastModified!!,
                isDirectory = builder.isDirectory!!
            )
        }

        context.contentResolver.query(
            uri,
            projection.toTypedArray(),
            null, null, null
        )?.use { cursor ->
            try {
                if (cursor.moveToFirst()) {
                    val queryResult = mutableMapOf<String, Any?>()

                    projection.forEach { column ->
                        when (column) {
                            nameColumn -> {
                                if (builder?.name == null) {
                                    queryResult[column] = cursor.getString(
                                        cursor.getColumnIndexOrThrow(column)
                                    )
                                }
                            }

                            sizeColumn -> {
                                if (builder?.size == null) {
                                    queryResult[column] = cursor.getLong(
                                        cursor.getColumnIndexOrThrow(column)
                                    )
                                }
                            }

                            lastModifiedColumn -> {
                                if (builder?.lastModified == null) {
                                    queryResult[column] = cursor.getLong(
                                        cursor.getColumnIndexOrThrow(column)
                                    )
                                }
                            }

                            isDirectoryColumn -> {
                                if (builder?.isDirectory == null) {
                                    queryResult[column] = cursor.getString(
                                        cursor.getColumnIndexOrThrow(column)
                                    )
                                }
                            }
                        }
                    }

                    val nameQuery = queryResult.getOrElse(nameColumn) {
                        builder?.name
                    } as String

                    val sizeQuery = queryResult.getOrElse(sizeColumn) {
                        builder?.size
                    } as Long

                    val lastModifiedQuery = queryResult.getOrElse(lastModifiedColumn) {
                        builder?.lastModified
                    } as Long

                    val isDirectoryQuery = when (queryResult[isDirectoryColumn]) {
                        DocumentsContract.Document.MIME_TYPE_DIR -> true
                        null -> builder?.isDirectory!!
                        else -> false
                    }

                    return QueryParams(
                        name = nameQuery,
                        size = sizeQuery,
                        lastModified = lastModifiedQuery,
                        isDirectory = isDirectoryQuery
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return QueryParams(
            name = "unknown_${UUID.randomUUID()}",
            size = 0,
            lastModified = 0,
            isDirectory = false
        )
    }

    private fun getFilePath(): String {
        val tempFile = DocumentFileCompat.fromUri(context, uri)
        return tempFile?.getAbsolutePath(context)?.trimEnd('/') ?: ""
    }
}