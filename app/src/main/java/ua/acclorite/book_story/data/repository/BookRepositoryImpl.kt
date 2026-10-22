/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.repository

import android.app.Application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ua.acclorite.book_story.core.CoverImage
import ua.acclorite.book_story.core.data.FileData
import ua.acclorite.book_story.data.mapper.book.BookMapper
import ua.acclorite.book_story.data.mapper.chapter.ChapterMapper
import ua.acclorite.book_story.data.mapper.file.FileMapper
import ua.acclorite.book_story.data.mapper.metadata.MetadataMapper
import ua.acclorite.book_story.data.mapper.node.NodeMapper
import ua.acclorite.book_story.data.parser.content.ContentParser
import ua.acclorite.book_story.data.parser.content.ContentParserState
import ua.acclorite.book_story.data.parser.cover.CoverParser
import ua.acclorite.book_story.data.storage.room.AppDatabase
import ua.acclorite.book_story.data.storage.room.ContentDatabase
import ua.acclorite.book_story.domain.model.book.Book
import ua.acclorite.book_story.domain.model.book.Chapter
import ua.acclorite.book_story.domain.model.book.Metadata
import ua.acclorite.book_story.domain.model.book.Node
import ua.acclorite.book_story.domain.model.book.NodeId
import ua.acclorite.book_story.domain.model.file.File
import ua.acclorite.book_story.domain.model.file.FilePath
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.domain.service.FileHandler
import ua.acclorite.book_story.domain.service.ImageHandler
import ua.acclorite.book_story.domain.service.NodeHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepositoryImpl @Inject constructor(
    private val application: Application,
    private val appDatabase: AppDatabase,
    private val bookMapper: BookMapper,
    private val fileMapper: FileMapper,
    private val metadataMapper: MetadataMapper,
    private val nodeMapper: NodeMapper,
    private val chapterMapper: ChapterMapper,
    private val coverParser: CoverParser,
    private val contentParser: ContentParser,
    private val fileHandler: FileHandler,
    private val imageHandler: ImageHandler,
    private val nodeHandler: NodeHandler
) : BookRepository {

    private var contentDatabases = mutableMapOf<String, ContentDatabase>()

    private fun getContentDatabase(uuid: String): ContentDatabase {
        if (!contentDatabases.contains(uuid)) {
            contentDatabases[uuid] = ContentDatabase.buildDatabase(uuid, application)
        }
        return contentDatabases.getValue(uuid)
    }

    override suspend fun searchBooks(query: String): Result<List<Book>> = runCatching {
        withContext(Dispatchers.IO) {
            appDatabase.bookDao.searchBooks(query).map { bookMapper.toBook(it) }
        }
    }

    override suspend fun getBook(bookId: Int): Result<Book> = runCatching {
        withContext(Dispatchers.IO) {
            appDatabase.bookDao.findBookById(bookId).let {
                if (it == null) throw NoSuchElementException("Couldn't get book [$bookId]")
                else bookMapper.toBook(it)
            }
        }
    }

    override suspend fun getBookContentMetadata(book: Book): Result<Metadata> = runCatching {
        withContext(Dispatchers.IO) {
            getContentDatabase(book.uuid).metadataDao.getMetadata()?.let {
                metadataMapper.toMetadata(it)
            } ?: error("Could not get metadata from book")
        }
    }

    override suspend fun getBookContentChapters(book: Book): Result<List<Chapter>> = runCatching {
        withContext(Dispatchers.IO) {
            getContentDatabase(book.uuid).chapterDao.getChapters().map {
                chapterMapper.toChapter(it)
            }
        }
    }

    override suspend fun getBookContentNodesInRange(
        book: Book,
        nodeId: NodeId,
        range: Int
    ): Result<List<Node>> = runCatching {
        withContext(Dispatchers.IO) {
            getContentDatabase(book.uuid).nodeDao.getNodesInRange(nodeId.id, range).map {
                nodeMapper.toNode(it)
            }
        }
    }

    override suspend fun getBookContentNodesAfter(
        book: Book,
        nodeId: NodeId,
        limit: Int
    ): Result<List<Node>> = runCatching {
        withContext(Dispatchers.IO) {
            getContentDatabase(book.uuid).nodeDao.getNodesAfter(nodeId.id, limit).map {
                nodeMapper.toNode(it)
            }
        }
    }

    override suspend fun getBookContentNodesBefore(
        book: Book,
        nodeId: NodeId,
        limit: Int
    ): Result<List<Node>> = runCatching {
        withContext(Dispatchers.IO) {
            getContentDatabase(book.uuid).nodeDao.getNodesBefore(nodeId.id, limit).map {
                nodeMapper.toNode(it)
            }
        }
    }

    override suspend fun getBookContentNodeByOffset(
        book: Book,
        offset: Int
    ): Result<Node?> = runCatching {
        withContext(Dispatchers.IO) {
            getContentDatabase(book.uuid).nodeDao.getNodeByOffset(offset)?.let {
                nodeMapper.toNode(it)
            }
        }
    }

    override suspend fun isBookContentNodesEmpty(book: Book): Result<Boolean> = runCatching {
        withContext(Dispatchers.IO) {
            getContentDatabase(book.uuid).nodeDao.isEmpty()
        }
    }

    override suspend fun saveBookContentImage(
        book: Book,
        imagePath: FilePath,
        id: Int
    ): Result<FilePath> = runCatching {
        withContext(Dispatchers.IO) {
            fileHandler.moveFile(
                imagePath, FilePath(
                    FileData.getBookContentImageFile(
                        bookUUID = book.uuid,
                        context = application,
                        id = id,
                        extension = imagePath.path.substringAfterLast(".")
                    )
                )
            ).getOrThrow()
        }
    }

    override suspend fun saveBookContentNodes(
        book: Book,
        nodes: List<Node>
    ): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            getContentDatabase(book.uuid).nodeDao.insertNodes(
                nodes.map { node -> nodeMapper.toNodeEntity(node) }
            )
        }
    }

    override suspend fun saveBookContentChapters(
        book: Book,
        chapters: List<Chapter>
    ): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            getContentDatabase(book.uuid).chapterDao.insertChapters(
                chapters.map { chapter -> chapterMapper.toChapterEntity(chapter) }
            )
        }
    }

    override suspend fun saveBookContentMetadata(
        book: Book,
        metadata: Metadata
    ): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            getContentDatabase(book.uuid).metadataDao.insertMetadata(
                metadataMapper.toMetadataEntity(metadata)
            )
        }
    }

    override suspend fun clearBookContentImages(book: Book) = runCatching {
        withContext(Dispatchers.IO) {
            fileHandler.deleteDirectory(
                FilePath(FileData.getBookContentImagesDirectory(book.uuid, application))
            ).getOrThrow()
        }
    }

    override suspend fun clearBookContent(book: Book): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            val contentDatabase = getContentDatabase(book.uuid)
            contentDatabase.metadataDao.clear()
            contentDatabase.nodeDao.clear()
            contentDatabase.chapterDao.clear()
            clearBookContentImages(book).getOrThrow()
        }
    }

    override suspend fun parseBookContent(
        book: Book
    ): Result<Flow<ContentParserState>> = runCatching {
        withContext(Dispatchers.IO) {
            val tempDirectory = FileData.getTempDirectory()
            fileHandler.getFileFromBook(book).mapCatching { cachedFile ->
                contentParser.parse(
                    cachedFile = cachedFile,
                    cacheImage = { filename, inputStream ->
                        imageHandler.cacheImageAndGetMetadata(
                            filename,
                            inputStream,
                            tempDirectory
                        ).getOrNull()
                    },
                    cacheNodes = { nodes ->
                        nodeHandler.cacheNodes(
                            nodes,
                            tempDirectory
                        ).getOrNull()
                    }
                )
            }.getOrThrow()
        }
    }

    override suspend fun getFileFromBook(bookId: Int): Result<File> {
        return withContext(Dispatchers.IO) {
            getBook(bookId)
                .mapCatching { fileHandler.getFileFromBook(it).getOrThrow() }
                .mapCatching { fileMapper.toFile(it) }
        }
    }

    override suspend fun addBook(book: Book): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            appDatabase.bookDao.insertBook(bookMapper.toBookEntity(book))
        }
    }

    override suspend fun updateBook(book: Book): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            appDatabase.bookDao.updateBook(bookMapper.toBookEntity(book)).also {
                if (it == 0) throw Exception("Could not update book in database")
            }
        }
    }

    override suspend fun deleteBook(book: Book): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            appDatabase.bookDao.deleteBook(bookMapper.toBookEntity(book)).also {
                if (it == 0) throw Exception("Could not delete book in database")
            }
            fileHandler.deleteDirectory(
                FilePath(FileData.getBookContentDirectory(book.uuid, application))
            ).getOrThrow()
        }
    }

    override suspend fun getDefaultCover(book: Book): Result<CoverImage?> = runCatching {
        return withContext(Dispatchers.IO) {
            fileHandler.getFileFromBook(book).mapCatching {
                coverParser.parse(it)
            }
        }
    }
}