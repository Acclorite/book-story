package ua.acclorite.book_story.presentation.screens.reader.data

import androidx.activity.ComponentActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.snapshotFlow
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.model.Category
import ua.acclorite.book_story.domain.model.History
import ua.acclorite.book_story.domain.use_case.GetText
import ua.acclorite.book_story.domain.use_case.InsertHistory
import ua.acclorite.book_story.domain.use_case.UpdateBooks
import ua.acclorite.book_story.presentation.Argument
import ua.acclorite.book_story.presentation.Navigator
import ua.acclorite.book_story.presentation.Screen
import ua.acclorite.book_story.util.Resource
import ua.acclorite.book_story.util.UIText
import java.util.Date
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@HiltViewModel(assistedFactory = ReaderViewModel.Factory::class)
class ReaderViewModel @AssistedInject constructor(
    @Assisted book: Book,
    private val getText: GetText,
    private val updateBooks: UpdateBooks,
    private val insertHistory: InsertHistory
) : ViewModel() {

    private val _state = MutableStateFlow(ReaderState(book))
    val state = _state.asStateFlow()

    fun onEvent(event: ReaderEvent) {
        when (event) {
            is ReaderEvent.OnFileNotFound -> {
                _state.update {
                    event.onLoaded()
                    it.copy(
                        errorMessage = UIText.StringResource(R.string.error_file_not_found),
                    )
                }
            }

            is ReaderEvent.OnLoadText -> {
                viewModelScope.launch(Dispatchers.IO) {
                    if (_state.value.book.file == null) {
                        return@launch
                    }

                    if (_state.value.book.text.isEmpty()) {
                        getText.execute(_state.value.book.file!!).collect { result ->
                            when (result) {
                                is Resource.Loading -> Unit
                                is Resource.Success -> {
                                    if (result.data?.isEmpty() != false) {
                                        _state.update {
                                            it.copy(
                                                errorMessage = UIText.StringResource(
                                                    R.string.error_something_went_wrong
                                                ),
                                            )
                                        }
                                        event.onLoaded()
                                        return@collect
                                    }

                                    _state.update {
                                        it.copy(
                                            book = it.book.copy(
                                                text = result.data,
                                            )
                                        )
                                    }


                                }

                                is Resource.Error -> Unit
                            }
                        }
                    }

                    val time = Date().time

                    _state.update {
                        it.copy(
                            book = it.book.copy(
                                lastOpened = time
                            )
                        )
                    }

                    updateBooks.execute(
                        listOf(_state.value.book)
                    )
                    insertHistory.execute(
                        listOf(
                            History(
                                null,
                                _state.value.book,
                                time
                            )
                        )
                    )
                    event.refreshList(_state.value.book)
                    event.navigator.putArgument(
                        Argument("book", _state.value.book)
                    )

                    viewModelScope.launch {
                        snapshotFlow {
                            event.scrollState.layoutInfo.totalItemsCount
                        }.collectLatest { itemsCount ->
                            val scrollTo = (itemsCount * _state.value.book.progress).roundToInt()
                            if (itemsCount >= _state.value.book.text.size) {
                                if (scrollTo > 0) {
                                    while (true) {
                                        try {
                                            event.scrollState.scrollToItem(scrollTo)
                                            break
                                        } catch (e: Exception) {
                                            delay(50)
                                        }
                                    }
                                }

                                delay(500)
                                event.onLoaded()
                                return@collectLatest
                            }
                        }
                    }
                }
            }

            is ReaderEvent.OnShowHideMenu -> {
                val shouldShow = event.show ?: !_state.value.showMenu
                val insetsController = WindowCompat.getInsetsController(
                    event.context.window,
                    event.context.window.decorView
                )

                insetsController.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

                insetsController.apply {
                    if (shouldShow) {
                        show(WindowInsetsCompat.Type.systemBars())
                    } else {
                        hide(WindowInsetsCompat.Type.systemBars())
                    }
                }

                _state.update {
                    it.copy(
                        showMenu = shouldShow
                    )
                }
            }

            is ReaderEvent.OnShowSystemBars -> {
                val insetsController = WindowCompat.getInsetsController(
                    event.context.window,
                    event.context.window.decorView
                )

                insetsController.show(WindowInsetsCompat.Type.systemBars())
            }

            is ReaderEvent.OnScroll -> {
                viewModelScope.launch {
                    val scrollTo = (_state.value.book.text.size * event.progress).roundToInt()

                    event.scrollState.scrollToItem(
                        scrollTo
                    )
                }
            }

            is ReaderEvent.OnChangeProgress -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.update {
                        it.copy(
                            book = it.book.copy(
                                progress = event.progress
                            )
                        )
                    }

                    updateBooks.execute(
                        listOf(_state.value.book)
                    )
                    event.navigator.putArgument(
                        Argument("book", _state.value.book)
                    )
                    event.refreshList(_state.value.book)
                }
            }

            is ReaderEvent.OnShowHideSettingsBottomSheet -> {
                _state.update {
                    it.copy(
                        currentPage = if (it.showSettingsBottomSheet) it.currentPage else 0,
                        showSettingsBottomSheet = !it.showSettingsBottomSheet
                    )
                }
            }

            is ReaderEvent.OnScrollToSettingsPage -> {
                viewModelScope.launch {
                    event.pagerState.scrollToPage(event.page)
                }
            }

            is ReaderEvent.OnMoveBookToAlreadyRead -> {
                viewModelScope.launch {
                    onEvent(ReaderEvent.OnShowSystemBars(event.context))

                    val book = _state.value.book.copy(
                        category = Category.ALREADY_READ
                    )
                    updateBooks.execute(listOf(book))

                    event.refreshList()

                    event.updatePage(
                        Category.entries.dropLastWhile {
                            it != Category.ALREADY_READ
                        }.size - 1
                    )
                    event.navigator.navigate(Screen.LIBRARY)
                }
            }
        }
    }

    fun init(
        navigator: Navigator,
        context: ComponentActivity,
        scrollState: LazyListState,
        refreshList: (Book) -> Unit,
        onLoaded: () -> Unit
    ) {
        viewModelScope.launch {
            val book = navigator.retrieveArgument("book") as? Book

            if (book == null) {
                navigator.navigateBack()
                return@launch
            }

            _state.update {
                ReaderState(book = book)
            }

            if (book.file == null) {
                onEvent(ReaderEvent.OnFileNotFound(onLoaded = { onLoaded() }))
            } else {
                onEvent(ReaderEvent.OnShowHideMenu(false, context))
                onEvent(
                    ReaderEvent.OnLoadText(
                        scrollState,
                        navigator,
                        refreshList = { refreshList(it) },
                        onLoaded = {
                            onLoaded()
                        }
                    )
                )
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(book: Book): ReaderViewModel
    }
}











