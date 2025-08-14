/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.browse

import androidx.core.net.toUri
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
import ua.acclorite.book_story.data.model.common.NullableBook
import ua.acclorite.book_story.domain.model.file.File
import ua.acclorite.book_story.domain.use_case.book.AddBookUseCase
import ua.acclorite.book_story.domain.use_case.file_system.GetBookFromFileUseCase
import ua.acclorite.book_story.domain.use_case.file_system.GetFilesUseCase
import ua.acclorite.book_story.presentation.browse.model.SelectableFile
import ua.acclorite.book_story.presentation.library.LibraryScreen
import ua.acclorite.book_story.presentation.library.model.SelectableNullableBook
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@HiltViewModel
class BrowseModel @Inject constructor(
    private val addBookUseCase: AddBookUseCase,
    private val getFilesUseCase: GetFilesUseCase,
    private val getBookFromFileUseCase: GetBookFromFileUseCase
) : ViewModel() {

    private val mutex = Mutex()

    private val _state = MutableStateFlow(BrowseState())
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<BrowseEffect>()
    val effects = _effects.asSharedFlow()

    init {
        onEvent(
            BrowseEvent.OnRefreshList(
                loading = true,
                hideSearch = true
            )
        )

        /* Observe channel - - - - - - - - - - - */
        viewModelScope.launch {
            BrowseScreen.refreshListChannel.receiveAsFlow().collectLatest {
                onEvent(
                    BrowseEvent.OnRefreshList(
                        loading = false,
                        hideSearch = false
                    )
                )
            }
        }
        /* - - - - - - - - - - - - - - - - - - - */
    }

    private var refreshJob: Job? = null
    private var searchQueryChangeJob: Job? = null
    private var getAddDialogBooksJob: Job? = null

    fun onEvent(event: BrowseEvent) {
        viewModelScope.launch {
            when (event) {
                is BrowseEvent.OnRefreshList -> {
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
                        val selectableFiles = getFilesUseCase(
                            if (_state.value.showSearch) _state.value.searchQuery
                            else ""
                        ).map { file ->
                            SelectableFile(
                                name = file.name,
                                uri = file.uri.toUri(),
                                path = file.path,
                                size = file.size,
                                lastModified = file.lastModified,
                                isDirectory = file.isDirectory,
                                selected = false
                            )
                        }
                        _state.update {
                            it.copy(
                                files = selectableFiles,
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

                is BrowseEvent.OnSearchVisibility -> {
                    if (!event.show) {
                        onEvent(
                            BrowseEvent.OnRefreshList(
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

                is BrowseEvent.OnRequestFocus -> {
                    if (!_state.value.hasFocused) {
                        _effects.emit(BrowseEffect.OnRequestFocus)
                        _state.update {
                            it.copy(
                                hasFocused = true
                            )
                        }
                    }
                }

                is BrowseEvent.OnSearchQueryChange -> {
                    _state.update {
                        it.copy(
                            searchQuery = event.query
                        )
                    }

                    searchQueryChangeJob?.cancel()
                    searchQueryChangeJob = viewModelScope.launch(Dispatchers.IO) {
                        delay(500)
                        onEvent(BrowseEvent.OnSearch)
                    }
                }

                is BrowseEvent.OnSearch -> {
                    onEvent(
                        BrowseEvent.OnRefreshList(
                            loading = false,
                            hideSearch = false
                        )
                    )
                }

                is BrowseEvent.OnClearSelectedFiles -> {
                    withContext(Dispatchers.Default) {
                        _state.update {
                            it.copy(
                                files = it.files.map { file -> file.copy(selected = false) },
                                hasSelectedItems = false
                            )
                        }
                    }
                }

                is BrowseEvent.OnSelectFiles -> {
                    withContext(Dispatchers.Default) {
                        val editedList = _state.value.files.map { file ->
                            if (event.files.any { file.path.startsWith(it.path, true) }) {
                                file.copy(
                                    selected = if (event.files.size > 1) true else !file.selected
                                )
                            } else file
                        }

                        _state.update {
                            it.copy(
                                files = editedList,
                                selectedItemsCount = editedList.filter { file ->
                                    file.selected
                                }.size.let { selectedItems ->
                                    if (selectedItems == 0) return@let it.selectedItemsCount
                                    selectedItems
                                },
                                hasSelectedItems = editedList.any { file ->
                                    file.selected
                                }
                            )
                        }
                    }
                }

                is BrowseEvent.OnShowFilterBottomSheet -> {
                    _state.update {
                        it.copy(
                            bottomSheet = BrowseScreen.FILTER_BOTTOM_SHEET
                        )
                    }
                }

                is BrowseEvent.OnDismissBottomSheet -> {
                    _state.update {
                        it.copy(
                            bottomSheet = null
                        )
                    }
                }

                is BrowseEvent.OnShowAddDialog -> {
                    _state.update {
                        it.copy(
                            dialog = BrowseScreen.ADD_DIALOG,
                            selectedBooksAddDialog = emptyList()
                        )
                    }

                    getAddDialogBooksJob = viewModelScope.launch(Dispatchers.IO) {
                        _state.update {
                            it.copy(
                                loadingAddDialog = true
                            )
                        }

                        val books = _state.value.files.mapNotNull { file ->
                            ensureActive()
                            if (!file.selected) return@mapNotNull null
                            SelectableNullableBook(
                                data = getBookFromFileUseCase(
                                    File(
                                        name = file.name,
                                        uri = file.uri.toString(),
                                        path = file.path,
                                        size = file.size,
                                        lastModified = file.lastModified,
                                        isDirectory = file.isDirectory
                                    )
                                ),
                                selected = true
                            )
                        }

                        if (books.isEmpty()) {
                            _state.update {
                                it.copy(
                                    loadingAddDialog = false,
                                    dialog = null
                                )
                            }
                            return@launch
                        }

                        _state.update {
                            it.copy(
                                selectedBooksAddDialog = books,
                                loadingAddDialog = false
                            )
                        }
                    }
                }

                is BrowseEvent.OnDismissAddDialog -> {
                    _state.update {
                        it.copy(
                            dialog = null
                        )
                    }
                    getAddDialogBooksJob?.cancel()
                }

                is BrowseEvent.OnActionAddDialog -> {
                    withContext(Dispatchers.Default) {
                        _state.value.selectedBooksAddDialog.mapNotNull {
                            if (it.data is NullableBook.NotNull && it.selected) return@mapNotNull it.data.bookWithCover
                            return@mapNotNull null
                        }.ifEmpty { return@withContext }.forEach { bookWithCover ->
                            addBookUseCase(
                                bookWithCover.book,
                                bookWithCover.coverImage
                            )
                        }

                        LibraryScreen.refreshListChannel.trySend(0)
                        LibraryScreen.scrollToPageCompositionChannel.trySend(0)

                        _effects.emit(BrowseEffect.OnNavigateToLibrary)
                        _effects.emit(BrowseEffect.OnBooksAdded)

                        _state.update {
                            it.copy(
                                dialog = null
                            )
                        }
                        onEvent(
                            BrowseEvent.OnRefreshList(
                                loading = false,
                                hideSearch = false
                            )
                        )
                        onEvent(BrowseEvent.OnClearSelectedFiles)
                    }
                }

                is BrowseEvent.OnSelectAddDialog -> {
                    withContext(Dispatchers.Default) {
                        val index = _state.value.selectedBooksAddDialog.indexOf(event.book)
                        if (index == -1) return@withContext

                        val editedList = _state.value.selectedBooksAddDialog.toMutableList()
                        editedList[index] = editedList[index].let {
                            it.copy(
                                selected = !it.selected
                            )
                        }

                        if (
                            !editedList
                                .filter { it.data is NullableBook.NotNull }
                                .any { it.selected }
                        ) {
                            return@withContext
                        }

                        _state.update {
                            it.copy(
                                selectedBooksAddDialog = editedList
                            )
                        }
                    }
                }

                is BrowseEvent.OnDismissDialog -> {
                    _state.update {
                        it.copy(
                            dialog = null
                        )
                    }
                }

                is BrowseEvent.OnUpdatePinnedPaths -> {
                    _effects.emit(BrowseEffect.OnUpdatePinnedPaths(event.path))
                }

                is BrowseEvent.OnNavigateToLibrary -> {
                    _effects.emit(BrowseEffect.OnNavigateToLibrary)
                }

                is BrowseEvent.OnNavigateToBrowseSettings -> {
                    _effects.emit(BrowseEffect.OnNavigateToBrowseSettings)
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