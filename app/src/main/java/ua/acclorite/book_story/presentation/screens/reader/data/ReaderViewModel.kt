package ua.acclorite.book_story.presentation.screens.reader.data

import android.app.SearchManager
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.snapshotFlow
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import ua.acclorite.book_story.domain.use_case.GetBooksById
import ua.acclorite.book_story.domain.use_case.GetLatestHistory
import ua.acclorite.book_story.domain.use_case.GetText
import ua.acclorite.book_story.domain.use_case.UpdateBooks
import ua.acclorite.book_story.domain.util.UIText
import ua.acclorite.book_story.presentation.data.Argument
import ua.acclorite.book_story.presentation.data.Navigator
import ua.acclorite.book_story.presentation.data.Screen
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class ReaderViewModel @Inject constructor(
    private val updateBooks: UpdateBooks,
    private val getText: GetText,
    private val getLatestHistory: GetLatestHistory,
    private val getBooksById: GetBooksById
) : ViewModel() {

    private val _state = MutableStateFlow(ReaderState())
    val state = _state.asStateFlow()

    fun onEvent(event: ReaderEvent) {
        when (event) {
            is ReaderEvent.OnTextIsEmpty -> {
                _state.update {
                    event.onLoaded()
                    it.copy(
                        errorMessage = UIText.StringResource(R.string.error_no_text),
                    )
                }
            }

            is ReaderEvent.OnLoadText -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val text = getText.execute(_state.value.book.textPath)

                    if (text.isEmpty()) {
                        event.onTextIsEmpty()
                    }

                    val textAsLine = text.joinToString(
                        separator = "\n",
                        transform = {
                            it.line
                        }
                    )

                    val letters = textAsLine
                        .replace("\n", "")
                        .length
                    val words = textAsLine
                        .replace("\n", " ")
                        .split("\\s+".toRegex())
                        .size

                    _state.update {
                        it.copy(
                            text = text,
                            letters = letters,
                            words = words
                        )
                    }

                    val history = _state.value.book.id.let {
                        getLatestHistory.execute(
                            it
                        )
                    }

                    _state.update {
                        it.copy(
                            book = it.book.copy(
                                lastOpened = history?.time,
                            )
                        )
                    }

                    updateBooks.execute(
                        listOf(_state.value.book)
                    )
                    event.refreshList(_state.value.book)

                    viewModelScope.launch {
                        snapshotFlow {
                            event.listState.layoutInfo.totalItemsCount
                        }.collectLatest { itemsCount ->
                            val index = _state.value.book.scrollIndex
                            val offset = _state.value.book.scrollOffset

                            if (itemsCount >= _state.value.text.size) {
                                if (index > 0 || offset > 0) {
                                    var loaded = false
                                    for (i in 1..100) {
                                        try {
                                            event.listState.scrollToItem(
                                                index,
                                                offset
                                            )
                                            loaded = true
                                            break
                                        } catch (e: Exception) {
                                            Log.w(
                                                "READER",
                                                "Couldn't scroll to desired index and offset"
                                            )
                                            delay(100)
                                        }
                                    }

                                    if (!loaded) {
                                        event.onTextIsEmpty()
                                    }
                                }

                                delay(100)
                                event.onLoaded()
                                return@collectLatest
                            }
                        }
                    }
                }
            }

            is ReaderEvent.OnShowHideMenu -> {
                if (_state.value.lockMenu) {
                    return
                }

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

            is ReaderEvent.OnGoBack -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            lockMenu = true
                        )
                    }

                    val insetsController = WindowCompat.getInsetsController(
                        event.context.window,
                        event.context.window.decorView
                    )

                    if (event.listState.layoutInfo.totalItemsCount > 0) {
                        val firstVisibleItemIndex = event.listState.firstVisibleItemIndex
                        val firstVisibleItemOffset = event.listState.firstVisibleItemScrollOffset
                        val lastVisibleItemIndex = event.listState.layoutInfo
                            .visibleItemsInfo.last().index

                        val progress = if (firstVisibleItemIndex > 0) {
                            if (lastVisibleItemIndex >= (event.listState.layoutInfo.totalItemsCount - 1)) {
                                1f
                            } else {
                                (firstVisibleItemIndex.toFloat() / (_state.value.text.lastIndex)
                                    .toFloat())
                            }
                        } else {
                            0f
                        }

                        _state.update {
                            it.copy(
                                book = it.book.copy(
                                    progress = progress,
                                    scrollIndex = firstVisibleItemIndex,
                                    scrollOffset = firstVisibleItemOffset
                                )
                            )
                        }
                    }

                    updateBooks.execute(
                        listOf(_state.value.book)
                    )
                    event.navigator.putArgument(
                        Argument("book", _state.value.book.id)
                    )
                    event.refreshList(_state.value.book)

                    insetsController.show(WindowInsetsCompat.Type.systemBars())
                    event.navigate(event.navigator)
                }
            }

            is ReaderEvent.OnScroll -> {
                viewModelScope.launch {
                    val scrollTo = (_state.value.text.size * event.progress).roundToInt()

                    event.listState.scrollToItem(
                        scrollTo
                    )
                }
            }

            is ReaderEvent.OnChangeProgress -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.update {
                        it.copy(
                            book = it.book.copy(
                                progress = event.progress,
                                scrollIndex = event.firstVisibleItemIndex,
                                scrollOffset = event.firstVisibleItemOffset
                            )
                        )
                    }

                    updateBooks.execute(
                        listOf(_state.value.book)
                    )
                    event.navigator.putArgument(
                        Argument(
                            "book",
                            _state.value.book.id
                        )
                    )
                    event.refreshList(_state.value.book)
                }
            }

            is ReaderEvent.OnShowHideSettingsBottomSheet -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.update {
                        it.copy(
                            currentPage = if (it.showSettingsBottomSheet) it.currentPage else 0,
                            showSettingsBottomSheet = !it.showSettingsBottomSheet
                        )
                    }
                }
            }

            is ReaderEvent.OnScrollToSettingsPage -> {
                viewModelScope.launch {
                    event.pagerState.scrollToPage(event.page)
                }
            }

            is ReaderEvent.OnMoveBookToAlreadyRead -> {
                viewModelScope.launch {
                    onEvent(
                        ReaderEvent.OnGoBack(
                            event.context,
                            event.navigator,
                            event.listState,
                            refreshList = {},
                            navigate = {}
                        )
                    )

                    val firstVisibleItemIndex = event.listState.firstVisibleItemIndex
                    val firstVisibleItemOffset = event.listState.firstVisibleItemScrollOffset

                    _state.update {
                        it.copy(
                            book = it.book.copy(
                                category = Category.ALREADY_READ,
                                progress = 1f,
                                scrollIndex = firstVisibleItemIndex,
                                scrollOffset = firstVisibleItemOffset
                            )
                        )
                    }
                    updateBooks.execute(listOf(_state.value.book))

                    event.onUpdateCategories(
                        _state.value.book
                    )
                    event.updatePage(
                        Category.entries.dropLastWhile {
                            it != Category.ALREADY_READ
                        }.size - 1
                    )
                    event.navigator.navigate(Screen.LIBRARY, true)
                }
            }

            is ReaderEvent.OnTranslateText -> {
                viewModelScope.launch {
                    val translatorIntent = Intent()
                    val browserIntent = Intent()

                    translatorIntent.type = "text/plain"
                    translatorIntent.action = Intent.ACTION_PROCESS_TEXT

                    browserIntent.action = Intent.ACTION_WEB_SEARCH

                    translatorIntent.putExtra(Intent.EXTRA_PROCESS_TEXT, event.textToTranslate)
                    translatorIntent.putExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, true)

                    browserIntent.putExtra(
                        SearchManager.QUERY,
                        "translate: ${event.textToTranslate.trim()}"
                    )

                    if (translatorIntent.resolveActivity(event.context.packageManager) != null) {
                        event.context.startActivity(translatorIntent)
                        return@launch
                    }

                    if (browserIntent.resolveActivity(event.context.packageManager) != null) {
                        event.context.startActivity(browserIntent)
                        return@launch
                    }

                    event.noAppsFound()
                }
            }

            is ReaderEvent.OnOpenDictionary -> {
                viewModelScope.launch {
                    val browserIntent = Intent()

                    browserIntent.action = Intent.ACTION_WEB_SEARCH
                    browserIntent.putExtra(
                        SearchManager.QUERY,
                        "dictionary" +
                                ": ${event.textToDefine.trim()}"
                    )

                    if (browserIntent.resolveActivity(event.context.packageManager) != null) {
                        event.context.startActivity(browserIntent)
                        return@launch
                    }

                    event.noAppsFound()
                }
            }
        }
    }

    fun init(
        navigator: Navigator,
        context: ComponentActivity,
        listState: LazyListState,
        refreshList: (Book) -> Unit,
        onLoaded: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val bookId = navigator.retrieveArgument("book") as? Int

            if (bookId == null) {
                navigator.navigateBack()
                return@launch
            }

            val book = getBooksById.execute(listOf(bookId))

            if (book.isEmpty()) {
                navigator.navigateBack()
                return@launch
            }

            _state.update {
                ReaderState(book = book.first())
            }

            viewModelScope.launch {
                onEvent(ReaderEvent.OnShowHideMenu(false, context))
            }

            onEvent(
                ReaderEvent.OnLoadText(
                    refreshList = { refreshList(it) },
                    listState = listState,
                    onLoaded = {
                        onLoaded()
                    },
                    onTextIsEmpty = {
                        onEvent(ReaderEvent.OnTextIsEmpty(onLoaded = { onLoaded() }))
                    }
                )
            )
        }
    }
}













