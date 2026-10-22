/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.reader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import ua.acclorite.book_story.domain.model.book.NodeId
import ua.acclorite.book_story.domain.use_case.book.GetBookContentNodeByOffsetUseCase
import ua.acclorite.book_story.domain.use_case.book.GetBookContentState
import ua.acclorite.book_story.domain.use_case.book.GetBookContentUseCase
import ua.acclorite.book_story.domain.use_case.book.GetBookUseCase
import ua.acclorite.book_story.domain.use_case.book.UpdateBookUseCase
import ua.acclorite.book_story.domain.use_case.history.GetHistoryForBookUseCase
import ua.acclorite.book_story.presentation.history.HistoryScreen
import ua.acclorite.book_story.presentation.library.LibraryScreen
import ua.acclorite.book_story.presentation.reader.model.Checkpoint
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@HiltViewModel
class ReaderModel @Inject constructor(
    private val updateBookUseCase: UpdateBookUseCase,
    private val getBookContentUseCase: GetBookContentUseCase,
    private val getBookUseCase: GetBookUseCase,
    private val getHistoryForBookUseCase: GetHistoryForBookUseCase,
    private val getBookContentNodeByOffsetUseCase: GetBookContentNodeByOffsetUseCase
) : ViewModel() {

    private val mutex = Mutex()

    private val _state = MutableStateFlow(ReaderState())
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<ReaderEffect>()
    val effects = _effects.asSharedFlow()

    private val _paginationNodeId = MutableStateFlow(NodeId(0))

    private var scrollJob: Job? = null
    private val eventStack = mutableListOf<Job>()

    fun onEvent(event: ReaderEvent) {
        viewModelScope.launch {
            when (event) {
                is ReaderEvent.OnLoadContent -> {
                    withContext(Dispatchers.IO) {
                        val lastOpened = getHistoryForBookUseCase(
                            _state.value.book.id
                        )?.time
                        _state.update {
                            it.copy(
                                book = it.book.copy(
                                    lastOpened = lastOpened
                                )
                            )
                        }
                        updateBookUseCase(_state.value.book)
                        _paginationNodeId.update { _state.value.book.progress.currentNodeId }

                        LibraryScreen.refreshListChannel.trySend(0)
                        HistoryScreen.refreshListChannel.trySend(0)

                        getBookContentUseCase(
                            _state.value.book,
                            _paginationNodeId
                        ).collect { state ->
                            when (state) {
                                is GetBookContentState.Loading -> {
                                    _state.update {
                                        it.copy(
                                            layoutState = LayoutState.Loading
                                        )
                                    }
                                    delay(500)
                                }

                                is GetBookContentState.Parsing -> {
                                    _state.update {
                                        it.copy(
                                            layoutState = LayoutState.Parsing(
                                                progress = state.progress
                                            )
                                        )
                                    }
                                }

                                is GetBookContentState.Saving -> {
                                    _state.update {
                                        it.copy(
                                            layoutState = LayoutState.Saving
                                        )
                                    }
                                }

                                is GetBookContentState.Success -> {
                                    _effects.emit(ReaderEffect.OnSystemBarsVisibility(show = null))

                                    getBookUseCase(_state.value.book.id)?.let { book ->
                                        _state.update {
                                            it.copy(
                                                book = book
                                            )
                                        }
                                    }

                                    LibraryScreen.refreshListChannel.trySend(0)
                                    HistoryScreen.refreshListChannel.trySend(0)
                                }

                                is GetBookContentState.Nodes -> {
                                    _state.update {
                                        it.copy(
                                            nodes = state.nodes,
                                            layoutState = LayoutState.Showing,
                                            paginating = false
                                        )
                                    }
                                }

                                is GetBookContentState.Chapters -> {
                                    _state.update {
                                        it.copy(
                                            chapters = state.chapters
                                        )
                                    }
                                }

                                is GetBookContentState.Error -> {
                                    _state.update {
                                        it.copy(
                                            layoutState = LayoutState.Error(
                                                message = state.message,
                                                action = state.action
                                            )
                                        )
                                    }
                                    _effects.emit(ReaderEffect.OnSystemBarsVisibility(show = true))
                                    return@collect
                                }
                            }
                        }
                    }
                }

                is ReaderEvent.OnRetry -> {
                    if (_state.value.book.id == -1) return@launch
                    init(_state.value.book.id)
                }

                is ReaderEvent.OnPaginateForward -> {
                    if (_state.value.paginating) return@launch
                    _state.update { it.copy(paginating = true) }
                    withContext(Dispatchers.IO) {
                        val newNodes = getBookContentUseCase.getNodesAfter(
                            book = _state.value.book,
                            nodeId = event.lastNodeId,
                            limit = 100
                        )
                        if (newNodes.isNotEmpty()) {
                            _state.update { currentState ->
                                val combined = currentState.nodes + newNodes
                                val finalNodes = if (combined.size > 500) {
                                    combined.drop(100)
                                } else {
                                    combined
                                }
                                currentState.copy(
                                    nodes = finalNodes,
                                    paginating = false
                                )
                            }
                        } else {
                            _state.update { it.copy(paginating = false) }
                        }
                    }
                }

                is ReaderEvent.OnPaginateBackward -> {
                    if (_state.value.paginating) return@launch
                    _state.update { it.copy(paginating = true) }
                    withContext(Dispatchers.IO) {
                        val newNodes = getBookContentUseCase.getNodesBefore(
                            book = _state.value.book,
                            nodeId = event.firstNodeId,
                            limit = 100
                        )
                        if (newNodes.isNotEmpty()) {
                            _state.update { currentState ->
                                val combined = newNodes + currentState.nodes
                                val finalNodes = if (combined.size > 500) {
                                    combined.dropLast(100)
                                } else {
                                    combined
                                }
                                currentState.copy(
                                    nodes = finalNodes,
                                    paginating = false
                                )
                            }
                        } else {
                            _state.update { it.copy(paginating = false) }
                        }
                    }
                }

                is ReaderEvent.OnUpdateProgress -> {
                    val node =
                        _state.value.nodes.find { it.id == event.currentNodeId } ?: return@launch
                    _state.update {
                        it.copy(
                            book = it.book.copy(
                                progress = it.book.progress.copy(
                                    currentNodeId = node.id,
                                    currentNodeIntraOffset = event.currentNodeIntraOffset,
                                    currentOffset = node.position.offset + event.currentNodeIntraOffset
                                )
                            )
                        )
                    }
                    updateBookUseCase(_state.value.book)

                    LibraryScreen.refreshListChannel.trySend(300)
                    HistoryScreen.refreshListChannel.trySend(300)
                }

                is ReaderEvent.OnMenuVisibility -> {
                    withContext(Dispatchers.Default) {
                        if (_state.value.lockMenu) return@withContext

                        _effects.emit(
                            ReaderEffect.OnSystemBarsVisibility(
                                show = if (event.show) true
                                else null
                            )
                        )

                        if (event.saveCheckpoint && event.show) {
                            val currentProgress = _state.value.book.progress
                            val checkpoints = _state.value.checkpoints.toMutableList()
                            checkpoints.removeIf {
                                it.offset == currentProgress.currentOffset
                            }
                            checkpoints.add(
                                Checkpoint(
                                    nodeId = currentProgress.currentNodeId,
                                    nodeIntraOffset = currentProgress.currentNodeIntraOffset,
                                    offset = currentProgress.currentOffset
                                )
                            )

                            _state.update {
                                it.copy(checkpoints = checkpoints)
                            }
                        }

                        _state.update {
                            it.copy(showMenu = event.show)
                        }
                    }
                }

                is ReaderEvent.OnScrollToChapter -> {
                    withContext(Dispatchers.Default) {
                        val chapter = event.chapter
                        val targetOffset = chapter.position.chapterOffset
                        val targetNodeId = NodeId(chapter.linkId.id)

                        _state.update {
                            it.copy(
                                scrollTarget = ScrollTarget(
                                    nodeId = targetNodeId,
                                    nodeIntraOffset = 0
                                ),
                                book = it.book.copy(
                                    progress = it.book.progress.copy(
                                        currentNodeId = targetNodeId,
                                        currentNodeIntraOffset = 0,
                                        currentOffset = targetOffset
                                    )
                                )
                            )
                        }
                        _paginationNodeId.update { targetNodeId }
                        updateBookUseCase(_state.value.book)

                        LibraryScreen.refreshListChannel.trySend(300)
                        HistoryScreen.refreshListChannel.trySend(300)
                    }
                }

                is ReaderEvent.OnScroll -> {
                    scrollJob?.cancel()
                    scrollJob = viewModelScope.launch(Dispatchers.Default) {
                        val totalLength = _state.value.book.progress.totalLength
                        if (totalLength <= 0) return@launch
                        val targetOffset = (event.progress.coerceIn(0f, 1f) * totalLength).toInt()
                        val node = _state.value.nodes.find {
                            targetOffset in it.position.offset until it.position.endOffset
                        } ?: getBookContentNodeByOffsetUseCase(_state.value.book, targetOffset)
                        ?: _state.value.nodes.firstOrNull() ?: return@launch

                        val intraOffset = (targetOffset - node.position.offset).coerceAtLeast(0)
                        _state.update {
                            it.copy(
                                scrollTarget = ScrollTarget(
                                    nodeId = node.id,
                                    nodeIntraOffset = intraOffset
                                ),
                                book = it.book.copy(
                                    progress = it.book.progress.copy(
                                        currentNodeId = node.id,
                                        currentNodeIntraOffset = intraOffset,
                                        currentOffset = targetOffset
                                    )
                                )
                            )
                        }
                        _paginationNodeId.update { node.id }
                        updateBookUseCase(_state.value.book)

                        LibraryScreen.refreshListChannel.trySend(300)
                        HistoryScreen.refreshListChannel.trySend(300)
                    }
                }

                is ReaderEvent.OnRestoreCheckpoint -> {
                    withContext(Dispatchers.Default) {
                        _state.update {
                            val checkpoints = it.checkpoints.toMutableList()
                            if (checkpoints.size > 1) checkpoints.remove(event.checkpoint)

                            it.copy(
                                checkpoints = checkpoints,
                                scrollTarget = ScrollTarget(
                                    nodeId = event.checkpoint.nodeId,
                                    nodeIntraOffset = event.checkpoint.nodeIntraOffset
                                ),
                                book = it.book.copy(
                                    progress = it.book.progress.copy(
                                        currentNodeId = event.checkpoint.nodeId,
                                        currentNodeIntraOffset = event.checkpoint.nodeIntraOffset,
                                        currentOffset = event.checkpoint.offset
                                    )
                                )
                            )
                        }
                        _paginationNodeId.update { event.checkpoint.nodeId }
                        updateBookUseCase(_state.value.book)

                        LibraryScreen.refreshListChannel.trySend(300)
                        HistoryScreen.refreshListChannel.trySend(300)
                    }
                }

                is ReaderEvent.OnScrollConsumed -> {
                    _state.update {
                        it.copy(scrollTarget = null)
                    }
                }

                is ReaderEvent.OnLeave -> {
                    _state.update {
                        it.copy(
                            lockMenu = true
                        )
                    }

                    if (_state.value.layoutState is LayoutState.Showing) {
                        updateBookUseCase(_state.value.book)

                        LibraryScreen.refreshListChannel.trySend(0)
                        HistoryScreen.refreshListChannel.trySend(0)
                    }

                    event.navigate()
                }

                is ReaderEvent.OnOpenTranslator -> {
                    _effects.emit(
                        ReaderEffect.OnOpenTranslator(
                            textToTranslate = event.textToTranslate,
                            translateWholeParagraph = event.translateWholeParagraph
                        )
                    )
                }

                is ReaderEvent.OnOpenShareApp -> {
                    _effects.emit(
                        ReaderEffect.OnOpenShareApp(
                            textToShare = event.textToShare
                        )
                    )
                }

                is ReaderEvent.OnOpenWebBrowser -> {
                    _effects.emit(
                        ReaderEffect.OnOpenWebBrowser(
                            textToSearch = event.textToSearch
                        )
                    )
                }

                is ReaderEvent.OnOpenDictionary -> {
                    _effects.emit(
                        ReaderEffect.OnOpenDictionary(
                            textToDefine = event.textToDefine
                        )
                    )
                }

                is ReaderEvent.OnShowSettingsBottomSheet -> {
                    _state.update {
                        it.copy(
                            bottomSheet = ReaderScreen.SETTINGS_BOTTOM_SHEET,
                            drawer = null
                        )
                    }
                }

                is ReaderEvent.OnDismissBottomSheet -> {
                    _state.update {
                        it.copy(
                            bottomSheet = null
                        )
                    }
                }

                is ReaderEvent.OnShowChaptersDrawer -> {
                    _state.update {
                        it.copy(
                            drawer = ReaderScreen.CHAPTERS_DRAWER,
                            bottomSheet = null
                        )
                    }
                }

                is ReaderEvent.OnDismissDrawer -> {
                    _state.update {
                        it.copy(
                            drawer = null
                        )
                    }
                }

                is ReaderEvent.OnNavigateBack -> {
                    _effects.emit(ReaderEffect.OnNavigateBack)
                }

                is ReaderEvent.OnNavigateToBookInfo -> {
                    _effects.emit(ReaderEffect.OnNavigateToBookInfo(event.changePath))
                }
            }
        }.also { eventStack.add(it) }
    }

    fun init(bookId: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            val book = getBookUseCase(bookId)

            if (book == null) {
                _effects.emit(ReaderEffect.OnNavigateBack)
                return@launch
            }

            clear()

            _state.update {
                ReaderState(
                    book = book,
                    scrollTarget = ScrollTarget(
                        nodeId = book.progress.currentNodeId,
                        nodeIntraOffset = book.progress.currentNodeIntraOffset
                    )
                )
            }

            onEvent(ReaderEvent.OnLoadContent)
        }
    }

    fun clearAsync() {
        viewModelScope.launch {
            eventStack.forEach { job ->
                job.cancel()
            }
            _state.update { ReaderState() }
        }
    }

    suspend fun clear() {
        eventStack.forEach { job ->
            job.cancel()
            job.join()
        }
        eventStack.clear()
        _state.update { ReaderState() }
    }

    private suspend inline fun <T> MutableStateFlow<T>.update(function: (T) -> T) {
        mutex.withLock {
            coroutineContext.ensureActive()
            this.value = function(this.value)
        }
    }
}
