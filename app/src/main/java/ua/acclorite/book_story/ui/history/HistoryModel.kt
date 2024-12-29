package ua.acclorite.book_story.ui.history

import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.history.GroupedHistory
import ua.acclorite.book_story.domain.history.History
import ua.acclorite.book_story.domain.use_case.book.GetBooksById
import ua.acclorite.book_story.domain.use_case.history.DeleteHistory
import ua.acclorite.book_story.domain.use_case.history.DeleteWholeHistory
import ua.acclorite.book_story.domain.use_case.history.GetHistory
import ua.acclorite.book_story.domain.use_case.history.InsertHistory
import ua.acclorite.book_story.presentation.core.util.showToast
import ua.acclorite.book_story.ui.library.LibraryScreen
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.collections.component1
import kotlin.collections.component2

@HiltViewModel
class HistoryModel @Inject constructor(
    private val getHistory: GetHistory,
    private val getBooksById: GetBooksById,
    private val insertHistory: InsertHistory,
    private val deleteHistory: DeleteHistory,
    private val deleteWholeHistory: DeleteWholeHistory
) : ViewModel() {

    private val mutex = Mutex()

    private val _state = MutableStateFlow(HistoryState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }
            getHistoryFromDatabase()
        }

        /* Observe channel - - - - - - - - - - - */
        viewModelScope.launch(Dispatchers.IO) {
            HistoryScreen.refreshListChannel.receiveAsFlow().collectLatest {
                delay(it)
                yield()

                onEvent(HistoryEvent.OnRefreshList(showIndicator = false, hideSearch = false))
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            HistoryScreen.insertHistoryChannel.receiveAsFlow().collectLatest {
                insertHistory.execute(
                    History(
                        bookId = it,
                        book = null,
                        time = Date().time
                    )
                )

                delay(500)
                yield()

                getHistoryFromDatabase()
                LibraryScreen.refreshListChannel.trySend(0)
            }
        }
        /* - - - - - - - - - - - - - - - - - - - */
    }

    private var refreshJob: Job? = null
    private var searchQueryChange: Job? = null
    private var deleteHistoryEntry: Job? = null

    fun onEvent(event: HistoryEvent) {
        when (event) {
            is HistoryEvent.OnRefreshList -> {
                refreshJob?.cancel()
                refreshJob = viewModelScope.launch(Dispatchers.IO) {
                    _state.update {
                        it.copy(
                            isRefreshing = event.showIndicator,
                            isLoading = !event.showIndicator,
                            showSearch = if (event.hideSearch) false else it.showSearch
                        )
                    }

                    yield()
                    getHistoryFromDatabase()

                    if (event.showIndicator) delay(500)
                    _state.update {
                        it.copy(
                            isRefreshing = false,
                            isLoading = false
                        )
                    }
                }
            }

            is HistoryEvent.OnSearchVisibility -> {
                viewModelScope.launch(Dispatchers.IO) {
                    if (!event.show) {
                        getHistoryFromDatabase("")
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

            is HistoryEvent.OnRequestFocus -> {
                viewModelScope.launch(Dispatchers.Main) {
                    if (!_state.value.hasFocused) {
                        event.focusRequester.requestFocus()
                        _state.update {
                            it.copy(
                                hasFocused = true
                            )
                        }
                    }
                }
            }

            is HistoryEvent.OnSearchQueryChange -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            searchQuery = event.query
                        )
                    }
                    searchQueryChange?.cancel()
                    searchQueryChange = launch(Dispatchers.IO) {
                        delay(500)
                        yield()
                        onEvent(HistoryEvent.OnSearch)
                    }
                }
            }

            is HistoryEvent.OnSearch -> {
                viewModelScope.launch(Dispatchers.IO) {
                    getHistoryFromDatabase()
                }
            }

            is HistoryEvent.OnDeleteHistoryEntry -> {
                viewModelScope.launch(Dispatchers.IO) {
                    deleteHistory.execute(event.history)

                    _state.update { it.copy(isRefreshing = true) }
                    getHistoryFromDatabase()
                    _state.update { it.copy(isRefreshing = false) }

                    LibraryScreen.refreshListChannel.trySend(0)

                    deleteHistoryEntry?.cancel()
                    event.snackbarState.currentSnackbarData?.dismiss()

                    deleteHistoryEntry = launch(Dispatchers.IO) {
                        repeat(10) {
                            yield()
                            delay(1000)
                        }

                        yield()
                        event.snackbarState.currentSnackbarData?.dismiss()
                    }
                    val snackbarResult = event.snackbarState.showSnackbar(
                        event.context.getString(R.string.history_element_deleted),
                        event.context.getString(R.string.undo)
                    )

                    when (snackbarResult) {
                        SnackbarResult.Dismissed -> Unit
                        SnackbarResult.ActionPerformed -> {
                            insertHistory.execute(event.history)
                            LibraryScreen.refreshListChannel.trySend(0)

                            _state.update { it.copy(isRefreshing = true) }
                            getHistoryFromDatabase()
                            delay(500)
                            _state.update { it.copy(isRefreshing = false) }
                        }
                    }
                }
            }

            is HistoryEvent.OnShowDeleteWholeHistoryDialog -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            dialog = HistoryScreen.DELETE_WHOLE_HISTORY_DIALOG
                        )
                    }
                }
            }

            is HistoryEvent.OnActionDeleteWholeHistoryDialog -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            dialog = null,
                            isLoading = true
                        )
                    }

                    deleteWholeHistory.execute()
                    LibraryScreen.refreshListChannel.trySend(0)
                    getHistoryFromDatabase("")

                    withContext(Dispatchers.Main) {
                        event.context
                            .getString(R.string.history_deleted)
                            .showToast(context = event.context)
                    }
                }
            }

            is HistoryEvent.OnDismissDialog -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            dialog = null
                        )
                    }
                }
            }
        }
    }

    private suspend fun getHistoryFromDatabase(
        query: String = if (_state.value.showSearch) _state.value.searchQuery else ""
    ) {
        fun isSameDay(historyTime: Calendar, nowTime: Calendar): Boolean {
            return historyTime.get(Calendar.YEAR) == nowTime.get(Calendar.YEAR) &&
                    historyTime.get(Calendar.DAY_OF_YEAR) == nowTime.get(Calendar.DAY_OF_YEAR)
        }

        fun filterMaxElementsById(elements: List<History>): List<History> {
            val groupedById = elements.groupBy { it.bookId }
            val maxElementsById = groupedById.map { (_, values) ->
                values.maxByOrNull { it.time }
            }
            return maxElementsById.filterNotNull()
        }

        val history = getHistory.execute().sortedByDescending {
            it.time
        }.run {
            val books = getBooksById.execute(
                this.map { it.bookId }.distinct()
            ).toMutableList()

            mapNotNull {
                val book = books.find { book -> book.id == it.bookId } ?: return@mapNotNull null
                if (!book.title.lowercase().trim().contains(query.lowercase().trim())) {
                    return@mapNotNull null
                }

                it.copy(book = book)
            }
        }.ifEmpty {
            _state.update {
                it.copy(
                    history = emptyList(),
                    isLoading = false
                )
            }
            return
        }.groupBy { item ->
            val historyTime = Calendar.getInstance().apply {
                timeInMillis = item.time
            }
            val nowTime = Calendar.getInstance()

            return@groupBy when {
                isSameDay(historyTime, nowTime) -> "today"

                isSameDay(
                    historyTime,
                    nowTime.apply { add(Calendar.DAY_OF_YEAR, -1) }
                ) -> "yesterday"

                else -> SimpleDateFormat(
                    "dd.MM.yy",
                    Locale.getDefault()
                ).format(item.time)
            }
        }.map { (day, history) -> GroupedHistory(day, filterMaxElementsById(history)) }

        _state.update {
            it.copy(
                history = history,
                isLoading = false,
            )
        }
    }

    private suspend inline fun <T> MutableStateFlow<T>.update(function: (T) -> T) {
        mutex.withLock {
            yield()
            this.value = function(this.value)
        }
    }
}