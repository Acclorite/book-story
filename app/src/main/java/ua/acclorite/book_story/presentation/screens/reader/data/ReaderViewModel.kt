package ua.acclorite.book_story.presentation.screens.reader.data

import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.compose.runtime.snapshotFlow
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
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
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.use_case.GetBookById
import ua.acclorite.book_story.domain.use_case.GetLatestHistory
import ua.acclorite.book_story.domain.use_case.GetText
import ua.acclorite.book_story.domain.use_case.UpdateBooks
import ua.acclorite.book_story.domain.util.OnNavigate
import ua.acclorite.book_story.domain.util.UIText
import ua.acclorite.book_story.presentation.core.navigation.Screen
import ua.acclorite.book_story.presentation.core.util.BaseViewModel
import ua.acclorite.book_story.presentation.core.util.launchActivity
import javax.inject.Inject
import kotlin.math.roundToInt

@Suppress("LABEL_NAME_CLASH")
@HiltViewModel
class ReaderViewModel @Inject constructor(
    private val updateBooks: UpdateBooks,
    private val getText: GetText,
    private val getLatestHistory: GetLatestHistory,
    private val getBookById: GetBookById
) : BaseViewModel<ReaderState, ReaderEvent>() {

    private val _state = MutableStateFlow(ReaderState())
    override val state = _state.asStateFlow()

    private var eventJob = SupervisorJob()

    override fun onEvent(event: ReaderEvent) {
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

                        _state.update {
                            it.copy(
                                text = text
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
                    launch {
                        if (_state.value.lockMenu) {
                            return@launch
                        }

                        val shouldShow = event.show ?: !_state.value.showMenu

                        yield()

                        showSystemBars(
                            show = shouldShow,
                            fullscreenMode = event.fullscreenMode,
                            activity = event.activity
                        )
                        _state.update {
                            it.copy(
                                showMenu = shouldShow
                            )
                        }
                    }
                }

                is ReaderEvent.OnGoBack -> {
                    launch {
                        yield()

                        _state.update {
                            it.copy(
                                lockMenu = true
                            )
                        }

                        _state.value.listState.apply {
                            if (
                                _state.value.loading
                                || layoutInfo.totalItemsCount < 1
                                || _state.value.text.isEmpty()
                            ) return@apply

                            _state.update {
                                it.copy(
                                    book = it.book.copy(
                                        progress = calculateProgress(),
                                        scrollIndex = firstVisibleItemIndex,
                                        scrollOffset = firstVisibleItemScrollOffset
                                    )
                                )
                            }

                            updateBooks.execute(
                                listOf(_state.value.book)
                            )
                            event.refreshList(_state.value.book)
                        }

                        WindowCompat.getInsetsController(
                            event.context.window,
                            event.context.window.decorView
                        ).show(WindowInsetsCompat.Type.systemBars())

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
                                showSettingsBottomSheet = !it.showSettingsBottomSheet
                            )
                        }
                    }
                }

                is ReaderEvent.OnScrollToSettingsPage -> {
                    launch {
                        _state.update {
                            event.pagerState?.scrollToPage(event.page)

                            it.copy(
                                currentPage = event.page
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
        fullscreenMode: Boolean,
        activity: ComponentActivity,
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
                withContext(Dispatchers.Main) {
                    activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                }

                onEvent(
                    ReaderEvent.OnShowHideMenu(
                        show = false,
                        fullscreenMode = fullscreenMode,
                        activity = activity
                    )
                )
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
    fun onUpdateProgress(refreshList: (Book) -> Unit) {
        viewModelScope.launch {
            snapshotFlow {
                _state.value.listState.firstVisibleItemIndex to _state.value.listState.firstVisibleItemScrollOffset
            }
                .distinctUntilChanged()
                .debounce(300)
                .collectLatest { (firstVisibleItemIndex, firstVisibleItemOffset) ->
                    calculateProgress(firstVisibleItemIndex).apply {
                        if (this == _state.value.book.progress) return@apply

                        onEvent(
                            ReaderEvent.OnChangeProgress(
                                progress = this,
                                firstVisibleItemIndex = firstVisibleItemIndex,
                                firstVisibleItemOffset = firstVisibleItemOffset,
                                refreshList = { book ->
                                    refreshList(book)
                                }
                            )
                        )
                    }
                }
        }
    }

    private fun calculateProgress(firstVisibleItemIndex: Int? = null): Float {
        return _state.value.run {
            if (
                loading ||
                listState.layoutInfo.totalItemsCount == 0 ||
                text.isEmpty()
            ) {
                return book.progress
            }

            if ((firstVisibleItemIndex ?: listState.firstVisibleItemIndex) == 0) {
                return 0f
            }

            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.last().index
            if (lastVisibleItemIndex >= text.lastIndex) {
                return 1f
            }

            return@run (firstVisibleItemIndex ?: listState.firstVisibleItemIndex) /
                    (text.lastIndex).toFloat()
        }
    }

    private fun showSystemBars(
        show: Boolean,
        fullscreenMode: Boolean,
        activity: ComponentActivity
    ): Boolean {
        WindowCompat.getInsetsController(
            activity.window,
            activity.window.decorView
        ).apply {
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            if (show || !fullscreenMode) show(WindowInsetsCompat.Type.systemBars())
            else hide(WindowInsetsCompat.Type.systemBars())
        }

        return show || !fullscreenMode
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