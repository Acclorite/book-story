package ua.acclorite.book_story.presentation.screens.reader.data

import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshotFlow
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
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
import ua.acclorite.book_story.domain.model.Chapter
import ua.acclorite.book_story.domain.use_case.book.CheckForTextUpdate
import ua.acclorite.book_story.domain.use_case.book.GetBookById
import ua.acclorite.book_story.domain.use_case.book.GetText
import ua.acclorite.book_story.domain.use_case.book.UpdateBook
import ua.acclorite.book_story.domain.use_case.history.GetLatestHistory
import ua.acclorite.book_story.domain.util.Resource
import ua.acclorite.book_story.domain.util.UIText
import ua.acclorite.book_story.domain.util.UIViewModel
import ua.acclorite.book_story.presentation.core.navigation.Screen
import ua.acclorite.book_story.presentation.core.util.coerceAndPreventNaN
import ua.acclorite.book_story.presentation.core.util.launchActivity
import javax.inject.Inject
import kotlin.math.roundToInt

@Suppress("LABEL_NAME_CLASH")
@HiltViewModel
class ReaderViewModel @Inject constructor(
    private val updateBook: UpdateBook,
    private val getText: GetText,
    private val getLatestHistory: GetLatestHistory,
    private val getBookById: GetBookById,
    private val checkForTextUpdate: CheckForTextUpdate
) : UIViewModel<ReaderState, ReaderEvent>() {

    companion object {
        @Composable
        fun getState() = getState<ReaderViewModel, ReaderState, ReaderEvent>()

        @Composable
        fun getEvent() = getEvent<ReaderViewModel, ReaderState, ReaderEvent>()
    }

    private val _state = MutableStateFlow(ReaderState())
    override val state = _state.asStateFlow()

    private var eventJob = SupervisorJob()
    private var scrollJob: Job? = null
    private var updateJob: Job? = null

    override fun onEvent(event: ReaderEvent) {
        viewModelScope.launch(eventJob + Dispatchers.Main) {
            when (event) {
                is ReaderEvent.OnInit -> init(event)

                is ReaderEvent.OnUpdateProgress -> updateProgress(event)

                is ReaderEvent.OnClearViewModel -> clearViewModel()

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
                                book = it.book.copy(
                                    lastOpened = getLatestHistory.execute(_state.value.book.id)?.time
                                ),
                                text = text
                            )
                        }

                        updateBook.execute(_state.value.book)
                        event.refreshList(_state.value.book)

                        launch {
                            snapshotFlow {
                                _state.value.listState.layoutInfo.totalItemsCount
                            }.collectLatest { itemsCount ->
                                if (itemsCount < _state.value.text.size) return@collectLatest

                                _state.value.book.apply {
                                    _state.value.listState.requestScrollToItem(
                                        scrollIndex,
                                        scrollOffset
                                    )
                                    updateChapter(index = scrollIndex)
                                }

                                event.checkForUpdate()

                                _state.update {
                                    it.copy(
                                        loading = false,
                                        errorMessage = null
                                    )
                                }

                                return@collectLatest
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
                                showMenu = shouldShow,
                                checkpoint = _state.value.listState.run {
                                    if (!shouldShow || !event.saveCheckpoint) return@run it.checkpoint

                                    firstVisibleItemIndex to firstVisibleItemScrollOffset
                                }
                            )
                        }
                    }
                }

                is ReaderEvent.OnRestoreCheckpoint -> {
                    launch {
                        _state.value.listState.requestScrollToItem(
                            _state.value.checkpoint.first,
                            _state.value.checkpoint.second
                        )

                        onEvent(
                            ReaderEvent.OnChangeProgress(
                                progress = calculateProgress(_state.value.checkpoint.first),
                                firstVisibleItemIndex = _state.value.checkpoint.first,
                                firstVisibleItemOffset = _state.value.checkpoint.second,
                                refreshList = { book ->
                                    event.refreshList(book)
                                }
                            )
                        )
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

                            updateBook.execute(_state.value.book)
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
                    scrollJob?.cancel()
                    scrollJob = launch {
                        delay(300)

                        yield()

                        val scrollTo = (_state.value.text.size * event.progress).roundToInt()
                        _state.value.listState.requestScrollToItem(scrollTo)
                    }
                }

                is ReaderEvent.OnScrollToChapter -> {
                    launch {
                        _state.value.listState.requestScrollToItem(
                            event.chapterStartIndex
                        )
                        updateChapter(index = event.chapterStartIndex)
                        onEvent(
                            ReaderEvent.OnChangeProgress(
                                progress = calculateProgress(event.chapterStartIndex),
                                firstVisibleItemIndex = event.chapterStartIndex,
                                firstVisibleItemOffset = 0,
                                refreshList = event.refreshList
                            )
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

                        updateBook.execute(_state.value.book)
                        event.refreshList(_state.value.book)
                    }
                }

                is ReaderEvent.OnShowHideSettingsBottomSheet -> {
                    _state.update {
                        it.copy(
                            showSettingsBottomSheet = event.show
                        )
                    }
                }

                is ReaderEvent.OnShowHideChaptersDrawer -> {
                    _state.update {
                        it.copy(
                            showChaptersDrawer = event.show
                        )
                    }
                }

                is ReaderEvent.OnScrollToSettingsPage -> {
                    launch {
                        event.pagerState?.requestScrollToPage(event.page)

                        _state.update {
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

                is ReaderEvent.OnCheckTextForUpdate -> {
                    updateJob?.cancel()
                    updateJob = launch(Dispatchers.IO) {
                        _state.update {
                            it.copy(
                                checkingForUpdate = true,
                                updateFound = false,
                                showUpdateDialog = false
                            )
                        }

                        yield()

                        val result = checkForTextUpdate.execute(bookId = _state.value.book.id)

                        yield()

                        when (result) {
                            is Resource.Success -> {
                                if (result.data == null) {
                                    withContext(Dispatchers.Main) {
                                        event.noUpdateFound()
                                    }
                                    _state.update {
                                        it.copy(
                                            checkingForUpdate = false,
                                            updateFound = false
                                        )
                                    }
                                    return@launch
                                } else {
                                    _state.update {
                                        it.copy(
                                            updateFound = true,
                                            checkingForUpdate = false
                                        )
                                    }
                                    onEvent(ReaderEvent.OnShowHideUpdateDialog(true))
                                    return@launch
                                }
                            }

                            is Resource.Error -> {
                                _state.update {
                                    it.copy(
                                        checkingForUpdate = false,
                                        updateFound = false
                                    )
                                }
                                return@launch
                            }
                        }
                    }
                }

                is ReaderEvent.OnShowHideUpdateDialog -> {
                    _state.update {
                        it.copy(
                            showUpdateDialog = event.show
                        )
                    }
                }

                is ReaderEvent.OnCancelCheckTextForUpdate -> {
                    _state.update {
                        updateJob?.cancel()

                        it.copy(
                            showUpdateDialog = false,
                            checkingForUpdate = false
                        )
                    }
                }

                is ReaderEvent.OnUpdateText -> {
                    launch {
                        showSystemBars(
                            show = true,
                            activity = event.activity
                        )
                        event.onNavigate {
                            navigate(
                                Screen.BookInfo(
                                    bookId = _state.value.book.id,
                                    startUpdate = true
                                ),
                                useBackAnimation = true,
                                saveInBackStack = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun init(event: ReaderEvent.OnInit) {
        viewModelScope.launch(Dispatchers.IO) {
            val book = getBookById.execute(event.screen.bookId)

            if (book == null) {
                event.navigateBack()
                return@launch
            }

            _state.update {
                ReaderState(book = book)
            }

            clear()
            withContext(Dispatchers.Main) {
                onEvent(
                    ReaderEvent.OnShowHideMenu(
                        show = false,
                        fullscreenMode = event.fullscreenMode,
                        saveCheckpoint = false,
                        activity = event.activity
                    )
                )
            }

            onEvent(
                ReaderEvent.OnLoadText(
                    checkForUpdate = {
                        if (event.checkForTextUpdate) {
                            onEvent(
                                ReaderEvent.OnCheckTextForUpdate {
                                    event.checkForTextUpdateToast()
                                }
                            )
                        }
                    },
                    refreshList = { event.refreshList(it) },
                    onError = {
                        event.onError(it)
                    },
                    onTextIsEmpty = {
                        onEvent(ReaderEvent.OnTextIsEmpty)
                    }
                )
            )
        }
    }

    @OptIn(FlowPreview::class)
    private fun updateProgress(event: ReaderEvent.OnUpdateProgress) {
        viewModelScope.launch(Dispatchers.Main) {
            snapshotFlow {
                _state.value.listState.run { firstVisibleItemIndex to firstVisibleItemScrollOffset }
            }.distinctUntilChanged().debounce(300).collectLatest { (index, offset) ->
                val progress = calculateProgress(index)
                if (progress == _state.value.book.progress) return@collectLatest
                val (currentChapter, currentChapterProgress) = calculateCurrentChapter(index)

                _state.update {
                    it.copy(
                        book = it.book.copy(
                            progress = progress,
                            scrollIndex = index,
                            scrollOffset = offset
                        ),
                        currentChapter = currentChapter,
                        currentChapterProgress = currentChapterProgress
                    )
                }

                updateBook.execute(_state.value.book)
                event.refreshList(_state.value.book)
            }
        }
    }

    private fun clearViewModel() {
        viewModelScope.launch(Dispatchers.Main) {
            _state.update {
                ReaderState()
            }

            eventJob.cancel()
            eventJob = SupervisorJob()
        }
    }

    private fun updateChapter(index: Int) {
        val (currentChapter, currentChapterProgress) = calculateCurrentChapter(index)
        _state.update {
            it.copy(
                currentChapter = currentChapter,
                currentChapterProgress = currentChapterProgress
            )
        }
    }

    private fun calculateCurrentChapter(index: Int): Pair<Chapter?, Float> {
        val currentChapter = _state.value.book.chapters.find { chapter ->
            index in chapter.startIndex..chapter.endIndex
        }
        val currentChapterProgress = currentChapter.run {
            if (this == null) return@run 0f

            val currentIndex = index - startIndex
            val endIndex = endIndex - startIndex
            (currentIndex / endIndex.toFloat())
        }.coerceAndPreventNaN()

        return currentChapter to currentChapterProgress
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

            return@run (firstVisibleItemIndex ?: listState.firstVisibleItemIndex)
                .div(text.lastIndex.toFloat())
                .coerceAndPreventNaN()
        }
    }

    private fun showSystemBars(
        show: Boolean,
        fullscreenMode: Boolean = true,
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

    private fun clear() {
        eventJob.cancel()
        eventJob = SupervisorJob()
    }
}