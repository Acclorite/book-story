package ua.acclorite.book_story.presentation.screens.reader.data

import android.app.SearchManager
import android.content.Intent
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.model.Category
import ua.acclorite.book_story.domain.model.LineWithTranslation
import ua.acclorite.book_story.domain.model.SelectableLanguage
import ua.acclorite.book_story.domain.use_case.DeleteLanguageModel
import ua.acclorite.book_story.domain.use_case.DownloadLanguageModel
import ua.acclorite.book_story.domain.use_case.GetBookById
import ua.acclorite.book_story.domain.use_case.GetLatestHistory
import ua.acclorite.book_story.domain.use_case.GetText
import ua.acclorite.book_story.domain.use_case.IdentifyLanguage
import ua.acclorite.book_story.domain.use_case.IsLanguageModelDownloaded
import ua.acclorite.book_story.domain.use_case.TranslateText
import ua.acclorite.book_story.domain.use_case.UpdateBooks
import ua.acclorite.book_story.domain.util.ID
import ua.acclorite.book_story.domain.util.LanguageCode
import ua.acclorite.book_story.domain.util.Resource
import ua.acclorite.book_story.domain.util.UIText
import ua.acclorite.book_story.presentation.data.Navigator
import ua.acclorite.book_story.presentation.data.Screen
import ua.acclorite.book_story.presentation.data.removeKey
import ua.acclorite.book_story.presentation.data.removeKeys
import ua.acclorite.book_story.presentation.data.update
import ua.acclorite.book_story.presentation.data.updateWithCopy
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import java.util.Locale
import javax.inject.Inject
import kotlin.math.roundToInt

@Suppress("LABEL_NAME_CLASH")
@HiltViewModel
class ReaderViewModel @Inject constructor(
    private val updateBooks: UpdateBooks,
    private val getText: GetText,
    private val getLatestHistory: GetLatestHistory,
    private val getBookById: GetBookById,
    private val identifyLanguage: IdentifyLanguage,
    private val isLanguageModelDownloaded: IsLanguageModelDownloaded,
    private val downloadLanguageModel: DownloadLanguageModel,
    private val deleteLanguageModel: DeleteLanguageModel,
    private val translateText: TranslateText,
) : ViewModel() {

    private val _state = MutableStateFlow(ReaderState())
    val state = _state.asStateFlow()

    private var eventJob = SupervisorJob()

    private var dismissTranslatorJob: Job? = null
    private var cancelTranslationJob: Job? = null
    private var translatorJob: Job? = null
    private var undoTranslationJob: Job? = null
    private var downloadLanguageJob: Job? = null

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
                            .map { it.value.originalLine }
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

                                    if (
                                        _state.value.book.translateWhenOpen &&
                                        _state.value.book.enableTranslator
                                    ) {
                                        onEvent(
                                            ReaderEvent.OnTranslateText(null) {
                                                event.onError(it)
                                            }
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

                is ReaderEvent.OnShowHideTranslatorBottomSheet -> {
                    _state.update {
                        it.copy(
                            showTranslatorBottomSheet = event.show
                                ?: !it.showTranslatorBottomSheet
                        )
                    }
                }

                is ReaderEvent.OnChangeTranslatorSettings -> {
                    launch(Dispatchers.IO) {
                        val book = _state.value.book
                        val updatedBook = book.copy(
                            enableTranslator = event.enableTranslator ?: book.enableTranslator,
                            translateFrom = event.translateFrom ?: book.translateFrom,
                            translateTo = event.translateTo ?: book.translateTo,
                            doubleClickTranslation = event.doubleClickTranslation
                                ?: book.doubleClickTranslation,
                            translateWhenOpen = event.translateWhenOpen
                                ?: book.translateWhenOpen
                        )

                        _state.update {
                            it.copy(
                                book = updatedBook
                            )
                        }
                        updateBooks.execute(listOf(updatedBook))
                    }
                }

                is ReaderEvent.OnShowHideLanguageBottomSheet -> {
                    _state.update {
                        it.copy(
                            showLanguageBottomSheet = event.show ?: !it.showLanguageBottomSheet,
                            languageBottomSheetTranslateFrom = event.translateFrom ?: false
                        )
                    }
                }

                is ReaderEvent.OnTranslateText -> {
                    _state.update {
                        it.copy(
                            isTranslating = true
                        )
                    }

                    launch {
                        joinJobs()

                        translatorJob = launch(Dispatchers.IO) {
                            onEvent(
                                ReaderEvent.OnChangeTranslatorSettings(
                                    enableTranslator = true
                                )
                            )

                            val keys = event.keys ?: _state.value.text.keys
                            var linesToTranslate = if (keys.size >= _state.value.text.size) {
                                _state.value.text
                            } else {
                                _state.value.text.filterKeys { keys.any { key -> key == it } }
                            }

                            val languages = mutableMapOf<LanguageCode, List<ID>?>()

                            yield()
                            linesToTranslate = linesToTranslate.updateWithCopy {
                                it.copy(
                                    isTranslationLoading = true,
                                    isTranslationFailed = false,
                                    errorMessage = null,
                                    useTranslation = false
                                )
                            }

                            yield()
                            onUpdateTextLines(
                                linesToTranslate,
                                wholeText = linesToTranslate.size >= _state.value.text.size
                            )

                            yield()
                            if (_state.value.book.translateFrom == "auto") {
                                val identifiedLanguages =
                                    mutableListOf<Pair<LanguageCode, ID>>()
                                val errors = mutableMapOf<ID, LineWithTranslation>()

                                for (line in linesToTranslate.toMap()) {
                                    yield()
                                    val result =
                                        identifyLanguage.execute(line.value.originalLine)

                                    when (result) {
                                        is Resource.Success -> {
                                            identifiedLanguages.add(
                                                result.data!! to line.key
                                            )
                                        }

                                        is Resource.Error -> {
                                            errors[line.key] = line.value.copy(
                                                isTranslationFailed = true,
                                                isTranslationLoading = false,
                                                errorMessage = result.message
                                            )
                                        }
                                    }
                                }

                                yield()
                                if (errors.isNotEmpty()) {
                                    onUpdateTextLines(
                                        errors,
                                        errors.size >= _state.value.text.size
                                    )
                                    linesToTranslate = linesToTranslate.removeKeys(errors.keys)
                                }

                                yield()
                                languages.putAll(
                                    identifiedLanguages
                                        .groupBy {
                                            it.first
                                        }.mapValues { (_, value) ->
                                            value.map { it.second }
                                        }
                                )
                            } else {
                                languages[_state.value.book.translateFrom] = emptyList()
                            }

                            yield()
                            if (languages.isEmpty()) {
                                _state.update {
                                    it.copy(
                                        isTranslating = false
                                    )
                                }

                                yield()
                                launch(Dispatchers.Main) {
                                    event.error(UIText.StringResource(R.string.error_languages_not_identified))
                                }
                                return@launch
                            }

                            var shouldShowDialog = false

                            if (languages.any { !isLanguageModelDownloaded.execute(it.key) }) {
                                shouldShowDialog = true
                            }

                            if (!isLanguageModelDownloaded.execute(_state.value.book.translateTo)) {
                                shouldShowDialog = true
                            }

                            yield()
                            if (shouldShowDialog) {
                                val languagesToDownload = mutableListOf<SelectableLanguage>()

                                val translateToLocale = Locale(_state.value.book.translateTo)
                                val translateToDisplayLanguage = translateToLocale
                                    .getDisplayLanguage(translateToLocale)
                                    .replaceFirstChar {
                                        it.uppercase()
                                    }

                                val translateToCanceled =
                                    _state.value.languagesToTranslate.find {
                                        it.languageCode == _state.value.book.translateTo
                                    }?.isCanceled ?: false

                                languagesToDownload.add(
                                    SelectableLanguage(
                                        languageCode = _state.value.book.translateTo,
                                        displayLanguage = translateToDisplayLanguage,
                                        isDownloading = false,
                                        isCanceled = translateToCanceled,
                                        isError = false,
                                        isDownloaded = isLanguageModelDownloaded.execute(_state.value.book.translateTo),
                                        isSelected = true,
                                        canUnselect = false,
                                        occurrences = languages[_state.value.book.translateTo]
                                    )
                                )
                                languagesToDownload.addAll(
                                    languages
                                        .filterNot { it.key == _state.value.book.translateTo }
                                        .toList()
                                        .sortedByDescending { it.second?.size }
                                        .mapIndexed { index, language ->
                                            val locale = Locale(language.first)
                                            val displayLanguage = locale
                                                .getDisplayLanguage(locale)
                                                .replaceFirstChar {
                                                    it.uppercase()
                                                }

                                            val isCanceled =
                                                _state.value.languagesToTranslate.find {
                                                    it.languageCode == language.first
                                                }?.isCanceled ?: false

                                            SelectableLanguage(
                                                languageCode = language.first,
                                                displayLanguage = displayLanguage,
                                                isDownloading = false,
                                                isCanceled = isCanceled,
                                                isDownloaded = isLanguageModelDownloaded.execute(
                                                    language.first
                                                ),
                                                isError = false,
                                                isSelected = index == 0,
                                                canUnselect = index > 0,
                                                occurrences = language.second
                                            )
                                        }
                                )

                                yield()
                                _state.update {
                                    it.copy(
                                        languagesToTranslate = languagesToDownload,
                                        showDownloadLanguageDialog = true
                                    )
                                }
                                return@launch
                            }

                            yield()
                            for (i in 0..<linesToTranslate.size) {
                                yield()

                                val closestLine = findClosestLineToTranslate(
                                    linesToTranslate
                                ) ?: continue
                                val line = linesToTranslate[closestLine] ?: continue

                                yield()
                                val sourceLanguage =
                                    if (_state.value.book.translateFrom == "auto") {
                                        when (
                                            val result =
                                                identifyLanguage.execute(line.originalLine)
                                        ) {
                                            is Resource.Success -> result.data!!
                                            is Resource.Error -> continue
                                        }
                                    } else {
                                        _state.value.book.translateFrom
                                    }
                                val targetLanguage = _state.value.book.translateTo

                                yield()
                                if (
                                    line.translatingFrom == sourceLanguage &&
                                    line.translatingTo == targetLanguage &&
                                    line.translatedLine != null
                                ) {
                                    val translatedLine = line.copy(
                                        useTranslation = true,
                                        isTranslationFailed = false,
                                        isTranslationLoading = false,
                                        errorMessage = null
                                    )

                                    yield()
                                    _state.update {
                                        it.copy(
                                            text = it.text.update(
                                                closestLine to translatedLine
                                            )
                                        )
                                    }
                                    linesToTranslate = linesToTranslate.removeKey(closestLine)
                                    continue
                                }

                                var translation: String? = null
                                var error: UIText? = null

                                yield()
                                val result = translateText.execute(
                                    sourceLanguage = sourceLanguage,
                                    targetLanguage = targetLanguage,
                                    text = line.originalLine
                                )

                                when (result) {
                                    is Resource.Success -> {
                                        translation = result.data
                                    }

                                    is Resource.Error -> {
                                        error = result.message
                                    }
                                }

                                val translatedLine = line.copy(
                                    useTranslation = translation != null,
                                    translatedLine = translation,
                                    translatingFrom = sourceLanguage,
                                    translatingTo = targetLanguage,
                                    isTranslationFailed = translation == null,
                                    isTranslationLoading = false,
                                    errorMessage = error
                                )

                                yield()
                                _state.update {
                                    it.copy(
                                        text = it.text.update(
                                            closestLine to translatedLine
                                        )
                                    )
                                }
                                linesToTranslate = linesToTranslate.removeKey(closestLine)
                            }

                            yield()
                            _state.update {
                                it.copy(
                                    isTranslating = false
                                )
                            }
                        }
                    }
                }

                is ReaderEvent.OnDismissDownloadLanguageDialog -> {
                    _state.update {
                        it.copy(
                            showDownloadLanguageDialog = false,
                            isTranslating = false
                        )
                    }

                    launch {
                        joinJobs()

                        dismissTranslatorJob = launch(Dispatchers.IO) {
                            val languagesToDownload = _state.value.languagesToTranslate

                            yield()
                            _state.update {
                                it.copy(
                                    languagesToTranslate = it.languagesToTranslate.map { lang ->
                                        lang.copy(
                                            isCanceled = true,
                                            isDownloading = false,
                                            isError = false
                                        )
                                    },
                                    showDownloadLanguageDialog = false
                                )
                            }

                            yield()

                            downloadLanguageJob?.cancel()
                            val lines = getLinesFromLanguages(languagesToDownload)
                            onUpdateTextLines(
                                lines.updateWithCopy {
                                    it.copy(
                                        isTranslationLoading = false,
                                        useTranslation = false
                                    )
                                },
                                wholeText = lines.size >= _state.value.text.size
                            )
                        }
                    }
                }

                is ReaderEvent.OnCancelTranslation -> {
                    launch {
                        cancelTranslationJob?.join()
                        undoTranslationJob?.join()
                        dismissTranslatorJob?.cancel()
                        translatorJob?.cancel()

                        cancelTranslationJob = launch(Dispatchers.IO) {
                            _state.update {
                                it.copy(
                                    isTranslating = false,
                                    showDownloadLanguageDialog = false
                                )
                            }

                            yield()
                            onUpdateTextLines(
                                _state.value.text.updateWithCopy {
                                    if (it.useTranslation) {
                                        return@updateWithCopy it
                                    }

                                    it.copy(
                                        isTranslationLoading = false,
                                        useTranslation = false
                                    )
                                },
                                wholeText = true
                            )
                        }
                    }
                }

                is ReaderEvent.OnUndoTranslation -> {
                    launch {
                        cancelTranslationJob?.join()
                        undoTranslationJob?.join()

                        cancelTranslationJob = launch(Dispatchers.IO) {
                            yield()

                            if (event.id != null) {
                                _state.update {
                                    val line = it.text[event.id]?.copy(
                                        useTranslation = false,
                                        isTranslationLoading = false
                                    )

                                    if (line == null) {
                                        return@update it
                                    }

                                    it.copy(
                                        text = it.text.update(event.id to line)
                                    )
                                }
                            } else {
                                _state.update {
                                    it.copy(
                                        text = it.text.updateWithCopy { line ->
                                            if (!line.useTranslation) {
                                                return@updateWithCopy line
                                            }

                                            line.copy(
                                                isTranslationLoading = false,
                                                useTranslation = false
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                is ReaderEvent.OnDownloadLanguages -> {
                    launch {
                        downloadLanguageJob?.cancel()

                        downloadLanguageJob = launch(Dispatchers.IO) {
                            _state.update {
                                it.copy(
                                    languagesToTranslate = it.languagesToTranslate.map { lang ->
                                        val isDownloaded = isLanguageModelDownloaded.execute(
                                            lang.languageCode
                                        )

                                        lang.copy(
                                            isDownloading = !isDownloaded && lang.isSelected,
                                            isCanceled = false,
                                            isDownloaded = isDownloaded
                                        )
                                    }
                                )
                            }

                            val shouldDownload = _state.value.languagesToTranslate.filter {
                                it.isSelected
                            }.any {
                                it.isDownloading
                            }

                            if (!shouldDownload) {
                                _state.update {
                                    it.copy(
                                        languagesToTranslate = it.languagesToTranslate.filter { lang ->
                                            lang.isDownloaded
                                        },
                                        showDownloadLanguageDialog = false
                                    )
                                }

                                onUnselectLines()
                                launch(Dispatchers.Main) {
                                    event.onSuccess(getLinesFromLanguages(_state.value.languagesToTranslate))
                                }
                                return@launch
                            }

                            val languagesToDownload = _state.value.languagesToTranslate.filter {
                                it.isSelected
                            }
                            val jobs = languagesToDownload.map { lang ->
                                async {
                                    downloadLanguageModel.execute(
                                        languageCode = lang.languageCode,
                                        onCompleted = {
                                            launch(Dispatchers.IO) {
                                                yield()

                                                val modelDownloaded =
                                                    isLanguageModelDownloaded.execute(lang.languageCode)
                                                if (!modelDownloaded) {
                                                    onEvent(
                                                        ReaderEvent.OnUpdateLanguage(lang.languageCode) {
                                                            it.copy(
                                                                isError = true,
                                                                isDownloading = false,
                                                                isDownloaded = false,
                                                                errorMessage = UIText.StringResource(
                                                                    R.string.error_could_not_download_language
                                                                )
                                                            )
                                                        }
                                                    )
                                                    return@launch
                                                }

                                                onEvent(
                                                    ReaderEvent.OnUpdateLanguage(lang.languageCode) {
                                                        it.copy(
                                                            isDownloading = false,
                                                            isDownloaded = true
                                                        )
                                                    }
                                                )

                                                val isCanceled =
                                                    _state.value.languagesToTranslate.find {
                                                        it.languageCode == lang.languageCode
                                                    }?.isCanceled ?: true

                                                if (isCanceled) {
                                                    deleteLanguageModel.execute(
                                                        languageCode = lang.languageCode
                                                    )
                                                    onEvent(
                                                        ReaderEvent.OnUpdateLanguage(lang.languageCode) {
                                                            it.copy(
                                                                isDownloading = false,
                                                                isDownloaded = false,
                                                                isCanceled = false
                                                            )
                                                        }
                                                    )
                                                    return@launch
                                                }
                                            }
                                        },
                                        onFailure = { error ->
                                            onEvent(
                                                ReaderEvent.OnUpdateLanguage(lang.languageCode) {
                                                    it.copy(
                                                        isError = true,
                                                        isDownloading = false,
                                                        isDownloaded = false,
                                                        errorMessage = UIText.StringResource(
                                                            R.string.error_query,
                                                            error.message ?: ""
                                                        )
                                                    )
                                                }
                                            )
                                        }
                                    )
                                }
                            }
                            jobs.awaitAll()

                            yield()

                            _state.update {
                                it.copy(
                                    languagesToTranslate = it.languagesToTranslate.filter { lang ->
                                        lang.isDownloaded
                                    },
                                    showDownloadLanguageDialog = false
                                )
                            }

                            if (languagesToDownload.isEmpty()) {
                                yield()
                                launch(Dispatchers.Main) {
                                    event.error(
                                        UIText.StringResource(
                                            R.string.error_could_not_download_languages
                                        )
                                    )
                                }
                                onEvent(ReaderEvent.OnDismissDownloadLanguageDialog)
                                return@launch
                            }

                            yield()

                            onUnselectLines()
                            launch(Dispatchers.Main) {
                                event.onSuccess(getLinesFromLanguages(_state.value.languagesToTranslate))
                            }
                        }
                    }
                }

                is ReaderEvent.OnSelectLanguage -> {
                    launch(Dispatchers.IO) {
                        val editedList = _state.value.languagesToTranslate.map {
                            if (it.languageCode == event.language.languageCode) {
                                it.copy(
                                    isSelected = !it.isSelected
                                )
                            } else {
                                it
                            }
                        }

                        _state.update {
                            it.copy(
                                languagesToTranslate = editedList
                            )
                        }
                    }
                }

                is ReaderEvent.OnUpdateLanguage -> {
                    launch(Dispatchers.IO) {
                        val editedList = _state.value.languagesToTranslate.map {
                            if (it.languageCode == event.languageCode) {
                                event.calculation(it)
                            } else {
                                it
                            }
                        }

                        _state.update {
                            it.copy(
                                languagesToTranslate = editedList
                            )
                        }
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
                        event.navigate(event.navigator)
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
                                event.navigator,
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

                        event.navigator.navigate(Screen.LIBRARY, true)
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
                    launch {
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
    }

    private suspend fun joinJobs() {
        cancelTranslationJob?.join()
        translatorJob?.join()
        dismissTranslatorJob?.join()
        undoTranslationJob?.join()
    }

    private suspend fun findClosestLineToTranslate(
        translatingLines: Map<ID, LineWithTranslation>
    ): ID? {
        val lines = translatingLines.toMutableMap()
        val visibleItems = _state.value.listState.layoutInfo.visibleItemsInfo
        val listStateIndex = (visibleItems.getOrNull(visibleItems.size / 2)?.index ?: 0) - 1
            .coerceAtLeast(0)

        var closestLineIndex: Int? = null
        var searchUpperLine = false
        var currentLineIndex = listStateIndex
        val invalidIndices = mutableListOf<Int>()

        while (true) {
            yield()

            if (lines.isEmpty()) {
                break
            }

            currentLineIndex = currentLineIndex.coerceIn(0..lines.maxOf { it.key })

            if (lines[currentLineIndex] == null) {
                invalidIndices.add(currentLineIndex)

                if (searchUpperLine) {
                    val invalid = invalidIndices.filterNot {
                        it >= currentLineIndex
                    }
                    var minus = 1

                    while (true) {
                        if (invalid.any { currentLineIndex - minus == it }) {
                            minus++
                            continue
                        }
                        break
                    }

                    currentLineIndex -= minus
                } else {
                    val invalid = invalidIndices.filterNot {
                        it <= currentLineIndex
                    }
                    var plus = 1

                    while (true) {
                        if (invalid.any { currentLineIndex + plus == it }) {
                            plus++
                            continue
                        }
                        break
                    }

                    currentLineIndex += plus
                }
                searchUpperLine = !searchUpperLine
                continue
            }

            if (
                lines[currentLineIndex]?.useTranslation == true ||
                lines[currentLineIndex]?.isTranslationFailed == true
            ) {
                invalidIndices.add(currentLineIndex)
                lines.remove(currentLineIndex)

                if (searchUpperLine) {
                    val invalid = invalidIndices.filterNot {
                        it >= currentLineIndex
                    }
                    var minus = 1

                    while (true) {
                        if (invalid.any { currentLineIndex - minus == it }) {
                            minus++
                            continue
                        }
                        break
                    }

                    currentLineIndex -= minus
                } else {
                    val invalid = invalidIndices.filterNot {
                        it <= currentLineIndex
                    }
                    var plus = 1

                    while (true) {
                        if (invalid.any { currentLineIndex + plus == it }) {
                            plus++
                            continue
                        }
                        break
                    }

                    currentLineIndex += plus
                }
                searchUpperLine = !searchUpperLine
                continue
            }

            closestLineIndex = currentLineIndex
            break
        }

        yield()
        return closestLineIndex
    }

    private suspend fun onUpdateTextLines(
        newLines: Map<ID, LineWithTranslation>,
        wholeText: Boolean
    ) {
        val newText = if (wholeText) newLines
        else _state.value.text.update(newLines)

        yield()
        _state.update {
            it.copy(
                text = newText
            )
        }
    }

    private suspend fun onUnselectLines() {
        onUpdateTextLines(
            _state.value.text.updateWithCopy {
                it.copy(isTranslationLoading = false)
            },
            wholeText = true
        )
    }

    private suspend fun getLinesFromLanguages(
        languages: List<SelectableLanguage>
    ): Map<ID, LineWithTranslation> {
        val lines = mutableMapOf<ID, LineWithTranslation>()

        yield()
        for (language in languages) {
            yield()

            if (language.occurrences == null) {
                continue
            }

            if (language.occurrences.isEmpty()) {
                lines += _state.value.text
                break
            }

            lines += language.occurrences.associateWith {
                _state.value.text[it]!!
            }
        }
        yield()

        return lines
    }

    fun init(
        navigator: Navigator,
        context: ComponentActivity,
        refreshList: (Book) -> Unit,
        onError: (UIText) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val bookId = navigator.retrieveArgument("book") as? Int

            if (bookId == null) {
                navigator.navigateBack()
                return@launch
            }

            val book = getBookById.execute(bookId)

            if (book == null) {
                navigator.navigateBack()
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