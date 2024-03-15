package ua.acclorite.book_story.presentation.data

import android.os.Build
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.acclorite.book_story.domain.use_case.ChangeLanguage
import ua.acclorite.book_story.domain.use_case.GetDatastore
import ua.acclorite.book_story.domain.use_case.SetDatastore
import ua.acclorite.book_story.presentation.screens.library.data.LibraryViewModel
import ua.acclorite.book_story.ui.DarkTheme
import ua.acclorite.book_story.ui.Theme
import ua.acclorite.book_story.ui.toDarkTheme
import ua.acclorite.book_story.ui.toTheme
import ua.acclorite.book_story.util.Constants
import ua.acclorite.book_story.util.DataStoreConstants
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

    /* -- Language ----------------------------------------------------- */
    private var _language: String =
        stateHandle[Constants.LANGUAGE]
            ?: if (
                Constants.LANGUAGES.any { Locale.getDefault().language.take(2) == it.first }
            ) {
                Locale.getDefault().language.take(2)
            } else {
                "en"
            }
        set(value) {
            field = value
            stateHandle[Constants.LANGUAGE] = value
        }
    val language: StateFlow<String?>
        get() = stateHandle.getStateFlow(Constants.LANGUAGE, null)

    /* -- Theme -------------------------------------------------------- */
    private var _theme: Theme =
        stateHandle[Constants.THEME]
            ?: if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) Theme.DYNAMIC else Theme.BLUE
        set(value) {
            field = value
            stateHandle[Constants.THEME] = value
        }
    val theme: StateFlow<Theme?>
        get() = stateHandle.getStateFlow(Constants.THEME, null)

    /* -- Dark Theme --------------------------------------------------- */
    private var _darkTheme: DarkTheme =
        stateHandle[Constants.DARK_THEME] ?: DarkTheme.FOLLOW_SYSTEM
        set(value) {
            field = value
            stateHandle[Constants.DARK_THEME] = value
        }
    val darkTheme: StateFlow<DarkTheme?>
        get() = stateHandle.getStateFlow(Constants.DARK_THEME, null)

    /* -- Show Start Screen -------------------------------------------- */
    private var _showStartScreen: Boolean =
        stateHandle[Constants.SHOW_START_SCREEN] ?: true
        set(value) {
            field = value
            stateHandle[Constants.SHOW_START_SCREEN] = value
        }
    val showStartScreen: StateFlow<Boolean?>
        get() = stateHandle.getStateFlow(Constants.SHOW_START_SCREEN, null)

    /* -- Background Color --------------------------------------------- */
    private var _backgroundColor: Long =
        stateHandle[Constants.BACKGROUND_COLOR] ?: Color.DarkGray.value.toLong()
        set(value) {
            field = value
            stateHandle[Constants.BACKGROUND_COLOR] = value
        }
    val backgroundColor: StateFlow<Long?>
        get() = stateHandle.getStateFlow(Constants.BACKGROUND_COLOR, null)

    /* -- Font Color --------------------------------------------------- */
    private var _fontColor: Long =
        stateHandle[Constants.FONT_COLOR] ?: Color.LightGray.value.toLong()
        set(value) {
            field = value
            stateHandle[Constants.FONT_COLOR] = value
        }
    val fontColor: StateFlow<Long?>
        get() = stateHandle.getStateFlow(Constants.FONT_COLOR, null)

    /* -- Font Family -------------------------------------------------- */
    private var _fontFamily: String =
        stateHandle[Constants.FONT] ?: Constants.FONTS[0].id
        set(value) {
            field = value
            stateHandle[Constants.FONT] = value
        }
    val fontFamily: StateFlow<String?>
        get() = stateHandle.getStateFlow(Constants.FONT, null)

    /* -- Is Italic ---------------------------------------------------- */
    private var _isItalic: Boolean =
        stateHandle[Constants.IS_ITALIC] ?: false
        set(value) {
            field = value
            stateHandle[Constants.IS_ITALIC] = value
        }
    val isItalic: StateFlow<Boolean?>
        get() = stateHandle.getStateFlow(Constants.IS_ITALIC, null)

    /* -- Font Size ---------------------------------------------------- */
    private var _fontSize: Int =
        stateHandle[Constants.FONT_SIZE] ?: 16
        set(value) {
            field = value
            stateHandle[Constants.FONT_SIZE] = value
        }
    val fontSize: StateFlow<Int?>
        get() = stateHandle.getStateFlow(Constants.FONT_SIZE, null)

    /* -- Line Height -------------------------------------------------- */
    private var _lineHeight: Int =
        stateHandle[Constants.LINE_HEIGHT] ?: 4
        set(value) {
            field = value
            stateHandle[Constants.LINE_HEIGHT] = value
        }
    val lineHeight: StateFlow<Int?>
        get() = stateHandle.getStateFlow(Constants.LINE_HEIGHT, null)

    /* -- Paragraph Height --------------------------------------------- */
    private var _paragraphHeight: Int =
        stateHandle[Constants.PARAGRAPH_HEIGHT] ?: 8
        set(value) {
            field = value
            stateHandle[Constants.PARAGRAPH_HEIGHT] = value
        }
    val paragraphHeight: StateFlow<Int?>
        get() = stateHandle.getStateFlow(Constants.PARAGRAPH_HEIGHT, null)

    /* -- Paragraph Indentation ---------------------------------------- */
    private var _paragraphIndentation: Boolean =
        stateHandle[Constants.PARAGRAPH_INDENTATION] ?: false
        set(value) {
            field = value
            stateHandle[Constants.PARAGRAPH_INDENTATION] = value
        }
    val paragraphIndentation: StateFlow<Boolean?>
        get() = stateHandle.getStateFlow(Constants.PARAGRAPH_INDENTATION, null)
    /* --------------------------------------------------------------- */

    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.OnChangeLanguage -> {
                viewModelScope.launch(Dispatchers.Main) {
                    changeLanguage.execute(event.lang)
                    _language = event.lang
                }
            }

            is MainEvent.OnChangeDarkTheme -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.DARK_THEME, event.darkTheme)
                    _darkTheme = event.darkTheme.toDarkTheme()
                }
            }

            is MainEvent.OnChangeTheme -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.THEME, event.theme)
                    _theme = event.theme.toTheme()
                }
            }

            is MainEvent.OnChangeFontFamily -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.FONT, event.fontFamily)
                    _fontFamily = Constants.FONTS.find { font -> font.id == event.fontFamily }?.id
                        ?: Constants.FONTS[0].id
                }
            }

            is MainEvent.OnChangeFontStyle -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.IS_ITALIC, event.fontStyle)
                    _isItalic = event.fontStyle
                }
            }

            is MainEvent.OnChangeFontSize -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.FONT_SIZE, event.fontSize)
                    _fontSize = event.fontSize
                }
            }

            is MainEvent.OnChangeLineHeight -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.LINE_HEIGHT, event.lineHeight)
                    _lineHeight = event.lineHeight
                }
            }

            is MainEvent.OnChangeParagraphHeight -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.PARAGRAPH_HEIGHT, event.paragraphHeight)
                    _paragraphHeight = event.paragraphHeight
                }
            }

            is MainEvent.OnChangeParagraphIndentation -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.PARAGRAPH_INDENTATION, event.bool)
                    _paragraphIndentation = event.bool
                }
            }

            is MainEvent.OnChangeBackgroundColor -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.BACKGROUND_COLOR, event.color)
                    _backgroundColor = event.color
                }
            }

            is MainEvent.OnChangeFontColor -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.FONT_COLOR, event.color)
                    _fontColor = event.color
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
            language,
            theme,
            darkTheme,
            showStartScreen,
            backgroundColor,
            fontColor,
            fontFamily,
            isItalic,
            fontSize,
            lineHeight,
            paragraphHeight,
            paragraphIndentation
        ) { values ->
            values.all { it != null }
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
                .execute(DataStoreConstants.LANGUAGE, _language)
                .first {
                    onEvent(MainEvent.OnChangeLanguage(it))
                    it.isNotBlank()
                }
        }

        // Theme
        viewModelScope.launch(Dispatchers.IO) {
            getDatastore
                .execute(DataStoreConstants.THEME, _theme.toString())
                .first {
                    onEvent(MainEvent.OnChangeTheme(it))
                    it.isNotBlank()
                }
        }

        // Dark Theme
        viewModelScope.launch(Dispatchers.IO) {
            getDatastore
                .execute(DataStoreConstants.DARK_THEME, _darkTheme.toString())
                .first {
                    onEvent(MainEvent.OnChangeDarkTheme(it))
                    it.isNotBlank()
                }
        }

        // Show Start Screen
        viewModelScope.launch(Dispatchers.IO) {
            getDatastore
                .execute(DataStoreConstants.SHOW_START_SCREEN, _showStartScreen)
                .first {
                    setDatastore.execute(DataStoreConstants.SHOW_START_SCREEN, it)
                    _showStartScreen = it

                    true
                }
        }

        // Background Color
        viewModelScope.launch(Dispatchers.IO) {
            getDatastore
                .execute(DataStoreConstants.BACKGROUND_COLOR, _backgroundColor)
                .first {
                    onEvent(MainEvent.OnChangeBackgroundColor(it))
                    true
                }
        }

        // Font Color
        viewModelScope.launch(Dispatchers.IO) {
            getDatastore
                .execute(DataStoreConstants.FONT_COLOR, _fontColor)
                .first {
                    onEvent(MainEvent.OnChangeFontColor(it))
                    true
                }
        }

        // Font Family
        viewModelScope.launch(Dispatchers.IO) {
            getDatastore
                .execute(DataStoreConstants.FONT, _fontFamily)
                .first {
                    onEvent(MainEvent.OnChangeFontFamily(it))
                    true
                }
        }

        // Font Style
        viewModelScope.launch(Dispatchers.IO) {
            getDatastore
                .execute(DataStoreConstants.IS_ITALIC, _isItalic)
                .first {
                    onEvent(MainEvent.OnChangeFontStyle(it))
                    true
                }
        }

        // Font Size
        viewModelScope.launch(Dispatchers.IO) {
            getDatastore
                .execute(DataStoreConstants.FONT_SIZE, _fontSize)
                .first {
                    onEvent(MainEvent.OnChangeFontSize(it))
                    it > 0
                }
        }

        // Line Height
        viewModelScope.launch(Dispatchers.IO) {
            getDatastore
                .execute(DataStoreConstants.LINE_HEIGHT, _lineHeight)
                .first {
                    onEvent(MainEvent.OnChangeLineHeight(it))
                    true
                }
        }

        // Paragraph Height
        viewModelScope.launch(Dispatchers.IO) {
            getDatastore
                .execute(DataStoreConstants.PARAGRAPH_HEIGHT, _paragraphHeight)
                .first {
                    onEvent(MainEvent.OnChangeParagraphHeight(it))
                    true
                }
        }

        // Paragraph Indentation
        viewModelScope.launch(Dispatchers.IO) {
            getDatastore
                .execute(DataStoreConstants.PARAGRAPH_INDENTATION, _paragraphIndentation)
                .first {
                    onEvent(MainEvent.OnChangeParagraphIndentation(it))
                    true
                }
        }

    }

}