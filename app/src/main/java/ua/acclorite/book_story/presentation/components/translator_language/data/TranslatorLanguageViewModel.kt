package ua.acclorite.book_story.presentation.components.translator_language.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.nl.translate.TranslateLanguage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.Language
import ua.acclorite.book_story.domain.model.LanguageHistory
import ua.acclorite.book_story.domain.use_case.DeleteLanguageModel
import ua.acclorite.book_story.domain.use_case.DownloadLanguageModel
import ua.acclorite.book_story.domain.use_case.IsLanguageModelDownloaded
import ua.acclorite.book_story.domain.util.UIText
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TranslatorLanguageViewModel @Inject constructor(
    private val isLanguageModelDownloaded: IsLanguageModelDownloaded,
    private val downloadLanguageModel: DownloadLanguageModel,
    private val deleteLanguageModel: DeleteLanguageModel
) : ViewModel() {

    private val _state = MutableStateFlow(TranslatorLanguageState())
    val state = _state.asStateFlow()

    private var job: Job? = null

    @Suppress("LABEL_NAME_CLASH")
    fun onEvent(event: TranslatorLanguageEvent) {
        when (event) {
            is TranslatorLanguageEvent.OnSelectLanguage -> {
                viewModelScope.launch(Dispatchers.IO) {
                    if (_state.value.showDownloadLanguageDialog) {
                        return@launch
                    }

                    val isDownloaded = isLanguageModelDownloaded.execute(
                        event.languageCode
                    )

                    _state.update {
                        it.copy(
                            languageToSelect = event.languageCode
                        )
                    }

                    onEvent(
                        TranslatorLanguageEvent.OnUpdateLanguage(event.languageCode) {
                            it.copy(
                                isDownloaded = isDownloaded
                            )
                        }
                    )

                    if (isDownloaded) {
                        val from = _state.value.translateFromSelecting
                        val langToSelect = _state.value.languageToSelect
                        val unselectedLang = _state.value.unselectedLanguage
                        val selectedLang = _state.value.selectedLanguage

                        val translateFrom: String
                        val translateTo: String

                        if (langToSelect == unselectedLang) {
                            if (from) {
                                if (selectedLang == "auto") {
                                    launch(Dispatchers.Main) {
                                        event.onError(
                                            UIText.StringResource(R.string.error_cannot_select_language)
                                        )
                                    }
                                    return@launch
                                }

                                translateFrom = langToSelect
                                translateTo = selectedLang
                            } else {
                                translateTo = langToSelect
                                translateFrom = selectedLang
                            }
                        } else {
                            if (from) {
                                translateFrom = langToSelect
                                translateTo = unselectedLang
                            } else {
                                translateTo = langToSelect
                                translateFrom = unselectedLang
                            }
                        }

                        if (translateTo == "auto") {
                            launch(Dispatchers.Main) {
                                event.onError(
                                    UIText.StringResource(R.string.error_wrong_language)
                                )
                            }
                            return@launch
                        }

                        event.onSelect(
                            translateFrom,
                            translateTo
                        )
                        return@launch
                    }

                    _state.update {
                        it.copy(
                            showDownloadLanguageDialog = true
                        )
                    }
                }
            }

            is TranslatorLanguageEvent.OnDownloadLanguage -> {
                viewModelScope.launch(Dispatchers.IO) {
                    onEvent(
                        TranslatorLanguageEvent.OnUpdateLanguage(event.languageCode) {
                            it.copy(
                                isDownloading = true,
                                isCanceled = false
                            )
                        }
                    )

                    val isDownloaded = isLanguageModelDownloaded.execute(
                        event.languageCode
                    )

                    onEvent(
                        TranslatorLanguageEvent.OnUpdateLanguage(event.languageCode) {
                            it.copy(
                                isDownloaded = isDownloaded
                            )
                        }
                    )

                    if (isDownloaded) {
                        onEvent(
                            TranslatorLanguageEvent.OnUpdateLanguage(event.languageCode) {
                                it.copy(
                                    isDownloading = false
                                )
                            }
                        )
                        return@launch
                    }

                    downloadLanguageModel.execute(
                        languageCode = event.languageCode,
                        onCompleted = {
                            viewModelScope.launch(Dispatchers.IO) {
                                val modelDownloaded = isLanguageModelDownloaded.execute(
                                    event.languageCode
                                )

                                onEvent(
                                    TranslatorLanguageEvent.OnUpdateLanguage(event.languageCode) {
                                        it.copy(
                                            isDownloading = false,
                                            isDownloaded = modelDownloaded
                                        )
                                    }
                                )

                                val isCanceled = _state.value.unfilteredLanguages.find {
                                    it.languageCode == event.languageCode
                                }?.isCanceled

                                if (isCanceled == true && modelDownloaded) {
                                    deleteLanguageModel.execute(
                                        languageCode = event.languageCode
                                    )
                                    onEvent(
                                        TranslatorLanguageEvent.OnUpdateLanguage(event.languageCode) {
                                            it.copy(
                                                isDownloading = false,
                                                isDownloaded = false,
                                                isCanceled = false
                                            )
                                        }
                                    )
                                    return@launch
                                }

                                event.onComplete()
                            }
                        },
                        onFailure = {
                            event.onFailure(it)
                        }
                    )
                }
            }

            is TranslatorLanguageEvent.OnCancelDownload -> {
                viewModelScope.launch(Dispatchers.IO) {
                    onEvent(
                        TranslatorLanguageEvent.OnUpdateLanguage(event.languageCode) {
                            it.copy(
                                isDownloaded = false,
                                isDownloading = false,
                                isCanceled = true
                            )
                        }
                    )
                }
            }

            is TranslatorLanguageEvent.OnDeleteLanguage -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val deleted = deleteLanguageModel.execute(event.languageCode)

                    if (deleted) {
                        onEvent(
                            TranslatorLanguageEvent.OnUpdateLanguage(event.languageCode) {
                                it.copy(
                                    isDownloaded = false,
                                    isDownloading = false
                                )
                            }
                        )
                    }
                }
            }

            is TranslatorLanguageEvent.OnSearchShowHide -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val shouldHide = _state.value.showSearch

                    if (shouldHide) {
                        filterLanguages("")
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

            is TranslatorLanguageEvent.OnSearchQueryChange -> {
                _state.update {
                    it.copy(
                        searchQuery = event.query
                    )
                }
                job?.cancel()
                job = viewModelScope.launch(Dispatchers.IO) {
                    delay(500)
                    yield()
                    onEvent(TranslatorLanguageEvent.OnSearch)
                }
            }

            is TranslatorLanguageEvent.OnSearch -> {
                viewModelScope.launch(Dispatchers.IO) {
                    filterLanguages()
                }
            }

            is TranslatorLanguageEvent.OnRequestFocus -> {
                if (!_state.value.hasFocused) {
                    event.focusRequester.requestFocus()
                    _state.update {
                        it.copy(
                            hasFocused = true
                        )
                    }
                }
            }

            is TranslatorLanguageEvent.OnDismissDownloadDialog -> {
                _state.update {
                    it.copy(
                        showDownloadLanguageDialog = false
                    )
                }
            }

            is TranslatorLanguageEvent.OnUpdateLanguage -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val unfilteredLanguages = _state.value.unfilteredLanguages.map {
                        if (it.languageCode == event.languageCode) {
                            event.block(it)
                        } else {
                            it
                        }
                    }

                    _state.update {
                        it.copy(
                            unfilteredLanguages = unfilteredLanguages
                        )
                    }
                    filterLanguages()
                }
            }
        }
    }

    fun init(
        selectedLanguage: String,
        unselectedLanguage: String,
        translateFromSelecting: Boolean,
        history: List<LanguageHistory>,
        loaded: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val oldState = _state.value
            _state.update {
                TranslatorLanguageState()
            }

            val unfilteredLanguages = TranslateLanguage.getAllLanguages()
                .filter {
                    if (it == "ru") {
                        return@filter false
                    }

                    true
                }
                .map { languageCode ->
                    val previousLanguage = oldState.languages.find {
                        it.languageCode == languageCode
                    }

                    val locale = Locale(languageCode)
                    val displayLanguage = locale.getDisplayLanguage(locale).replaceFirstChar {
                        it.uppercase()
                    }

                    val isDownloading = previousLanguage?.isDownloading ?: false
                    val isCancelled = previousLanguage?.isCanceled ?: false
                    val isSelected = selectedLanguage == languageCode

                    val historyOrder = history.find {
                        it.languageCode == languageCode
                    }?.id

                    Language(
                        languageCode = languageCode,
                        displayLanguage = displayLanguage,
                        isDownloading = isDownloading,
                        isDownloaded = false,
                        isCanceled = isCancelled,
                        isSelected = isSelected,
                        historyOrder = historyOrder
                    )
                }


            _state.update {
                it.copy(
                    selectedLanguage = selectedLanguage,
                    unselectedLanguage = unselectedLanguage,
                    translateFromSelecting = translateFromSelecting,
                    unfilteredLanguages = unfilteredLanguages
                )
            }
            filterLanguages()
            loaded()
            loadDownloadedState()
        }
    }

    private suspend fun loadDownloadedState() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value.unfilteredLanguages.map { language ->
                val isDownloaded = isLanguageModelDownloaded.execute(
                    language.languageCode
                )
                val isDownloading = if (isDownloaded) false else null

                onEvent(
                    TranslatorLanguageEvent.OnUpdateLanguage(
                        languageCode = language.languageCode,
                        block = {
                            it.copy(
                                isDownloaded = isDownloaded,
                                isDownloading = isDownloading ?: it.isDownloading
                            )
                        }
                    )
                )
            }
        }
    }

    private fun filterLanguages(
        query: String = if (_state.value.showSearch) _state.value.searchQuery else ""
    ) {
        _state.update {
            it.copy(
                languages = it.unfilteredLanguages.filter { language ->
                    if (query.isBlank()) {
                        return@filter true
                    }

                    language.displayLanguage.lowercase().trim().contains(
                        query.lowercase().trim()
                    )
                }
            )
        }
    }
}