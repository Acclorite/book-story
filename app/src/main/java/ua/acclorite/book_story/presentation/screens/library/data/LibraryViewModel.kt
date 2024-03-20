package ua.acclorite.book_story.presentation.screens.library.data

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.acclorite.book_story.domain.model.Category
import ua.acclorite.book_story.domain.model.History
import ua.acclorite.book_story.domain.use_case.DeleteBooks
import ua.acclorite.book_story.domain.use_case.GetBooks
import ua.acclorite.book_story.domain.use_case.InsertHistory
import ua.acclorite.book_story.domain.use_case.UpdateBooks
import ua.acclorite.book_story.domain.util.Resource
import ua.acclorite.book_story.presentation.data.Argument
import ua.acclorite.book_story.presentation.data.Screen
import java.util.Date
import javax.inject.Inject

@OptIn(ExperimentalFoundationApi::class)
@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val getBooks: GetBooks,
    private val updateBooks: UpdateBooks,
    private val deleteBooks: DeleteBooks,
    private val insertHistory: InsertHistory
) : ViewModel() {

    private val _state = MutableStateFlow(LibraryState())
    val state = _state.asStateFlow()

    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    private var job: Job? = null
    private var job2: Job? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }
            getBooksFromDatabase()
            _isReady.update {
                true
            }
        }
    }

    fun onEvent(event: LibraryEvent) {
        when (event) {
            is LibraryEvent.OnScrollToPage -> {
                viewModelScope.launch {
                    event.pagerState.scrollToPage(event.index)
                    _state.update {
                        it.copy(
                            currentPage = event.index
                        )
                    }
                }
            }

            is LibraryEvent.OnUpdateCurrentPage -> {
                _state.update {
                    it.copy(
                        currentPage = event.page
                    )
                }
            }

            is LibraryEvent.OnRefreshList -> {
                job2?.cancel()
                job2 = viewModelScope.launch(Dispatchers.IO) {
                    _state.update {
                        it.copy(
                            isRefreshing = true,
                            hasSelectedItems = false,
                            showSearch = false
                        )
                    }

                    getBooksFromDatabase("")
                    delay(500)
                    _state.update {
                        it.copy(
                            isRefreshing = false
                        )
                    }
                }
            }

            is LibraryEvent.OnLoadList -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.update {
                        it.copy(
                            isLoading = true,
                            hasSelectedItems = false
                        )
                    }
                    getBooksFromDatabase()
                }
            }

            is LibraryEvent.OnSearchShowHide -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val shouldHide = _state.value.showSearch

                    if (shouldHide) {
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
                            showSearch = !shouldHide
                        )
                    }
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

            is LibraryEvent.OnSearchQueryChange -> {
                _state.update {
                    it.copy(
                        searchQuery = event.query
                    )
                }
                job?.cancel()
                job = viewModelScope.launch(Dispatchers.IO) {
                    delay(500)
                    getBooksFromDatabase()
                }
            }

            is LibraryEvent.OnSelectBook -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val editedList = _state.value.books.map {
                        if (it.first.id == event.book.first.id) {
                            it.copy(
                                second = event.select ?: !it.second
                            )
                        } else {
                            it
                        }
                    }

                    _state.update {
                        it.copy(
                            books = editedList,
                            selectedItemsCount = editedList.filter { book -> book.second }.size,
                            hasSelectedItems = editedList.any { book -> book.second }
                        )
                    }
                }
            }

            is LibraryEvent.OnClearSelectedBooks -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.update {
                        it.copy(
                            books = it.books.map { book -> book.copy(second = false) },
                            hasSelectedItems = false
                        )
                    }
                }
            }

            is LibraryEvent.OnShowHideMoveDialog -> {
                val categories = mutableListOf<Category>()

                Category.entries.forEach { category ->
                    if (!_state.value.books
                            .filter { it.second }
                            .all { it.first.category == category }
                    ) {
                        categories.add(
                            category
                        )
                    }
                }

                _state.update {
                    it.copy(
                        categories = categories,
                        selectedCategory = categories[0],
                        showMoveDialog = !it.showMoveDialog
                    )
                }
            }

            is LibraryEvent.OnSelectCategory -> {
                _state.update {
                    it.copy(
                        selectedCategory = event.category
                    )
                }
            }

            is LibraryEvent.OnMoveBooks -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            showMoveDialog = false
                        )
                    }

                    val books = _state.value.books.filter {
                        it.second
                    }.map { it.first.copy(category = _state.value.selectedCategory) }
                    updateBooks.execute(books)

                    getBooksFromDatabase()
                    event.refreshList()

                    onEvent(LibraryEvent.OnClearSelectedBooks)
                    onEvent(
                        LibraryEvent.OnScrollToPage(
                            Category.entries.dropLastWhile {
                                it != _state.value.selectedCategory
                            }.size - 1,
                            event.pagerState
                        )
                    )
                }
            }

            is LibraryEvent.OnShowHideDeleteDialog -> {
                _state.update {
                    it.copy(
                        showDeleteDialog = !it.showDeleteDialog
                    )
                }
            }

            is LibraryEvent.OnDeleteBooks -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            showDeleteDialog = false,
                            isLoading = true
                        )
                    }

                    val books = _state.value.books.filter {
                        it.second
                    }.map { it.first }
                    deleteBooks.execute(books)

                    getBooksFromDatabase()
                    event.refreshList()

                    onEvent(LibraryEvent.OnClearSelectedBooks)
                }
            }

            is LibraryEvent.OnUpdateBook -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val books = _state.value.books.map {
                        if (it.first.id == event.book.id) {
                            it.copy(
                                first = event.book
                            )
                        } else {
                            it
                        }
                    }

                    _state.update {
                        it.copy(
                            books = books
                        )
                    }
                }
            }

            is LibraryEvent.OnNavigateToReaderScreen -> {
                viewModelScope.launch {
                    event.book.id?.let {
                        insertHistory.execute(
                            listOf(
                                History(
                                    null,
                                    it,
                                    null,
                                    Date().time
                                )
                            )
                        )
                    }
                    event.navigator.navigate(
                        Screen.READER,
                        false,
                        Argument(
                            "book", event.book
                        )
                    )
                }
            }
        }
    }

    private suspend fun getBooksFromDatabase(
        query: String = if (_state.value.showSearch) _state.value.searchQuery else ""
    ) {
        getBooks.execute(query).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            books = result.data?.map { book -> Pair(book, false) }
                                ?: emptyList(),
                            isLoading = false
                        )
                    }
                }

                is Resource.Loading -> Unit
                is Resource.Error -> Unit
            }
        }
    }

}












