package ua.acclorite.book_story.ui.reader

import android.app.SearchManager
import android.content.Intent
import android.net.Uri
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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.reader.Chapter
import ua.acclorite.book_story.domain.reader.Checkpoint
import ua.acclorite.book_story.domain.ui.UIText
import ua.acclorite.book_story.domain.use_case.book.CheckForTextUpdate
import ua.acclorite.book_story.domain.use_case.book.GetBookById
import ua.acclorite.book_story.domain.use_case.book.GetText
import ua.acclorite.book_story.domain.use_case.book.UpdateBook
import ua.acclorite.book_story.domain.use_case.history.GetLatestHistory
import ua.acclorite.book_story.domain.util.Resource
import ua.acclorite.book_story.presentation.core.util.coerceAndPreventNaN
import ua.acclorite.book_story.presentation.core.util.launchActivity
import ua.acclorite.book_story.presentation.core.util.setBrightness
import ua.acclorite.book_story.presentation.core.util.showToast
import ua.acclorite.book_story.ui.history.HistoryScreen
import ua.acclorite.book_story.ui.library.LibraryScreen
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class ReaderModel @Inject constructor(
    private val getBookById: GetBookById,
    private val updateBook: UpdateBook,
    private val getText: GetText,
    private val getLatestHistory: GetLatestHistory,
    private val checkForTextUpdate: CheckForTextUpdate
) : ViewModel() {

    private val mutex = Mutex()

    private val _state = MutableStateFlow(ReaderState())
    val state = _state.asStateFlow()

    private var eventJob = SupervisorJob()

    private var scrollJob: Job? = null
    private var updateJob: Job? = null

    fun onEvent(event: ReaderEvent) {
        viewModelScope.launch(eventJob + Dispatchers.Main) {
            when (event) {
                is ReaderEvent.OnLoadText -> {
                    launch(Dispatchers.IO) {
                        val text = getText.execute(_state.value.book.textPath)

                        if (text.isEmpty()) {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = UIText.StringResource(R.string.error_no_text)
                                )
                            }
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

                        LibraryScreen.refreshListChannel.trySend(0)
                        HistoryScreen.refreshListChannel.trySend(0)

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

                                if (event.checkForTextUpdate) {
                                    onEvent(
                                        ReaderEvent.OnCheckTextForUpdate(
                                            showToast = event.checkForTextUpdateToast,
                                            context = event.context
                                        )
                                    )
                                }

                                _state.update {
                                    it.copy(
                                        isLoading = false,
                                        errorMessage = null
                                    )
                                }

                                return@collectLatest
                            }
                        }
                    }
                }

                is ReaderEvent.OnMenuVisibility -> {
                    launch {
                        if (_state.value.lockMenu) {
                            return@launch
                        }

                        val shouldShow = event.show

                        yield()

                        systemBarsVisibility(
                            show = shouldShow,
                            fullscreenMode = event.fullscreenMode,
                            activity = event.activity
                        )
                        _state.update {
                            it.copy(
                                showMenu = shouldShow,
                                checkpoint = _state.value.listState.run {
                                    if (!shouldShow || !event.saveCheckpoint) return@run it.checkpoint

                                    Checkpoint(firstVisibleItemIndex, firstVisibleItemScrollOffset)
                                }
                            )
                        }
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

                        LibraryScreen.refreshListChannel.trySend(300)
                        HistoryScreen.refreshListChannel.trySend(300)
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
                                firstVisibleItemOffset = 0
                            )
                        )
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

                is ReaderEvent.OnRestoreCheckpoint -> {
                    launch {
                        _state.value.apply {
                            listState.requestScrollToItem(
                                checkpoint.index,
                                checkpoint.offset
                            )

                            onEvent(
                                ReaderEvent.OnChangeProgress(
                                    progress = calculateProgress(checkpoint.index),
                                    firstVisibleItemIndex = checkpoint.index,
                                    firstVisibleItemOffset = checkpoint.offset,
                                )
                            )
                        }
                    }
                }

                is ReaderEvent.OnLeave -> {
                    launch {
                        yield()

                        _state.update {
                            it.copy(
                                lockMenu = true
                            )
                        }

                        _state.value.listState.apply {
                            if (
                                _state.value.isLoading
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

                            LibraryScreen.refreshListChannel.trySend(0)
                            HistoryScreen.refreshListChannel.trySend(0)
                        }

                        WindowCompat.getInsetsController(
                            event.activity.window,
                            event.activity.window.decorView
                        ).show(WindowInsetsCompat.Type.systemBars())
                        event.activity.setBrightness(brightness = null)

                        event.navigate()
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

                        translatorIntent.launchActivity(
                            activity = event.activity,
                            createChooser = !event.translateWholeParagraph,
                            success = {
                                return@launch
                            }
                        )
                        browserIntent.launchActivity(
                            activity = event.activity,
                            success = {
                                return@launch
                            }
                        )

                        withContext(Dispatchers.Main) {
                            event.activity.getString(R.string.error_no_translator)
                                .showToast(context = event.activity, longToast = false)
                        }
                    }
                }

                is ReaderEvent.OnOpenShareApp -> {
                    launch {
                        val shareIntent = Intent()

                        shareIntent.action = Intent.ACTION_SEND
                        shareIntent.type = "text/plain"
                        shareIntent.putExtra(
                            Intent.EXTRA_SUBJECT,
                            event.activity.getString(R.string.app_name)
                        )
                        shareIntent.putExtra(
                            Intent.EXTRA_TEXT,
                            event.textToShare.trim()
                        )

                        shareIntent.launchActivity(
                            activity = event.activity,
                            createChooser = true,
                            success = {
                                return@launch
                            }
                        )

                        withContext(Dispatchers.Main) {
                            event.activity.getString(R.string.error_no_share_app)
                                .showToast(context = event.activity, longToast = false)
                        }
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

                        browserIntent.launchActivity(
                            activity = event.activity,
                            success = {
                                return@launch
                            }
                        )

                        withContext(Dispatchers.Main) {
                            event.activity.getString(R.string.error_no_browser)
                                .showToast(context = event.activity, longToast = false)
                        }
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

                        dictionaryIntent.launchActivity(
                            activity = event.activity,
                            createChooser = true,
                            success = {
                                return@launch
                            }
                        )

                        browserIntent.launchActivity(
                            activity = event.activity,
                            success = {
                                return@launch
                            }
                        )

                        withContext(Dispatchers.Main) {
                            event.activity.getString(R.string.error_no_dictionary)
                                .showToast(context = event.activity, longToast = false)
                        }
                    }
                }

                is ReaderEvent.OnCheckTextForUpdate -> {
                    updateJob?.cancel()
                    updateJob = launch(Dispatchers.IO) {
                        _state.update {
                            it.copy(
                                checkingForUpdate = true,
                                updateFound = false,
                                dialog = null
                            )
                        }

                        yield()

                        val result = checkForTextUpdate.execute(bookId = _state.value.book.id)

                        yield()

                        when (result) {
                            is Resource.Success -> {
                                if (result.data == null) {
                                    withContext(Dispatchers.Main) {
                                        if (event.showToast) {
                                            event.context.getString(R.string.nothing_changed)
                                                .showToast(
                                                    context = event.context,
                                                    longToast = false
                                                )
                                        }
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
                                            dialog = ReaderScreen.UPDATE_DIALOG,
                                            updateFound = true,
                                            checkingForUpdate = false
                                        )
                                    }
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

                is ReaderEvent.OnUpdateText -> {
                    launch {
                        systemBarsVisibility(
                            show = true,
                            fullscreenMode = true,
                            activity = event.activity
                        )
                        event.navigateToBookInfo()
                    }
                }

                is ReaderEvent.OnCancelCheckForTextUpdate -> {
                    _state.update {
                        updateJob?.cancel()

                        it.copy(
                            dialog = null,
                            checkingForUpdate = false
                        )
                    }
                }

                is ReaderEvent.OnShowUpdateDialog -> {
                    _state.update {
                        it.copy(
                            dialog = ReaderScreen.UPDATE_DIALOG
                        )
                    }
                }

                is ReaderEvent.OnDismissDialog -> {
                    _state.update {
                        it.copy(
                            dialog = null
                        )
                    }
                }

                is ReaderEvent.OnShowSettingsBottomSheet -> {
                    _state.update {
                        it.copy(
                            bottomSheet = ReaderScreen.SETTINGS_BOTTOM_SHEET,
                            drawer = null
                        )
                    }
                }

                is ReaderEvent.OnDismissBottomSheet -> {
                    _state.update {
                        it.copy(
                            bottomSheet = null
                        )
                    }
                }

                is ReaderEvent.OnShowChaptersDrawer -> {
                    _state.update {
                        it.copy(
                            drawer = ReaderScreen.CHAPTERS_DRAWER,
                            bottomSheet = null
                        )
                    }
                }

                is ReaderEvent.OnDismissDrawer -> {
                    _state.update {
                        it.copy(
                            drawer = null
                        )
                    }
                }
            }
        }
    }

    fun init(
        bookId: Int,
        fullscreenMode: Boolean,
        checkForTextUpdate: Boolean,
        checkForTextUpdateToast: Boolean,
        activity: ComponentActivity,
        navigateBack: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val book = getBookById.execute(bookId)

            if (book == null) {
                navigateBack()
                return@launch
            }

            eventJob.cancel()
            eventJob.join()
            eventJob = SupervisorJob()

            _state.update {
                ReaderState(book = book)
            }

            onEvent(
                ReaderEvent.OnMenuVisibility(
                    show = false,
                    fullscreenMode = fullscreenMode,
                    saveCheckpoint = false,
                    activity = activity
                )
            )
            onEvent(
                ReaderEvent.OnLoadText(
                    checkForTextUpdate = checkForTextUpdate,
                    checkForTextUpdateToast = checkForTextUpdateToast,
                    context = activity
                )
            )
        }
    }

    @OptIn(FlowPreview::class)
    fun updateProgress(listState: LazyListState) {
        viewModelScope.launch(Dispatchers.Main) {
            snapshotFlow {
                listState.run { firstVisibleItemIndex to firstVisibleItemScrollOffset }
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

                LibraryScreen.refreshListChannel.trySend(0)
                HistoryScreen.refreshListChannel.trySend(0)
            }
        }
    }

    private fun updateChapter(index: Int) {
        viewModelScope.launch {
            val (currentChapter, currentChapterProgress) = calculateCurrentChapter(index)
            _state.update {
                it.copy(
                    currentChapter = currentChapter,
                    currentChapterProgress = currentChapterProgress
                )
            }
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
                isLoading ||
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

    private fun systemBarsVisibility(
        show: Boolean,
        fullscreenMode: Boolean,
        activity: ComponentActivity
    ) {
        WindowCompat.getInsetsController(
            activity.window,
            activity.window.decorView
        ).apply {
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            if (show || !fullscreenMode) show(WindowInsetsCompat.Type.systemBars())
            else hide(WindowInsetsCompat.Type.systemBars())
        }
    }

    fun resetScreen() {
        viewModelScope.launch(Dispatchers.Main) {
            eventJob.cancel()
            eventJob = SupervisorJob()
        }
    }

    private suspend inline fun <T> MutableStateFlow<T>.update(function: (T) -> T) {
        mutex.withLock {
            this.value = function(this.value)
        }
    }
}