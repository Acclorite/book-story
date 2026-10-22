/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.book

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import ua.acclorite.book_story.core.data.ParsersData
import ua.acclorite.book_story.core.log.logI
import ua.acclorite.book_story.core.log.logW
import ua.acclorite.book_story.domain.model.book.Book
import ua.acclorite.book_story.domain.model.book.BookProgress
import ua.acclorite.book_story.domain.model.book.Chapter
import ua.acclorite.book_story.domain.model.book.ChapterId
import ua.acclorite.book_story.domain.model.book.ChapterPosition
import ua.acclorite.book_story.domain.model.book.LinkId
import ua.acclorite.book_story.domain.model.book.Metadata
import ua.acclorite.book_story.domain.model.book.Node
import ua.acclorite.book_story.domain.model.book.NodeContent
import ua.acclorite.book_story.domain.model.book.NodeId
import ua.acclorite.book_story.domain.model.book.NodePosition
import ua.acclorite.book_story.domain.model.file.FilePath
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.domain.service.FileHandler
import ua.acclorite.book_story.domain.service.NodeHandler
import javax.inject.Inject

@Immutable
private data class ChapterHeader(
    val chapterId: ChapterId,
    val linkId: LinkId,
    val header: NodeContent.Header,
    val chapterOffset: Int,
    val chapterLength: Int
)

private const val TAG = "SaveBookContent"
private const val SAVE_CHUNK = 1000

class SaveBookContentUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val nodeHandler: NodeHandler,
    private val fileHandler: FileHandler
) {
    suspend operator fun invoke(book: Book, nodePaths: List<FilePath>): Result<Unit> = runCatching {
        logI(TAG, "Saving [${book.title}] with [${nodePaths.size}] node paths.")

        val chapterHeaders = mutableListOf<ChapterHeader>()
        val savedImages = mutableMapOf<FilePath, FilePath>()
        var totalLength = 0
        var index = 0
        var imageIndex = 1
        var chapterIndex = 0

        bookRepository.clearBookContent(book).getOrThrow()

        nodePaths.forEach { nodePath ->
            nodeHandler.getNodesFromFile(nodePath)
                .getOrThrow()
                .chunked(SAVE_CHUNK)
                .forEach { nodesChunk ->
                    currentCoroutineContext().ensureActive()
                    val nodes = mutableListOf<Node>()

                    nodesChunk.forEach { nodeContent ->
                        val nodeLength = nodeContent.computeLength()
                        val nodeContent = nodeContent.let { nodeContent ->
                            when (nodeContent) {
                                is NodeContent.Header -> {
                                    chapterHeaders.add(
                                        ChapterHeader(
                                            chapterId = ChapterId(chapterIndex),
                                            linkId = LinkId(index),
                                            header = nodeContent,
                                            chapterOffset = totalLength,
                                            chapterLength = nodeLength
                                        )
                                    )
                                    chapterIndex++
                                    nodeContent
                                }

                                is NodeContent.Image -> {
                                    // Accounting for duplicates in nodes
                                    val imagePath = savedImages.getOrPut(
                                        key = FilePath(nodeContent.content)
                                    ) {
                                        bookRepository.saveBookContentImage(
                                            book = book,
                                            imagePath = FilePath(nodeContent.content),
                                            id = imageIndex
                                        ).getOrElse {
                                            logW(
                                                TAG,
                                                "Could not save book content image with message: ${it.message}"
                                            )
                                            return@forEach
                                        }
                                    }
                                    imageIndex++
                                    nodeContent.copy(
                                        content = imagePath.path
                                    )
                                }

                                else -> nodeContent
                            }
                        }

                        nodes.add(
                            Node(
                                id = NodeId(index),
                                position = NodePosition(
                                    length = nodeLength,
                                    offset = totalLength
                                ),
                                content = nodeContent
                            )
                        )
                        totalLength += nodeLength
                        index++
                    }

                    bookRepository.saveBookContentNodes(book, nodes).getOrThrow()
                }
        }

        if (index == 0) {
            error("No nodes were inserted.")
        }

        currentCoroutineContext().ensureActive()

        val chapters = mutableListOf<Chapter>()
        chapterHeaders.forEachIndexed { index, chapterHeader ->
            var endChapterOffset = totalLength
            for (nextChapterHeader in chapterHeaders.subList(index + 1, chapterHeaders.size)) {
                if (NodeContent.HeaderLevel.isHigherOrEqual(
                        nextChapterHeader.header.level,
                        chapterHeader.header.level
                    )
                ) {
                    endChapterOffset = nextChapterHeader.chapterOffset
                    break
                }
            }

            var parentId: ChapterId? = null
            if (index > 0) {
                for (prevChapterHeader in chapterHeaders.subList(0, index).asReversed()) {
                    if (
                        NodeContent.HeaderLevel.isHigher(
                            prevChapterHeader.header.level,
                            chapterHeader.header.level
                        )
                    ) {
                        parentId = prevChapterHeader.chapterId
                        break
                    }
                }
            }

            chapters.add(
                Chapter(
                    id = chapterHeader.chapterId,
                    linkId = chapterHeader.linkId,
                    parentId = parentId,
                    title = chapterHeader.header.title,
                    position = ChapterPosition(
                        chapterOffset = chapterHeader.chapterOffset,
                        endChapterOffset = endChapterOffset,
                        chapterLength = chapterHeader.chapterLength
                    )
                )
            )
        }

        currentCoroutineContext().ensureActive()

        bookRepository.saveBookContentChapters(book, chapters).getOrThrow()

        currentCoroutineContext().ensureActive()

        val parser = fileHandler.getParserFromBook(book).getOrThrow()
        val metadata = Metadata(
            fileHash = fileHandler.getFileHashFromBook(book).getOrThrow(),
            parserVersion = ParsersData.getCurrentParserVersion(parser),
            parser = parser
        )
        bookRepository.saveBookContentMetadata(book, metadata).getOrThrow()

        val nodesCount = index
        val lastIndex = index - 1

        val progressFraction = if (book.oldProgress != -1f) {
            book.oldProgress
        } else {
            book.progress.getPercentage()
        }
        val currentNodeId = (progressFraction * lastIndex).toInt()
        val currentOffset = (progressFraction * totalLength).toInt()

        val updatedBook = book.copy(
            progress = BookProgress(
                currentNodeId = NodeId(currentNodeId),
                currentNodeIntraOffset = 0,
                totalNodes = nodesCount,
                currentOffset = currentOffset,
                totalLength = totalLength
            ),
            oldProgress = -1f,
            scrollIndex = -1,
            scrollOffset = -1
        )
        bookRepository.updateBook(book = updatedBook)
    }
}