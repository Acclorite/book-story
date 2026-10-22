/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.service

import android.app.Application
import ua.acclorite.book_story.core.data.FileData
import ua.acclorite.book_story.data.storage.file.CachedFile
import ua.acclorite.book_story.data.storage.file.CachedFileCompat
import ua.acclorite.book_story.domain.model.book.Book
import ua.acclorite.book_story.domain.model.file.FilePath
import ua.acclorite.book_story.domain.service.FileHandler
import ua.acclorite.book_story.domain.service.FileHash
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.security.DigestInputStream
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject

private const val HASH_ALGORITHM = "SHA-256"

class FileHandlerImpl @Inject constructor(
    private val application: Application
) : FileHandler {

    /**
     * Walks through the storage and searches for specific file path.
     * Fixes issues with getting files from URI.
     */
    override fun getFileFromBook(book: Book): Result<CachedFile> = runCatching {
        application.contentResolver.persistedUriPermissions.forEach { storage ->
            val storageFile = CachedFileCompat.fromUri(
                application,
                storage.uri
            )

            if (!storageFile.isDirectory) return@forEach
            if (!book.filePath.startsWith(storageFile.path, ignoreCase = true)) return@forEach

            storageFile.walk().forEach { file ->
                if (book.filePath.equals(file.path, ignoreCase = true)) {
                    return@runCatching file
                }
            }
        }

        throw NoSuchElementException("Could not find file from book")
    }

    /**
     * Gets file has from book for further usage. Calls [getFileFromBook] first to get file.
     *
     * @throws IllegalStateException if the file is empty or could not open input stream.
     */
    override fun getFileHashFromBook(book: Book): Result<FileHash> {
        return getFileFromBook(book).mapCatching { file ->
            if (file.size == 0L) throw IllegalStateException("File is empty")

            val digest = MessageDigest.getInstance(HASH_ALGORITHM)

            file.openInputStream()?.use { inputStream ->
                DigestInputStream(inputStream, digest).use { dis ->
                    val buffer = ByteArray(8192)
                    while (true) {
                        if (dis.read(buffer) == -1) break
                    }
                }
            } ?: throw IllegalStateException("Input Stream is null")

            val bytes = digest.digest()
            val hash = bytes.joinToString("") { "%02x".format(it) }
            FileHash(hash)
        }
    }

    override fun getParserFromBook(book: Book): Result<String> = runCatching {
        book.filePath.substringAfterLast(".")
    }

    /**
     * Gets all storage files with granted Uri permissions.
     * Filters out all similar paths, such as:
     * * /Downloads
     * * /Downloads/books <- this one is filtered out
     */
    override fun getStorageFiles(): Result<List<CachedFile>> = runCatching {
        application.contentResolver.persistedUriPermissions.mapNotNull { permission ->
            val storage = CachedFileCompat.fromUri(
                application,
                permission.uri
            )
            if (!storage.isDirectory) return@mapNotNull null

            storage
        }.let { storages ->
            storages.filter { storage ->
                storages.none {
                    it.path != storage.path && storage.path.startsWith(
                        it.path,
                        ignoreCase = true
                    )
                }
            }
        }
    }

    /**
     * Parent function to save a random file to cache.
     * Gives lambda with [OutputStream].
     */
    override fun saveCustomToCacheWithRandomName(
        save: (OutputStream) -> Unit,
        extension: String,
        directory: String
    ): Result<FilePath> = runCatching {
        val cacheDir = FileData.getCacheDirectory(application).resolve(directory)
        if (!cacheDir.exists()) cacheDir.mkdirs()

        val randomName = "${UUID.randomUUID()}.$extension"
        val randomFile = File(cacheDir, randomName)

        randomFile.outputStream().buffered().use { outputStream ->
            save(outputStream)
        }

        return@runCatching FilePath(randomFile.absolutePath)
    }

    /**
     * Saves [InputStream] to cache as random file
     * (with given extension and in given subdirectory).
     */
    override fun saveStreamToCacheWithRandomName(
        inputStream: InputStream,
        extension: String,
        directory: String
    ): Result<FilePath> = saveCustomToCacheWithRandomName(
        save = { inputStream.copyTo(it) },
        extension = extension,
        directory = directory
    )

    override fun moveFile(
        fromPath: FilePath,
        toPath: FilePath
    ): Result<FilePath> = runCatching {
        val fromFile = File(fromPath.path)
        if (!fromFile.exists()) error("From file does not exist")

        val toFile = File(toPath.path)
        val toDirectory = toFile.parentFile ?: throw IOException("Could not get parent directory")
        if (!toDirectory.exists()) toDirectory.mkdirs()

        if (!fromFile.renameTo(toFile)) throw IOException("Could not move file")
        FilePath(toFile.absolutePath)
    }

    override fun deleteDirectory(directory: FilePath): Result<Unit> = runCatching {
        File(directory.path).let {
            if (it.exists()) it.deleteRecursively()
        }
    }
}