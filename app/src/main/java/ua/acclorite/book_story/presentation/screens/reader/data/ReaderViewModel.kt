package ua.acclorite.book_story.presentation.screens.reader.data

import android.app.SearchManager
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.ExperimentalFoundationApi
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
import ua.acclorite.book_story.domain.model.History
import ua.acclorite.book_story.domain.model.StringWithId
import ua.acclorite.book_story.domain.use_case.GetBooksById
import ua.acclorite.book_story.domain.use_case.GetText
import ua.acclorite.book_story.domain.use_case.InsertHistory
import ua.acclorite.book_story.domain.use_case.UpdateBooks
import ua.acclorite.book_story.presentation.data.Argument
import ua.acclorite.book_story.presentation.data.Navigator
import ua.acclorite.book_story.presentation.data.Screen
import ua.acclorite.book_story.util.UIText
import java.util.Date
import javax.inject.Inject
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@HiltViewModel
class ReaderViewModel @Inject constructor(
    private val updateBooks: UpdateBooks,
    private val insertHistory: InsertHistory,
    private val getText: GetText,
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
                    val text = getText.execute(_state.value.book.id!!)

                    if (text.isBlank()) {
                        event.onTextIsEmpty()
                    }

                    val time = Date().time
                    val letters = text
                        .replace("\n", "")
                        .length
                    val words = text
                        .replace("\n", " ")
                        .split("\\s+".toRegex())
                        .size

                    _state.update {
                        it.copy(
                            book = it.book.copy(
                                text = text
                                    .split("\n")
                                    .map { line -> StringWithId(line.trim()) },
                                lastOpened = time
                            ),
                            letters = letters,
                            words = words
                        )
                    }

                    updateBooks.execute(
                        listOf(_state.value.book)
                    )
                    insertHistory.execute(
                        listOf(
                            History(
                                null,
                                _state.value.book.id!!,
                                time
                            )
                        )
                    )
                    event.refreshList(_state.value.book)
                    event.navigator.putArgument(
                        Argument("book", _state.value.book.id)
                    )

                    viewModelScope.launch {
                        snapshotFlow {
                            event.scrollState.layoutInfo.totalItemsCount
                        }.collectLatest { itemsCount ->
                            val scrollTo =
                                (_state.value.book.text.size * _state.value.book.progress)
                                    .toInt()

                            if (itemsCount >= _state.value.book.text.size) {
                                if (scrollTo > 0) {
                                    var loaded = false
                                    for (i in 1..100) {
                                        try {
                                            event.scrollState.scrollToItem(scrollTo)
                                            loaded = true
                                            break
                                        } catch (e: Exception) {
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
                        Argument("book", _state.value.book.id)
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
                        category = Category.ALREADY_READ,
                        progress = 1f
                    )
                    updateBooks.execute(listOf(book))

                    event.refreshList()

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
        scrollState: LazyListState,
        refreshList: (Book) -> Unit,
        onLoaded: () -> Unit
    ) {
        viewModelScope.launch {
            val bookIndex = navigator.retrieveArgument("book") as? Int

            if (bookIndex == null || bookIndex < 0) {
                navigator.navigateBack()
                return@launch
            }

            val book = getBooksById.execute(listOf(bookIndex)).first()

            _state.update {
                ReaderState(book = book)
            }

            onEvent(ReaderEvent.OnShowHideMenu(false, context))
            onEvent(
                ReaderEvent.OnLoadText(
                    scrollState,
                    navigator,
                    refreshList = { refreshList(it) },
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













