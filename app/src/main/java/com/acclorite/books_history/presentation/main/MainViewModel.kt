package com.acclorite.books_history.presentation.main

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acclorite.books_history.domain.use_case.ChangeLanguage
import com.acclorite.books_history.domain.use_case.GetDatastore
import com.acclorite.books_history.domain.use_case.SetDatastore
import com.acclorite.books_history.ui.DarkTheme
import com.acclorite.books_history.ui.Theme
import com.acclorite.books_history.ui.toDarkTheme
import com.acclorite.books_history.ui.toTheme
import com.acclorite.books_history.util.Constants
import com.acclorite.books_history.util.DataStoreConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

    private val _updating = MutableStateFlow(false)
    val updating = _updating.asStateFlow()

    /* -- Language ----------------------------------------------------- */
    private var _language: String =
        stateHandle[Constants.LANGUAGE] ?: Locale.getDefault().language
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
        stateHandle[Constants.FONT] ?: Constants.FONTS[0].fontName
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
                viewModelScope.launch(Dispatchers.IO) {
                    _updating.update { true }

                    changeLanguage.execute(event.lang, event.activity)
                    _language = event.lang
                    delay(100)

                    _updating.update { false }
                }
            }

            is MainEvent.OnLocaleUpdate -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _updating.update { true }
                    changeLanguage.execute(_language, event.activity)
                    delay(10)
                    _updating.update { false }
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
        }
    }

    fun init(activity: ComponentActivity) {
        val isDataReady = combine(
            language, theme, darkTheme, showStartScreen, backgroundColor,
            fontColor, fontFamily, isItalic, fontSize, lineHeight,
            paragraphHeight, paragraphIndentation
        ) { values ->
            values.all { it != null }
        }

        viewModelScope.launch {
            isDataReady.collect { bool ->
                if (bool) {
                    _isReady.value = true
                }
            }
        }

        // Language
        viewModelScope.launch(Dispatchers.IO) {
            getDatastore
                .execute(DataStoreConstants.LANGUAGE, _language)
                .first {
                    onEvent(MainEvent.OnChangeLanguage(it, activity))
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
                    setDatastore.execute(DataStoreConstants.BACKGROUND_COLOR, it)
                    _backgroundColor = it

                    true
                }
        }

        // Font Color
        viewModelScope.launch(Dispatchers.IO) {
            getDatastore
                .execute(DataStoreConstants.FONT_COLOR, _fontColor)
                .first {
                    setDatastore.execute(DataStoreConstants.FONT_COLOR, it)
                    _fontColor = it

                    true
                }
        }

        // Font Family
        viewModelScope.launch(Dispatchers.IO) {
            getDatastore
                .execute(DataStoreConstants.FONT, _fontFamily)
                .first {
                    setDatastore.execute(DataStoreConstants.FONT, it)
                    _fontFamily = Constants.FONTS.find { font -> font.fontName == it }?.fontName
                        ?: Constants.FONTS[0].fontName

                    true
                }
        }

        // Font Style
        viewModelScope.launch(Dispatchers.IO) {
            getDatastore
                .execute(DataStoreConstants.IS_ITALIC, _isItalic)
                .first {
                    setDatastore.execute(DataStoreConstants.IS_ITALIC, it)
                    _isItalic = it

                    true
                }
        }

        // Font Size
        viewModelScope.launch(Dispatchers.IO) {
            getDatastore
                .execute(DataStoreConstants.FONT_SIZE, _fontSize)
                .first {
                    setDatastore.execute(DataStoreConstants.FONT_SIZE, it)
                    _fontSize = it

                    it > 0
                }
        }

        // Line Height
        viewModelScope.launch(Dispatchers.IO) {
            getDatastore
                .execute(DataStoreConstants.LINE_HEIGHT, _lineHeight)
                .first {
                    setDatastore.execute(DataStoreConstants.LINE_HEIGHT, it)
                    _lineHeight = it

                    true
                }
        }

        // Paragraph Height
        viewModelScope.launch(Dispatchers.IO) {
            getDatastore
                .execute(DataStoreConstants.PARAGRAPH_HEIGHT, _paragraphHeight)
                .first {
                    setDatastore.execute(DataStoreConstants.PARAGRAPH_HEIGHT, it)
                    _paragraphHeight = it

                    true
                }
        }

        // Paragraph Indentation
        viewModelScope.launch(Dispatchers.IO) {
            getDatastore
                .execute(DataStoreConstants.PARAGRAPH_INDENTATION, _paragraphIndentation)
                .first {
                    setDatastore.execute(DataStoreConstants.PARAGRAPH_INDENTATION, it)
                    _paragraphIndentation = it

                    true
                }
        }

    }

}