/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.book

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import ua.acclorite.book_story.R
import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.core.log.logI
import ua.acclorite.book_story.core.log.logW
import ua.acclorite.book_story.core.ui.UIText
import ua.acclorite.book_story.data.parser.content.ContentParserState
import ua.acclorite.book_story.domain.model.book.Book
import ua.acclorite.book_story.domain.model.book.Chapter
import ua.acclorite.book_story.domain.model.book.Node
import ua.acclorite.book_story.domain.model.book.NodeId
import ua.acclorite.book_story.domain.model.file.FilePath
import ua.acclorite.book_story.domain.repository.BookRepository
import javax.inject.Inject

@Immutable
sealed class GetBookContentState {
    companion object {
        enum class ErrorAction {
            RETRY
        }
    }

    @Immutable
    data object Loading : GetBookContentState()

    @Immutable
    data class Parsing(
        val progress: Float
    ) : GetBookContentState()

    @Immutable
    data object Saving : GetBookContentState()

    @Immutable
    data object Success : GetBookContentState()

    @Immutable
    data class Nodes(
        val nodes: List<Node>
    ) : GetBookContentState()

    @Immutable
    data class Chapters(
        val chapters: List<Chapter>
    ) : GetBookContentState()

    @Immutable
    data class Error(
        val message: UIText,
        val action: ErrorAction
    ) : GetBookContentState()
}

private const val TAG = "GetBookContent"

class GetBookContentUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val isBookContentValidUseCase: IsBookContentValidUseCase,
    private val saveBookContentUseCase: SaveBookContentUseCase
) {
    operator fun invoke(
        book: Book,
        nodeId: Flow<NodeId>,
        range: Int = 200
    ): Flow<GetBookContentState> = channelFlow {
        send(GetBookContentState.Loading)

        currentCoroutineContext().ensureActive()
        val isValid = isBookContentValidUseCase(book).getOrElse {
            logE(
                TAG,
                "Could not validate book content of [${book.title}] with exception: ${it.message}"
            )
            send(
                GetBookContentState.Error(
                    message = UIText.StringResource(
                        R.string.error_could_not_validate,
                        it.message ?: ""
                    ),
                    action = GetBookContentState.Companion.ErrorAction.RETRY
                )
            )
            return@channelFlow
        }
        currentCoroutineContext().ensureActive()

        if (!isValid) {
            logI(TAG, "Content of [${book.title}] is not valid. Parsing.")

            var error = false
            var nodePaths = emptyList<FilePath>()
            bookRepository.parseBookContent(book).getOrElse {
                logE(TAG, "Could not parse book content with exception: ${it.message}")
                send(
                    GetBookContentState.Error(
                        message = UIText.StringResource(
                            R.string.error_could_not_parse_file,
                            it.message ?: ""
                        ),
                        action = GetBookContentState.Companion.ErrorAction.RETRY
                    )
                )
                return@channelFlow
            }.collect { parserState ->
                when (parserState) {
                    is ContentParserState.Progress -> {
                        send(GetBookContentState.Parsing(parserState.progress))
                    }

                    is ContentParserState.Success -> {
                        nodePaths = parserState.nodePaths
                    }

                    is ContentParserState.Error -> {
                        send(
                            GetBookContentState.Error(
                                message = parserState.message,
                                action = GetBookContentState.Companion.ErrorAction.RETRY
                            )
                        )
                        error = true
                    }
                }
            }
            if (error) {
                logE(TAG, "Could not parse book content.")
                return@channelFlow
            }

            currentCoroutineContext().ensureActive()
            logI(
                TAG,
                "Successfully parsed [${book.title}] and got [${nodePaths.size}] node paths. Saving."
            )
            send(GetBookContentState.Saving)

            saveBookContentUseCase(book, nodePaths).getOrElse {
                logE(TAG, "Could not save book content with exception: ${it.message}")
                send(
                    GetBookContentState.Error(
                        message = UIText.StringResource(
                            R.string.error_could_not_save_file,
                            it.message ?: ""
                        ),
                        action = GetBookContentState.Companion.ErrorAction.RETRY
                    )
                )
                return@channelFlow
            }
        } else {
            logI(TAG, "Content of [${book.title}] is valid. Loading.")
        }

        send(GetBookContentState.Success)

        val chapters = bookRepository.getBookContentChapters(book).getOrElse {
            logE(TAG, "Could not save book content with exception: ${it.message}")
            send(
                GetBookContentState.Error(
                    message = UIText.StringResource(
                        R.string.error_could_not_get_chapters,
                        it.message ?: ""
                    ),
                    action = GetBookContentState.Companion.ErrorAction.RETRY
                )
            )
            return@channelFlow
        }
        send(GetBookContentState.Chapters(chapters))

        nodeId.collectLatest { nodeId ->
            bookRepository.getBookContentNodesInRange(
                book,
                nodeId,
                range
            ).fold(
                onSuccess = { nodes ->
                    send(GetBookContentState.Nodes(nodes))
                },
                onFailure = {
                    logW(TAG, "Could not get nodes with exception: ${it.message}")
                }
            )
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getNodesAfter(
        book: Book,
        nodeId: NodeId,
        limit: Int = 100
    ): List<Node> {
        return bookRepository.getBookContentNodesAfter(book, nodeId, limit).fold(
            onSuccess = { it },
            onFailure = {
                logW(TAG, "Could not get nodes after [${nodeId.id}] with exception: ${it.message}")
                emptyList()
            }
        )
    }

    suspend fun getNodesBefore(
        book: Book,
        nodeId: NodeId,
        limit: Int = 100
    ): List<Node> {
        return bookRepository.getBookContentNodesBefore(book, nodeId, limit).fold(
            onSuccess = { it },
            onFailure = {
                logW(TAG, "Could not get nodes before [${nodeId.id}] with exception: ${it.message}")
                emptyList()
            }
        )
    }
}