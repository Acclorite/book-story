package ua.acclorite.book_story.presentation.screens.reader.data

import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.snapshotFlow
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.model.Category
import ua.acclorite.book_story.domain.use_case.GetBookById
import ua.acclorite.book_story.domain.use_case.GetLatestHistory
import ua.acclorite.book_story.domain.use_case.GetText
import ua.acclorite.book_story.domain.use_case.UpdateBooks
import ua.acclorite.book_story.domain.util.OnNavigate
import ua.acclorite.book_story.domain.util.UIText
import ua.acclorite.book_story.presentation.data.Screen
import ua.acclorite.book_story.presentation.data.launchActivity
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import javax.inject.Inject
import kotlin.math.roundToInt

@Suppress("LABEL_NAME_CLASH")
@HiltViewModel
class ReaderViewModel @Inject constructor(
    private val updateBooks: UpdateBooks,
    private val getText: GetText,
    private val getLatestHistory: GetLatestHistory,
    private val getBookById: GetBookById
) : ViewModel() {

    private val _state = MutableStateFlow(ReaderState())
    val state = _state.asStateFlow()

    private var eventJob = SupervisorJob()

    fun onEvent(event: ReaderEvent) {
        viewModelScope.launch(eventJob + Dispatchers.Main) {
            when (event) {
                is ReaderEvent.OnTextIsEmpty -> {
                    _state.update {
                        it.copy(
                            loading = false,
                            errorMessage = UIText.StringResource(R.string.error_no_text)
                        )
                    }
                }

                is ReaderEvent.OnLoadText -> {
                    launch(Dispatchers.IO) {
                        val text = getText.execute(_state.value.book.textPath)

                        if (text.isEmpty()) {
                            event.onTextIsEmpty()
                        }

                        val textAsLine = text
                            .joinToString(
                                separator = "\n"
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

                        launch {
                            snapshotFlow {
                                _state.value.listState.layoutInfo.totalItemsCount
                            }.collectLatest { itemsCount ->
                                val index = _state.value.book.scrollIndex
                                val offset = _state.value.book.scrollOffset

                                if (itemsCount >= _state.value.text.size) {
                                    if (index > 0 || offset > 0) {
                                        var loaded = false
                                        for (i in 1..100) {
                                            try {
                                                _state.value.listState.requestScrollToItem(
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
                                    _state.update {
                                        it.copy(
                                            loading = false
                                        )
                                    }

                                    return@collectLatest
                                }
                            }
                        }
                    }
                }

                is ReaderEvent.OnShowHideMenu -> {
                    if (_state.value.lockMenu) {
                        return@launch
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
                    launch {
                        _state.update {
                            it.copy(
                                lockMenu = true
                            )
                        }

                        val insetsController = WindowCompat.getInsetsController(
                            event.context.window,
                            event.context.window.decorView
                        )

                        val listState = _state.value.listState
                        if (
                            !_state.value.loading &&
                            listState.layoutInfo.totalItemsCount != 0 &&
                            _state.value.text.isNotEmpty()
                        ) {
                            _state.update {
                                it.copy(
                                    book = it.book.copy(
                                        progress = calculateProgress(),
                                        scrollIndex = listState.firstVisibleItemIndex,
                                        scrollOffset = listState.firstVisibleItemScrollOffset
                                    )
                                )
                            }

                            updateBooks.execute(
                                listOf(_state.value.book)
                            )
                            event.refreshList(_state.value.book)
                        }

                        insetsController.show(WindowInsetsCompat.Type.systemBars())
                        event.navigate()
                    }
                }

                is ReaderEvent.OnScroll -> {
                    launch {
                        val scrollTo = (_state.value.text.size * event.progress).roundToInt()

                        _state.value.listState.scrollToItem(
                            scrollTo
                        )
                    }
                }

                is ReaderEvent.OnChangeProgress -> {
                    launch(Dispatchers.IO) {
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
                        event.refreshList(_state.value.book)
                    }
                }

                is ReaderEvent.OnShowHideSettingsBottomSheet -> {
                    launch(Dispatchers.IO) {
                        _state.update {
                            it.copy(
                                currentPage = if (it.showSettingsBottomSheet) it.currentPage else 0,
                                showSettingsBottomSheet = !it.showSettingsBottomSheet
                            )
                        }
                    }
                }

                is ReaderEvent.OnScrollToSettingsPage -> {
                    launch {
                        event.pagerState.scrollToPage(event.page)
                    }
                }

                is ReaderEvent.OnMoveBookToAlreadyRead -> {
                    launch {
                        onEvent(
                            ReaderEvent.OnGoBack(
                                event.context,
                                refreshList = {},
                                navigate = {}
                            )
                        )

                        _state.update {
                            val listState = it.listState
                            it.copy(
                                book = it.book.copy(
                                    category = Category.ALREADY_READ,
                                    progress = 1f,
                                    scrollIndex = listState.firstVisibleItemIndex,
                                    scrollOffset = listState.firstVisibleItemScrollOffset
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

                        event.onNavigate {
                            navigate(
                                Screen.Library,
                                useBackAnimation = true
                            )
                        }
                    }
                }

                is ReaderEvent.OnOpenTranslator -> {
                    launch {
                        val translatorIntent = Intent()
                        val browserIntent = Intent()

                        translatorIntent.type = "text/plain"
                        translatorIntent.action = Intent.ACTION_PROCESS_TEXT

                        browserIntent.action = Intent.ACTION_WEB_SEARCH

                        translatorIntent.putExtra(
                            Intent.EXTRA_PROCESS_TEXT,
                            event.textToTranslate
                        )
                        translatorIntent.putExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, true)

                        browserIntent.putExtra(
                            SearchManager.QUERY,
                            "translate: ${event.textToTranslate.trim()}"
                        )

                        var translatorFailure = false
                        translatorIntent.launchActivity(
                            event.context,
                            createChooser = !event.translateWholeParagraph
                        ) {
                            translatorFailure = true
                        }
                        if (!translatorFailure) {
                            return@launch
                        }

                        var browserFailure = false
                        browserIntent.launchActivity(event.context) {
                            browserFailure = true
                        }
                        if (!browserFailure) {
                            return@launch
                        }

                        event.noAppsFound()
                    }
                }

                is ReaderEvent.OnOpenShareApp -> {
                    launch {
                        val shareIntent = Intent()

                        shareIntent.action = Intent.ACTION_SEND
                        shareIntent.type = "text/plain"
                        shareIntent.putExtra(
                            Intent.EXTRA_SUBJECT,
                            event.context.getString(R.string.app_name)
                        )
                        shareIntent.putExtra(
                            Intent.EXTRA_TEXT,
                            event.textToShare.trim()
                        )

                        var shareFailure = false
                        shareIntent.launchActivity(event.context, createChooser = true) {
                            shareFailure = true
                        }
                        if (!shareFailure) {
                            return@launch
                        }

                        event.noAppsFound()
                    }
                }

                is ReaderEvent.OnOpenWebBrowser -> {
                    launch {
                        val browserIntent = Intent()

                        browserIntent.action = Intent.ACTION_WEB_SEARCH
                        browserIntent.putExtra(
                            SearchManager.QUERY,
                            event.textToSearch
                        )

                        var browserFailure = false
                        browserIntent.launchActivity(event.context) {
                            browserFailure = true
                        }
                        if (!browserFailure) {
                            return@launch
                        }

                        event.noAppsFound()
                    }
                }

                is ReaderEvent.OnOpenDictionary -> {
                    launch {
                        val dictionaryIntent = Intent()
                        val browserIntent = Intent()

                        dictionaryIntent.type = "text/plain"
                        dictionaryIntent.action = Intent.ACTION_PROCESS_TEXT
                        dictionaryIntent.putExtra(
                            Intent.EXTRA_PROCESS_TEXT,
                            event.textToDefine.trim()
                        )
                        dictionaryIntent.putExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, true)

                        browserIntent.action = Intent.ACTION_VIEW
                        val text = event.textToDefine.trim().replace(" ", "+")
                        browserIntent.data = Uri.parse("https://www.onelook.com/?w=$text")

                        var dictionaryFailure = false
                        dictionaryIntent.launchActivity(event.context, createChooser = true) {
                            dictionaryFailure = true
                        }
                        if (!dictionaryFailure) {
                            return@launch
                        }

                        var browserFailure = false
                        browserIntent.launchActivity(event.context) {
                            browserFailure = true
                        }
                        if (!browserFailure) {
                            return@launch
                        }

                        event.noAppsFound()
                    }
                }
            }
        }
    }

    fun init(
        screen: Screen.Reader,
        onNavigate: OnNavigate,
        context: ComponentActivity,
        refreshList: (Book) -> Unit,
        onError: (UIText) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val book = getBookById.execute(screen.bookId)

            if (book == null) {
                onNavigate {
                    navigateBack()
                }
                return@launch
            }

            _state.update {
                ReaderState(book = book)
            }

            clear()
            launch {
                onEvent(ReaderEvent.OnShowHideMenu(false, context))
                onEvent(
                    ReaderEvent.OnLoadText(
                        refreshList = { refreshList(it) },
                        onError = {
                            onError(it)
                        },
                        onTextIsEmpty = {
                            onEvent(ReaderEvent.OnTextIsEmpty)
                        }
                    )
                )
            }
        }
    }

    @OptIn(FlowPreview::class)
    fun onUpdateProgress(
        onLibraryEvent: (LibraryEvent) -> Unit,
        onHistoryEvent: (HistoryEvent) -> Unit
    ) {
        viewModelScope.launch {
            snapshotFlow {
                _state.value.listState.firstVisibleItemIndex to _state.value.listState.firstVisibleItemScrollOffset
            }
                .distinctUntilChanged()
                .debounce(300)
                .collectLatest { (firstVisibleItemIndex, firstVisibleItemScrollOffset) ->
                    val listState = _state.value.listState
                    if (
                        !_state.value.loading &&
                        _state.value.text.isNotEmpty() &&
                        listState.layoutInfo.totalItemsCount > 0
                    ) {
                        val lastVisibleItemIndex = listState
                            .layoutInfo
                            .visibleItemsInfo
                            .last()
                            .index
                        val totalItemsCount = listState.layoutInfo.totalItemsCount - 1

                        val progress = if (firstVisibleItemIndex > 0) {
                            if (lastVisibleItemIndex >= totalItemsCount) {
                                1f
                            } else {
                                firstVisibleItemIndex / (_state.value.text.size - 1).toFloat()
                            }
                        } else {
                            0f
                        }

                        onEvent(
                            ReaderEvent.OnChangeProgress(
                                progress = progress,
                                firstVisibleItemIndex = firstVisibleItemIndex,
                                firstVisibleItemOffset = firstVisibleItemScrollOffset,
                                refreshList = { book ->
                                    onLibraryEvent(LibraryEvent.OnUpdateBook(book))
                                    onHistoryEvent(HistoryEvent.OnUpdateBook(book))
                                }
                            )
                        )
                    }
                }
        }
    }

    private fun calculateProgress(): Float {
        val listState = _state.value.listState

        if (
            _state.value.loading ||
            listState.layoutInfo.totalItemsCount == 0 ||
            _state.value.text.isEmpty()
        ) {
            return _state.value.book.progress
        }

        val lastVisibleItemIndex = listState.layoutInfo
            .visibleItemsInfo.last().index
        val totalItemsCount = listState.layoutInfo.totalItemsCount - 1

        if (listState.firstVisibleItemIndex == 0) {
            return 0f
        }

        if (lastVisibleItemIndex >= totalItemsCount) {
            return 1f
        }

        return listState.firstVisibleItemIndex / (_state.value.text.size - 1).toFloat()
    }

    private suspend fun clear() {
        eventJob.cancel()
        eventJob.join()
        eventJob = SupervisorJob()
    }

    fun clearViewModel() {
        viewModelScope.launch(Dispatchers.Main) {
            _state.update {
                ReaderState()
            }

            eventJob.cancel()
            eventJob.join()
            eventJob = SupervisorJob()
        }
    }
}