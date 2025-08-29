/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.history

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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import ua.acclorite.book_story.domain.model.history.History
import ua.acclorite.book_story.domain.use_case.book.GetBookUseCase
import ua.acclorite.book_story.domain.use_case.history.AddHistoryUseCase
import ua.acclorite.book_story.domain.use_case.history.DeleteHistoryUseCase
import ua.acclorite.book_story.domain.use_case.history.DeleteWholeHistoryUseCase
import ua.acclorite.book_story.domain.use_case.history.GetHistoryUseCase
import ua.acclorite.book_story.presentation.library.LibraryScreen
import java.util.Date
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@HiltViewModel
class HistoryModel @Inject constructor(
    private val getHistoryUseCase: GetHistoryUseCase,
    private val addHistoryUseCase: AddHistoryUseCase,
    private val deleteHistoryUseCase: DeleteHistoryUseCase,
    private val deleteWholeHistoryUseCase: DeleteWholeHistoryUseCase,
    private val getBookUseCase: GetBookUseCase
) : ViewModel() {

    private val mutex = Mutex()

    private val _state = MutableStateFlow(HistoryState())
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<HistoryEffect>()
    val effects = _effects.asSharedFlow()

    init {
        onEvent(
            HistoryEvent.OnRefreshList(
                loading = true,
                hideSearch = true
            )
        )

        /* Observe channel - - - - - - - - - - - */
        viewModelScope.launch {
            HistoryScreen.refreshListChannel.receiveAsFlow().collectLatest { delay ->
                delay(delay)

                onEvent(
                    HistoryEvent.OnRefreshList(
                        loading = false,
                        hideSearch = false
                    )
                )
            }
        }
        viewModelScope.launch {
            HistoryScreen.insertHistoryChannel.receiveAsFlow().collectLatest { bookId ->
                getBookUseCase(bookId)?.let { book ->
                    addHistoryUseCase(
                        History(
                            id = 0,
                            book = book,
                            time = Date().time
                        )
                    )
                }

                delay(500)

                onEvent(
                    HistoryEvent.OnRefreshList(
                        loading = false,
                        hideSearch = false
                    )
                )
                LibraryScreen.refreshListChannel.trySend(0)
            }
        }
        /* - - - - - - - - - - - - - - - - - - - */
    }

    private var refreshJob: Job? = null
    private var searchQueryChange: Job? = null

    fun onEvent(event: HistoryEvent) {
        viewModelScope.launch {
            when (event) {
                is HistoryEvent.OnRefreshList -> {
                    refreshJob?.cancel()
                    refreshJob = viewModelScope.launch(Dispatchers.Default) {
                        _state.update {
                            it.copy(
                                isRefreshing = true,
                                isLoading = event.loading,
                                showSearch = if (event.hideSearch) false else it.showSearch
                            )
                        }

                        ensureActive()
                        val history = getHistoryUseCase(
                            if (_state.value.showSearch) _state.value.searchQuery
                            else ""
                        )
                        _state.update {
                            it.copy(
                                history = history,
                                isLoading = false
                            )
                        }

                        delay(500) // Delay for UI smoothness
                        _state.update {
                            it.copy(
                                isRefreshing = false
                            )
                        }
                    }
                }

                is HistoryEvent.OnSearchVisibility -> {
                    if (!event.show) {
                        onEvent(
                            HistoryEvent.OnRefreshList(
                                loading = false,
                                hideSearch = true
                            )
                        )
                    }

                    _state.update {
                        it.copy(
                            showSearch = event.show,
                            searchQuery = if (event.show) "" else it.searchQuery,
                            hasFocused = if (event.show) false else it.hasFocused
                        )
                    }
                }

                is HistoryEvent.OnRequestFocus -> {
                    if (!_state.value.hasFocused) {
                        _effects.emit(HistoryEffect.OnRequestFocus)
                        _state.update {
                            it.copy(
                                hasFocused = true
                            )
                        }
                    }
                }

                is HistoryEvent.OnSearchQueryChange -> {
                    _state.update {
                        it.copy(
                            searchQuery = event.query
                        )
                    }

                    searchQueryChange?.cancel()
                    searchQueryChange = viewModelScope.launch(Dispatchers.IO) {
                        delay(500)
                        onEvent(HistoryEvent.OnSearch)
                    }
                }

                is HistoryEvent.OnSearch -> {
                    onEvent(
                        HistoryEvent.OnRefreshList(
                            loading = false,
                            hideSearch = false
                        )
                    )
                }

                is HistoryEvent.OnDeleteHistoryEntry -> {
                    withContext(Dispatchers.Default) {
                        deleteHistoryUseCase(event.history)

                        onEvent(
                            HistoryEvent.OnRefreshList(
                                loading = false,
                                hideSearch = false
                            )
                        )
                        LibraryScreen.refreshListChannel.trySend(0)

                        _effects.emit(HistoryEffect.OnShowSnackbar(event.history))
                    }
                }

                is HistoryEvent.OnRestoreHistoryEntry -> {
                    withContext(Dispatchers.Default) {
                        addHistoryUseCase(event.history)

                        onEvent(
                            HistoryEvent.OnRefreshList(
                                loading = false,
                                hideSearch = false
                            )
                        )
                        LibraryScreen.refreshListChannel.trySend(0)
                    }
                }

                is HistoryEvent.OnShowDeleteWholeHistoryDialog -> {
                    _state.update {
                        it.copy(
                            dialog = HistoryScreen.DELETE_WHOLE_HISTORY_DIALOG
                        )
                    }
                }

                is HistoryEvent.OnActionDeleteWholeHistoryDialog -> {
                    withContext(Dispatchers.Default) {
                        _state.update {
                            it.copy(
                                dialog = null,
                                isLoading = true
                            )
                        }

                        deleteWholeHistoryUseCase()

                        onEvent(
                            HistoryEvent.OnRefreshList(
                                loading = true,
                                hideSearch = true
                            )
                        )
                        LibraryScreen.refreshListChannel.trySend(0)

                        _effects.emit(HistoryEffect.OnWholeHistoryDeleted)
                    }
                }

                is HistoryEvent.OnDismissDialog -> {
                    _state.update {
                        it.copy(
                            dialog = null
                        )
                    }
                }

                is HistoryEvent.OnNavigateToLibrary -> {
                    _effects.emit(HistoryEffect.OnNavigateToLibrary)
                }

                is HistoryEvent.OnNavigateToBookInfo -> {
                    _effects.emit(HistoryEffect.OnNavigateToBookInfo(event.bookId))
                }

                is HistoryEvent.OnNavigateToReader -> {
                    _effects.emit(HistoryEffect.OnNavigateToReader(event.bookId))
                }
            }
        }
    }

    private suspend inline fun <T> MutableStateFlow<T>.update(function: (T) -> T) {
        mutex.withLock {
            coroutineContext.ensureActive()
            this.value = function(this.value)
        }
    }
}