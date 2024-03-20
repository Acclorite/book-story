package ua.acclorite.book_story.presentation.data

import android.os.Build
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.acclorite.book_story.domain.use_case.ChangeLanguage
import ua.acclorite.book_story.domain.use_case.GetDatastore
import ua.acclorite.book_story.domain.use_case.SetDatastore
import ua.acclorite.book_story.domain.util.Constants
import ua.acclorite.book_story.domain.util.DataStoreConstants
import ua.acclorite.book_story.presentation.screens.library.data.LibraryViewModel
import ua.acclorite.book_story.presentation.ui.DarkTheme
import ua.acclorite.book_story.presentation.ui.Theme
import ua.acclorite.book_story.presentation.ui.toDarkTheme
import ua.acclorite.book_story.presentation.ui.toTheme
import java.util.Locale
import javax.inject.Inject

/**
 * Stores all variables such as theme, language etc
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle,

    private val getDatastore: GetDatastore,
    private val setDatastore: SetDatastore,
    private val changeLanguage: ChangeLanguage,
) : ViewModel() {

    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    private val _state: MutableStateFlow<MainState> = MutableStateFlow(
        stateHandle[Constants.MAIN_STATE] ?: MainState()
    )
    val state = _state.asStateFlow()

    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.OnChangeLanguage -> {
                viewModelScope.launch(Dispatchers.Main) {
                    changeLanguage.execute(event.lang)
                    _state.updateWithSavedHandle {
                        it.copy(
                            language = event.lang
                        )
                    }
                }
            }

            is MainEvent.OnChangeDarkTheme -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.DARK_THEME, event.darkTheme)
                    _state.updateWithSavedHandle {
                        it.copy(
                            darkTheme = event.darkTheme.toDarkTheme()
                        )
                    }
                }
            }

            is MainEvent.OnChangeTheme -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.THEME, event.theme)
                    _state.updateWithSavedHandle {
                        it.copy(
                            theme = event.theme.toTheme()
                        )
                    }
                }
            }

            is MainEvent.OnChangeFontFamily -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.FONT, event.fontFamily)
                    _state.updateWithSavedHandle {
                        it.copy(
                            fontFamily = Constants.FONTS.find { font -> font.id == event.fontFamily }?.id
                                ?: Constants.FONTS[0].id
                        )
                    }
                }
            }

            is MainEvent.OnChangeFontStyle -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.IS_ITALIC, event.fontStyle)
                    _state.updateWithSavedHandle {
                        it.copy(
                            isItalic = event.fontStyle
                        )
                    }
                }
            }

            is MainEvent.OnChangeFontSize -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.FONT_SIZE, event.fontSize)
                    _state.updateWithSavedHandle {
                        it.copy(
                            fontSize = event.fontSize
                        )
                    }
                }
            }

            is MainEvent.OnChangeLineHeight -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.LINE_HEIGHT, event.lineHeight)
                    _state.updateWithSavedHandle {
                        it.copy(
                            lineHeight = event.lineHeight
                        )
                    }
                }
            }

            is MainEvent.OnChangeParagraphHeight -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.PARAGRAPH_HEIGHT, event.paragraphHeight)
                    _state.updateWithSavedHandle {
                        it.copy(
                            paragraphHeight = event.paragraphHeight
                        )
                    }
                }
            }

            is MainEvent.OnChangeParagraphIndentation -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.PARAGRAPH_INDENTATION, event.bool)
                    _state.updateWithSavedHandle {
                        it.copy(
                            paragraphIndentation = event.bool
                        )
                    }
                }
            }

            is MainEvent.OnChangeBackgroundColor -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.BACKGROUND_COLOR, event.color)
                    _state.updateWithSavedHandle {
                        it.copy(
                            backgroundColor = event.color
                        )
                    }
                }
            }

            is MainEvent.OnChangeFontColor -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.FONT_COLOR, event.color)
                    _state.updateWithSavedHandle {
                        it.copy(
                            fontColor = event.color
                        )
                    }
                }
            }

            is MainEvent.OnChangeShowStartScreen -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.SHOW_START_SCREEN, event.bool)
                    _state.updateWithSavedHandle {
                        it.copy(
                            showStartScreen = event.bool
                        )
                    }
                }
            }
        }
    }

    fun init(
        libraryViewModel: LibraryViewModel,
    ) {
        val isViewModelsReady = combine(
            libraryViewModel.isReady,
        ) { values ->
            values.all { it }
        }

        val isDataReady = combine(
            _state
        ) {
            val value = it.first()

            return@combine value.language != null &&
                    value.theme != null &&
                    value.darkTheme != null &&
                    value.fontFamily != null &&
                    value.isItalic != null &&
                    value.fontSize != null &&
                    value.lineHeight != null &&
                    value.paragraphHeight != null &&
                    value.paragraphIndentation != null &&
                    value.backgroundColor != null &&
                    value.fontColor != null &&
                    value.showStartScreen != null
        }

        val isReady = combine(
            isViewModelsReady,
            isDataReady
        ) { values ->
            values.all { it }
        }

        viewModelScope.launch {
            isReady.collect { bool ->
                if (bool) {
                    _isReady.update {
                        true
                    }
                    return@collect
                }
            }
        }

        // Language
        viewModelScope.launch(Dispatchers.Main) {
            getDatastore
                .execute(
                    DataStoreConstants.LANGUAGE,
                    if (Constants.LANGUAGES.any { Locale.getDefault().language.take(2) == it.first }) {
                        Locale.getDefault().language.take(2)
                    } else {
                        "en"
                    }
                )
                .first {
                    onEvent(MainEvent.OnChangeLanguage(it))
                    it.isNotBlank()
                }
        }

        // Theme
        viewModelScope.launch(Dispatchers.IO) {
            getDatastore
                .execute(
                    DataStoreConstants.THEME,
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) Theme.DYNAMIC.name
                    else Theme.BLUE.name
                )
                .first {
                    onEvent(MainEvent.OnChangeTheme(it))
                    it.isNotBlank()
                }
        }

        // Dark Theme
        viewModelScope.launch(Dispatchers.IO) {
            getDatastore
                .execute(DataStoreConstants.DARK_THEME, DarkTheme.FOLLOW_SYSTEM.name)
                .first {
                    onEvent(MainEvent.OnChangeDarkTheme(it))
                    it.isNotBlank()
                }
        }

        // Show Start Screen
        viewModelScope.launch(Dispatchers.IO) {
            getDatastore
                .execute(DataStoreConstants.SHOW_START_SCREEN, true)
                .first {
                    onEvent(MainEvent.OnChangeShowStartScreen(it))
                    true
                }
        }

        // Background Color
        viewModelScope.launch(Dispatchers.IO) {
            getDatastore
                .execute(DataStoreConstants.BACKGROUND_COLOR, Color.DarkGray.value.toLong())
                .first {
                    onEvent(MainEvent.OnChangeBackgroundColor(it))
                    true
                }
        }

        // Font Color
        viewModelScope.launch(Dispatchers.IO) {
            getDatastore
                .execute(DataStoreConstants.FONT_COLOR, Color.LightGray.value.toLong())
                .first {
                    onEvent(MainEvent.OnChangeFontColor(it))
                    true
                }
        }

        // Font Family
        viewModelScope.launch(Dispatchers.IO) {
            getDatastore
                .execute(DataStoreConstants.FONT, Constants.FONTS[0].id)
                .first {
                    onEvent(MainEvent.OnChangeFontFamily(it))
                    true
                }
        }

        // Font Style
        viewModelScope.launch(Dispatchers.IO) {
            getDatastore
                .execute(DataStoreConstants.IS_ITALIC, false)
                .first {
                    onEvent(MainEvent.OnChangeFontStyle(it))
                    true
                }
        }

        // Font Size
        viewModelScope.launch(Dispatchers.IO) {
            getDatastore
                .execute(DataStoreConstants.FONT_SIZE, 16)
                .first {
                    onEvent(MainEvent.OnChangeFontSize(it))
                    it > 0
                }
        }

        // Line Height
        viewModelScope.launch(Dispatchers.IO) {
            getDatastore
                .execute(DataStoreConstants.LINE_HEIGHT, 4)
                .first {
                    onEvent(MainEvent.OnChangeLineHeight(it))
                    true
                }
        }

        // Paragraph Height
        viewModelScope.launch(Dispatchers.IO) {
            getDatastore
                .execute(DataStoreConstants.PARAGRAPH_HEIGHT, 8)
                .first {
                    onEvent(MainEvent.OnChangeParagraphHeight(it))
                    true
                }
        }

        // Paragraph Indentation
        viewModelScope.launch(Dispatchers.IO) {
            getDatastore
                .execute(DataStoreConstants.PARAGRAPH_INDENTATION, false)
                .first {
                    onEvent(MainEvent.OnChangeParagraphIndentation(it))
                    true
                }
        }

    }

    /**
     * Updates [MutableStateFlow] along with [SavedStateHandle].
     */
    private fun <T> MutableStateFlow<T>.updateWithSavedHandle(
        const: String = Constants.MAIN_STATE,
        function: (T) -> T
    ) {
        val nextValue = function(value)
        update {
            stateHandle[const] = nextValue
            nextValue
        }
    }
}