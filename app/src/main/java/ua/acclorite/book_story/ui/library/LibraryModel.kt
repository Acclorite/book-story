package ua.acclorite.book_story.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.library.book.Book
import ua.acclorite.book_story.domain.library.book.SelectableBook
import ua.acclorite.book_story.domain.use_case.book.DeleteBooks
import ua.acclorite.book_story.domain.use_case.book.GetBooks
import ua.acclorite.book_story.domain.use_case.book.UpdateBook
import ua.acclorite.book_story.presentation.core.util.showToast
import ua.acclorite.book_story.ui.browse.BrowseScreen
import ua.acclorite.book_story.ui.history.HistoryScreen
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class LibraryModel @Inject constructor(
    private val getBooks: GetBooks,
    private val deleteBooks: DeleteBooks,
    private val moveBooks: UpdateBook
) : ViewModel() {

    private val _state = MutableStateFlow(LibraryState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getBooksFromDatabase("")
        }

        /* Observe channel - - - - - - - - - - - */
        viewModelScope.launch(Dispatchers.IO) {
            LibraryScreen.refreshListChannel.receiveAsFlow().debounce(200).collectLatest {
                onEvent(LibraryEvent.OnRefreshList(showIndicator = false, hideSearch = false))
            }
        }
        /* - - - - - - - - - - - - - - - - - - - */
    }

    private var refreshJob: Job? = null
    private var searchQueryChange: Job? = null

    fun onEvent(event: LibraryEvent) {
        when (event) {
            is LibraryEvent.OnRefreshList -> {
                refreshJob?.cancel()
                refreshJob = viewModelScope.launch(Dispatchers.IO) {
                    _state.update {
                        it.copy(
                            isRefreshing = event.showIndicator,
                            isLoading = !event.showIndicator,
                            hasSelectedItems = false,
                            showSearch = if (event.hideSearch) false else it.showSearch
                        )
                    }

                    yield()
                    getBooksFromDatabase()

                    if (event.showIndicator) delay(500)
                    _state.update {
                        it.copy(
                            isRefreshing = false,
                            isLoading = false
                        )
                    }
                }
            }

            is LibraryEvent.OnSearchVisibility -> {
                viewModelScope.launch(Dispatchers.IO) {
                    if (!event.show) {
                        getBooksFromDatabase("")
                    } else {
                        _state.update {
                            it.copy(
                                searchQuery = "",
                                hasFocused = false
                            )
                        }
                    }

                    _state.update {
                        it.copy(
                            showSearch = event.show
                        )
                    }
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
                    yield()
                    onEvent(LibraryEvent.OnSearch)
                }
            }

            is LibraryEvent.OnSearch -> {
                viewModelScope.launch(Dispatchers.IO) {
                    getBooksFromDatabase()
                }
            }

            is LibraryEvent.OnRequestFocus -> {
                if (!_state.value.hasFocused) {
                    event.focusRequester.requestFocus()
                    _state.update {
                        it.copy(
                            hasFocused = true
                        )
                    }
                }
            }

            is LibraryEvent.OnClearSelectedBooks -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.update {
                        it.copy(
                            books = it.books.map { book -> book.copy(selected = false) },
                            hasSelectedItems = false
                        )
                    }
                }
            }

            is LibraryEvent.OnSelectBook -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val editedList = _state.value.books.map {
                        if (it.data.id == event.id) it.copy(selected = event.select ?: !it.selected)
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

            is LibraryEvent.OnShowMoveDialog -> {
                _state.update {
                    it.copy(
                        dialog = LibraryScreen.MOVE_DIALOG
                    )
                }
            }

            is LibraryEvent.OnActionMoveDialog -> {
                viewModelScope.launch {
                    _state.value.books.forEach { book ->
                        if (!book.selected) return@forEach
                        moveBooks.execute(
                            book.data.copy(
                                category = event.selectedCategory
                            )
                        )
                    }

                    _state.update {
                        it.copy(
                            books = it.books.map { book ->
                                if (!book.selected) return@map book
                                book.copy(
                                    data = book.data.copy(
                                        category = event.selectedCategory
                                    ),
                                    selected = false
                                )
                            },
                            hasSelectedItems = false,
                            dialog = null
                        )
                    }

                    HistoryScreen.refreshListChannel.trySend(Unit)
                    LibraryScreen.scrollToPageCompositionChannel.trySend(
                        event.categories.dropLastWhile {
                            it.category != event.selectedCategory
                        }.lastIndex
                    )

                    withContext(Dispatchers.Main) {
                        event.context
                            .getString(R.string.books_moved)
                            .showToast(context = event.context)
                    }
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
                viewModelScope.launch {
                    deleteBooks.execute(
                        _state.value.books.mapNotNull {
                            if (!it.selected) return@mapNotNull null
                            it.data
                        }
                    )

                    _state.update {
                        it.copy(
                            books = it.books.filter { book -> !book.selected },
                            hasSelectedItems = false,
                            dialog = null
                        )
                    }

                    HistoryScreen.refreshListChannel.trySend(Unit)
                    BrowseScreen.refreshListChannel.trySend(Unit)

                    withContext(Dispatchers.Main) {
                        event.context
                            .getString(R.string.books_deleted)
                            .showToast(context = event.context)
                    }
                }
            }

            is LibraryEvent.OnDismissDialog -> {
                _state.update {
                    it.copy(
                        dialog = null
                    )
                }
            }
        }
    }

    private suspend fun getBooksFromDatabase(
        query: String = if (_state.value.showSearch) _state.value.searchQuery else ""
    ) {
        val books = getBooks
            .execute(query)
            .sortedWith(compareByDescending<Book> { it.lastOpened }.thenBy { it.title })
            .map { book -> SelectableBook(book, false) }

        _state.update {
            it.copy(
                books = books,
                isLoading = false
            )
        }
    }
}