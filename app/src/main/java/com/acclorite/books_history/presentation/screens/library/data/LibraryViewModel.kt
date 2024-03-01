package com.acclorite.books_history.presentation.screens.library.data

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acclorite.books_history.domain.model.Category
import com.acclorite.books_history.domain.use_case.DeleteBooks
import com.acclorite.books_history.domain.use_case.GetBooks
import com.acclorite.books_history.domain.use_case.UpdateBooks
import com.acclorite.books_history.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalFoundationApi::class)
@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val getBooks: GetBooks,
    private val updateBooks: UpdateBooks,
    private val deleteBooks: DeleteBooks
) : ViewModel() {

    private val _state = MutableStateFlow(LibraryState())
    val state = _state.asStateFlow()

    private var job: Job? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getBooksFromDatabase()
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

            is LibraryEvent.OnPreloadBooks -> {
                _state.update {
                    it.copy(
                        books = event.books.map { book -> Pair(book, false) }
                    )
                }
            }

            is LibraryEvent.OnRefreshList -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.update {
                        it.copy(
                            isRefreshing = true,
                            selectedItemsCount = 0,
                            hasSelectedItems = false,
                            showSearch = false,
                            books = emptyList()
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
                            isLoading = true
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
                job = viewModelScope.launch {
                    delay(500)
                    getBooksFromDatabase()
                }
            }

            is LibraryEvent.OnSelectBook -> {
                val indexOfBook = _state.value.books.indexOf(event.book)
                val editedList = _state.value.books.toMutableList()
                editedList[indexOfBook] = editedList[indexOfBook].copy(
                    second = event.select ?: !editedList[indexOfBook].second
                )

                _state.update {
                    it.copy(
                        books = editedList.toList(),
                        selectedItemsCount = editedList.filter { book -> book.second }.size,
                        hasSelectedItems = editedList.any { book -> book.second }
                    )
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
                            showDeleteDialog = false
                        )
                    }

                    val books = _state.value.books.filter {
                        it.second
                    }.map { it.first }
                    deleteBooks.execute(books)

                    getBooksFromDatabase()

                    onEvent(LibraryEvent.OnClearSelectedBooks)
                }
            }

            is LibraryEvent.OnUpdateBook -> {
                val books = _state.value.books.toMutableList()

                val index = books.indexOfFirst { it.first.id == event.book.id }
                if (index != -1) {
                    books[index] = Pair(event.book, books[index].second)
                }

                _state.update {
                    it.copy(
                        books = books
                    )
                }
            }
        }
    }

    private suspend fun getBooksFromDatabase(query: String = _state.value.searchQuery) {
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

                is Resource.Loading -> {
                    _state.update {
                        it.copy(
                            isLoading = result.isLoading
                        )
                    }
                }

                is Resource.Error -> Unit
            }
        }
    }

}












