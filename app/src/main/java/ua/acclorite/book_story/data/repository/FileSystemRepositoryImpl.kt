package ua.acclorite.book_story.data.repository

import android.os.Environment
import android.util.Log
import ua.acclorite.book_story.R
import ua.acclorite.book_story.data.local.room.BookDao
import ua.acclorite.book_story.data.mapper.book.BookMapper
import ua.acclorite.book_story.data.parser.FileParser
import ua.acclorite.book_story.domain.browse.SelectableFile
import ua.acclorite.book_story.domain.library.book.NullableBook
import ua.acclorite.book_story.domain.library.book.NullableBook.NotNull
import ua.acclorite.book_story.domain.library.book.NullableBook.Null
import ua.acclorite.book_story.domain.repository.FileSystemRepository
import ua.acclorite.book_story.domain.ui.UIText
import ua.acclorite.book_story.presentation.core.constants.Constants
import ua.acclorite.book_story.presentation.core.constants.provideExtensions
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

private const val GET_BOOK_FROM_FILE = "BOOK FROM FILE, REPO"
private const val GET_FILES_FROM_DEVICE = "FILES FROM DEVICE, REPO"

/**
 * File System repository.
 * Manages all File System([java.io.File]) related work.
 */
@Singleton
class FileSystemRepositoryImpl @Inject constructor(
    private val database: BookDao,
    private val bookMapper: BookMapper,
    private val fileParser: FileParser
) : FileSystemRepository {

    /**
     * Get all matching files from device.
     * Filters by [query] and sorts out not supported file formats and already added files.
     */
    override suspend fun getFilesFromDevice(query: String): List<SelectableFile> {
        Log.i(GET_FILES_FROM_DEVICE, "Getting files from device by query: \"$query\".")

        val existingBooks = database
            .searchBooks("")
            .map { bookMapper.toBook(it) }
        val supportedExtensions = Constants.provideExtensions()

        fun File.isValid(): Boolean {
            if (!exists()) {
                return false
            }

            val isFileSupported = supportedExtensions.any { ext ->
                name.endsWith(
                    ext,
                    ignoreCase = true
                )
            }

            if (!isFileSupported) {
                return false
            }

            val isFileNotAdded = existingBooks.all {
                it.filePath.lowercase().trim() != path.lowercase().trim()
            }

            if (!isFileNotAdded) {
                return false
            }

            val isQuery = if (query.isEmpty()) true else name.trim().lowercase()
                .contains(query.trim().lowercase())

            return isQuery
        }

        suspend fun File.getAllFiles(): List<SelectableFile> {
            val filesList = mutableListOf<SelectableFile>()

            val files = listFiles()
            if (files != null) {
                for (file in files) {
                    if (!file.exists()) {
                        continue
                    }

                    when {
                        file.isFile -> {
                            if (file.isValid()) {
                                filesList.add(
                                    SelectableFile(
                                        fileOrDirectory = file,
                                        parentDirectory = this,
                                        isDirectory = false,
                                        isFavorite = false,
                                        isSelected = false
                                    )
                                )
                            }
                        }

                        file.isDirectory -> {
                            val subDirectoryFiles = file.getAllFiles()
                            if (subDirectoryFiles.isNotEmpty()) {
                                filesList.add(
                                    SelectableFile(
                                        fileOrDirectory = file,
                                        parentDirectory = this,
                                        isDirectory = true,
                                        isFavorite = database.favoriteDirectoryExits(file.path),
                                        isSelected = false
                                    )
                                )
                                filesList.addAll(subDirectoryFiles)
                            }
                        }
                    }
                }
            }

            return filesList
        }

        val rootDirectory = Environment.getExternalStorageDirectory()
        if (
            !rootDirectory.exists() ||
            !rootDirectory.isDirectory ||
            (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED &&
                    Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED_READ_ONLY)
        ) {
            Log.e(GET_FILES_FROM_DEVICE, "Could not correctly get root directory.")
            return emptyList()
        }

        Log.i(GET_FILES_FROM_DEVICE, "Successfully got all matching files.")
        return rootDirectory.getAllFiles()
    }

    /**
     * Gets book from given file. If error happened, returns [NullableBook.Null].
     */
    override suspend fun getBookFromFile(file: File): NullableBook {
        val parsedBook = fileParser.parse(file)
        if (parsedBook == null) {
            Log.e(GET_BOOK_FROM_FILE, "Parsed file(${file.name}) is null.")
            return Null(
                file.name,
                UIText.StringResource(R.string.error_something_went_wrong)
            )
        }

        Log.i(GET_BOOK_FROM_FILE, "Successfully got book from file.")
        return NotNull(bookWithCover = parsedBook)
    }
}