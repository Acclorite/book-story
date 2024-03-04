package ua.acclorite.book_story.presentation.screens.history.data

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
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
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.GroupedHistory
import ua.acclorite.book_story.domain.model.History
import ua.acclorite.book_story.domain.use_case.DeleteHistory
import ua.acclorite.book_story.domain.use_case.DeleteWholeHistory
import ua.acclorite.book_story.domain.use_case.GetHistory
import ua.acclorite.book_story.domain.use_case.InsertHistory
import ua.acclorite.book_story.util.Resource
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val insertHistory: InsertHistory,
    private val getHistory: GetHistory,
    private val deleteWholeHistory: DeleteWholeHistory,
    private val deleteHistory: DeleteHistory
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryState())
    val state = _state.asStateFlow()

    private var job: Job? = null

    init {
        onEvent(HistoryEvent.OnLoadList)
    }

    fun onEvent(event: HistoryEvent) {
        when (event) {
            is HistoryEvent.OnRefreshList -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.update {
                        it.copy(
                            isRefreshing = true,
                            showSearch = false
                        )
                    }

                    getHistoryFromDatabase("")
                    delay(500)
                    _state.update {
                        it.copy(
                            isRefreshing = false
                        )
                    }
                }
            }

            is HistoryEvent.OnLoadList -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                    getHistoryFromDatabase()
                    onEvent(HistoryEvent.OnUpdateScrollIndex(0))
                    onEvent(HistoryEvent.OnUpdateScrollOffset(0))
                }
            }

            is HistoryEvent.OnDeleteWholeHistory -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            showDeleteWholeHistoryDialog = false
                        )
                    }

                    deleteWholeHistory.execute()
                    getHistoryFromDatabase("")
                }

            }

            is HistoryEvent.OnShowHideDeleteWholeHistoryDialog -> {
                _state.update {
                    it.copy(
                        showDeleteWholeHistoryDialog = !it.showDeleteWholeHistoryDialog
                    )
                }
            }

            is HistoryEvent.OnDeleteHistoryElement -> {
                viewModelScope.launch {
                    deleteHistory.execute(
                        listOf(event.historyToDelete)
                    )
                    onEvent(HistoryEvent.OnRefreshList)

                    event.snackbarState.currentSnackbarData?.dismiss()
                    val snackbar = event.snackbarState.showSnackbar(
                        event.context.getString(R.string.history_element_deleted),
                        event.context.getString(R.string.undo),
                        duration = SnackbarDuration.Long
                    )

                    when (snackbar) {
                        SnackbarResult.Dismissed -> return@launch
                        SnackbarResult.ActionPerformed -> {
                            insertHistory.execute(
                                listOf(event.historyToDelete)
                            )
                            onEvent(HistoryEvent.OnRefreshList)
                        }
                    }
                }
            }

            is HistoryEvent.OnSearchShowHide -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val shouldHide = _state.value.showSearch

                    if (shouldHide) {
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
                            showSearch = !shouldHide
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
                job?.cancel()
                job = viewModelScope.launch {
                    delay(500)
                    getHistoryFromDatabase()
                }
            }

            is HistoryEvent.OnRequestFocus -> {
                if (!_state.value.hasFocused) {
                    event.focusRequester.requestFocus()
                    _state.update {
                        it.copy(
                            hasFocused = true
                        )
                    }
                }
            }

            is HistoryEvent.OnUpdateScrollIndex -> {
                _state.update {
                    it.copy(
                        scrollIndex = event.index
                    )
                }
            }

            is HistoryEvent.OnUpdateScrollOffset -> {
                _state.update {
                    it.copy(
                        scrollOffset = event.offset
                    )
                }
            }
        }
    }

    private suspend fun getHistoryFromDatabase(
        query: String = if (_state.value.showSearch) _state.value.searchQuery else ""
    ) {
        fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
            return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
        }

        fun filterMaxElementsById(elements: List<History>): List<History> {
            val groupedById = elements.groupBy { it.book.id }
            val maxElementsById = groupedById.map { (_, values) ->
                values.maxByOrNull { it.time }
            }
            return maxElementsById.filterNotNull()
        }

        getHistory.execute().collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            history = emptyList()
                        )
                    }
                    val history = result.data?.sortedByDescending { it.time } ?: emptyList()

                    if (history.isEmpty()) {
                        _state.update {
                            it.copy(
                                isLoading = false
                            )
                        }
                        return@collect
                    }

                    val groupedHistory = mutableListOf<GroupedHistory>()

                    history
                        .filter {
                            it.book.title.lowercase().trim().contains(query.lowercase().trim())
                        }.groupBy { item ->
                            val calendar = Calendar.getInstance().apply {
                                timeInMillis = item.time
                            }
                            val now = Calendar.getInstance()

                            when {
                                isSameDay(calendar, now) -> "today"
                                isSameDay(
                                    calendar,
                                    now.apply {
                                        add(
                                            Calendar.DAY_OF_YEAR,
                                            -1
                                        )
                                    }) -> "yesterday"

                                else -> SimpleDateFormat(
                                    "dd.MM.yy",
                                    Locale.getDefault()
                                ).format(item.time)
                            }
                        }.forEach { (key, value) ->
                            groupedHistory.add(
                                GroupedHistory(
                                    key,
                                    filterMaxElementsById(value)
                                )
                            )
                        }

                    _state.update {
                        it.copy(
                            history = groupedHistory,
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


















