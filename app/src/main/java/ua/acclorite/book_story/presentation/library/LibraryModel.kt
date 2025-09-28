/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.library

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
import ua.acclorite.book_story.domain.use_case.book.DeleteBookUseCase
import ua.acclorite.book_story.domain.use_case.book.SearchBooksUseCase
import ua.acclorite.book_story.domain.use_case.book.UpdateBookUseCase
import ua.acclorite.book_story.presentation.browse.BrowseScreen
import ua.acclorite.book_story.presentation.history.HistoryScreen
import ua.acclorite.book_story.presentation.library.model.SelectableBook
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@HiltViewModel
class LibraryModel @Inject constructor(
    private val updateBookUseCase: UpdateBookUseCase,
    private val searchBooksUseCase: SearchBooksUseCase,
    private val deleteBookUseCase: DeleteBookUseCase
) : ViewModel() {

    private val mutex = Mutex()

    private val _state = MutableStateFlow(LibraryState())
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<LibraryEffect>()
    val effects = _effects.asSharedFlow()

    init {
        onEvent(
            LibraryEvent.OnRefreshList(
                loading = true,
                hideSearch = true
            )
        )

        /* Observe channel - - - - - - - - - - - */
        viewModelScope.launch {
            LibraryScreen.refreshListChannel.receiveAsFlow().collectLatest {
                delay(it)
                onEvent(
                    LibraryEvent.OnRefreshList(
                        loading = false,
                        hideSearch = false
                    )
                )
            }
        }
        /* - - - - - - - - - - - - - - - - - - - */
    }

    private var refreshJob: Job? = null
    private var searchQueryChange: Job? = null

    fun onEvent(event: LibraryEvent) {
        viewModelScope.launch {
            when (event) {
                is LibraryEvent.OnRefreshList -> {
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
                        val books = searchBooksUseCase(
                            if (_state.value.showSearch) _state.value.searchQuery
                            else ""
                        ).map { book -> SelectableBook(book, false) }
                        _state.update {
                            it.copy(
                                books = books,
                                hasSelectedItems = false,
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

                is LibraryEvent.OnSearchVisibility -> {
                    if (!event.show) {
                        onEvent(
                            LibraryEvent.OnRefreshList(
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

                is LibraryEvent.OnSearchQueryChange -> {
                    _state.update {
                        it.copy(
                            searchQuery = event.query
                        )
                    }
                    searchQueryChange?.cancel()
                    searchQueryChange = viewModelScope.launch(Dispatchers.IO) {
                        delay(500)
                        onEvent(LibraryEvent.OnSearch)
                    }
                }

                is LibraryEvent.OnSearch -> {
                    onEvent(
                        LibraryEvent.OnRefreshList(
                            loading = false,
                            hideSearch = false
                        )
                    )
                }

                is LibraryEvent.OnRequestFocus -> {
                    if (!_state.value.hasFocused) {
                        _effects.emit(LibraryEffect.OnRequestFocus)
                        _state.update {
                            it.copy(
                                hasFocused = true
                            )
                        }
                    }
                }

                is LibraryEvent.OnClearSelectedBooks -> {
                    withContext(Dispatchers.Default) {
                        _state.update {
                            it.copy(
                                books = it.books.map { book -> book.copy(selected = false) },
                                hasSelectedItems = false
                            )
                        }
                    }
                }

                is LibraryEvent.OnSelectBook -> {
                    withContext(Dispatchers.Default) {
                        val editedList = _state.value.books.map {
                            if (it.data.id == event.id) it.copy(
                                selected = event.select ?: !it.selected
                            )
                            else it
                        }

                        _state.update {
                            it.copy(
                                books = editedList,
                                selectedItemsCount = editedList.filter { book -> book.selected }.size,
                                hasSelectedItems = editedList.any { book -> book.selected }
                            )
                        }
                    }
                }

                is LibraryEvent.OnSelectBooks -> {
                    withContext(Dispatchers.Default) {
                        val editedList = _state.value.books.map { book ->
                            if (event.books.any { book.data.id == it.data.id }) book.copy(
                                selected = if (event.books.size > 1) true else !book.selected
                            )
                            else book
                        }

                        _state.update {
                            it.copy(
                                books = editedList,
                                selectedItemsCount = editedList.filter { book -> book.selected }.size,
                                hasSelectedItems = editedList.any { book -> book.selected }
                            )
                        }
                    }
                }

                is LibraryEvent.OnShowMoveDialog -> {
                    _state.update {
                        it.copy(
                            dialog = LibraryScreen.MOVE_DIALOG
                        )
                    }
                }

                is LibraryEvent.OnActionMoveDialog -> {
                    withContext(Dispatchers.Default) {
                        _state.value.books.forEach { book ->
                            if (!book.selected) return@forEach
                            updateBookUseCase(
                                book.data.copy(
                                    categories = event.selectedCategories.map { it.id }
                                )
                            )
                        }

                        _state.update {
                            it.copy(
                                books = it.books.map { book ->
                                    if (!book.selected) return@map book
                                    book.copy(
                                        data = book.data.copy(
                                            categories = event.selectedCategories.map { it.id }
                                        ),
                                        selected = false
                                    )
                                },
                                hasSelectedItems = false,
                                dialog = null
                            )
                        }

                        HistoryScreen.refreshListChannel.trySend(0)
                        _effects.emit(LibraryEffect.OnBooksMoved)
                    }
                }

                is LibraryEvent.OnShowDeleteDialog -> {
                    _state.update {
                        it.copy(
                            dialog = LibraryScreen.DELETE_DIALOG
                        )
                    }
                }

                is LibraryEvent.OnActionDeleteDialog -> {
                    withContext(Dispatchers.Default) {
                        _state.value.books.forEach {
                            if (!it.selected) return@forEach
                            deleteBookUseCase(it.data)
                        }

                        _state.update {
                            it.copy(
                                books = it.books.filter { book -> !book.selected },
                                hasSelectedItems = false,
                                dialog = null
                            )
                        }

                        HistoryScreen.refreshListChannel.trySend(0)
                        BrowseScreen.refreshListChannel.trySend(Unit)
                        _effects.emit(LibraryEffect.OnBooksDeleted)
                    }
                }

                is LibraryEvent.OnDismissDialog -> {
                    _state.update {
                        it.copy(
                            dialog = null
                        )
                    }
                }

                is LibraryEvent.OnShowFilterBottomSheet -> {
                    _state.update {
                        it.copy(
                            bottomSheet = LibraryScreen.FILTER_BOTTOM_SHEET
                        )
                    }
                }

                is LibraryEvent.OnDismissBottomSheet -> {
                    _state.update {
                        it.copy(
                            bottomSheet = null
                        )
                    }
                }

                is LibraryEvent.OnNavigateToLibrarySettings -> {
                    _effects.emit(LibraryEffect.OnNavigateToLibrarySettings)
                }

                is LibraryEvent.OnNavigateToBrowse -> {
                    _effects.emit(LibraryEffect.OnNavigateToBrowse)
                }

                is LibraryEvent.OnNavigateToBookInfo -> {
                    _effects.emit(LibraryEffect.OnNavigateToBookInfo(event.id))
                }

                is LibraryEvent.OnNavigateToReader -> {
                    _effects.emit(LibraryEffect.OnNavigateToReader(event.id))
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