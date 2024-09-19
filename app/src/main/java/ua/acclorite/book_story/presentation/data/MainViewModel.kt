package ua.acclorite.book_story.presentation.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.acclorite.book_story.domain.use_case.ChangeLanguage
import ua.acclorite.book_story.domain.use_case.CheckForUpdates
import ua.acclorite.book_story.domain.use_case.GetAllSettings
import ua.acclorite.book_story.domain.use_case.SetDatastore
import ua.acclorite.book_story.presentation.core.constants.Constants
import ua.acclorite.book_story.presentation.core.constants.DataStoreConstants
import ua.acclorite.book_story.presentation.core.util.BaseViewModel
import ua.acclorite.book_story.presentation.screens.library.data.LibraryViewModel
import ua.acclorite.book_story.presentation.screens.settings.data.SettingsViewModel
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.data.toBrowseLayout
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.data.toBrowseSortOrder
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.data.toFilesStructure
import ua.acclorite.book_story.presentation.screens.settings.nested.reader.data.toTextAlignment
import ua.acclorite.book_story.presentation.ui.toDarkTheme
import ua.acclorite.book_story.presentation.ui.toPureDark
import ua.acclorite.book_story.presentation.ui.toTheme
import ua.acclorite.book_story.presentation.ui.toThemeContrast
import javax.inject.Inject

/**
 * Stores all variables such as theme, language etc
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle,

    private val setDatastore: SetDatastore,
    private val changeLanguage: ChangeLanguage,
    private val checkForUpdates: CheckForUpdates,
    private val getAllSettings: GetAllSettings
) : BaseViewModel<MainState, MainEvent>() {

    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    private val isSettingsReady = MutableStateFlow(false)
    private val isViewModelReady = MutableStateFlow(false)

    private val _state: MutableStateFlow<MainState> = MutableStateFlow(
        stateHandle[Constants.MAIN_STATE] ?: MainState()
    )
    override val state = _state.asStateFlow()

    override fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.OnChangeLanguage -> {
                viewModelScope.launch(Dispatchers.Main) {
                    changeLanguage.execute(event.lang)
                    updateStateWithSavedHandle {
                        it.copy(
                            language = event.lang
                        )
                    }
                }
            }

            is MainEvent.OnChangeDarkTheme -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.DARK_THEME, event.darkTheme)
                    updateStateWithSavedHandle {
                        it.copy(
                            darkTheme = event.darkTheme.toDarkTheme()
                        )
                    }
                }
            }

            is MainEvent.OnChangePureDark -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.PURE_DARK, event.pureDark)
                    updateStateWithSavedHandle {
                        it.copy(
                            pureDark = event.pureDark.toPureDark()
                        )
                    }
                }
            }

            is MainEvent.OnChangeThemeContrast -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.THEME_CONTRAST, event.themeContrast)
                    updateStateWithSavedHandle {
                        it.copy(
                            themeContrast = event.themeContrast.toThemeContrast()
                        )
                    }
                }
            }

            is MainEvent.OnChangeTheme -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.THEME, event.theme)
                    updateStateWithSavedHandle {
                        it.copy(
                            theme = event.theme.toTheme()
                        )
                    }
                }
            }

            is MainEvent.OnChangeFontFamily -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.FONT, event.fontFamily)
                    updateStateWithSavedHandle {
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
                    updateStateWithSavedHandle {
                        it.copy(
                            isItalic = event.fontStyle
                        )
                    }
                }
            }

            is MainEvent.OnChangeFontSize -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.FONT_SIZE, event.fontSize)
                    updateStateWithSavedHandle {
                        it.copy(
                            fontSize = event.fontSize
                        )
                    }
                }
            }

            is MainEvent.OnChangeLineHeight -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.LINE_HEIGHT, event.lineHeight)
                    updateStateWithSavedHandle {
                        it.copy(
                            lineHeight = event.lineHeight
                        )
                    }
                }
            }

            is MainEvent.OnChangeParagraphHeight -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.PARAGRAPH_HEIGHT, event.paragraphHeight)
                    updateStateWithSavedHandle {
                        it.copy(
                            paragraphHeight = event.paragraphHeight
                        )
                    }
                }
            }

            is MainEvent.OnChangeParagraphIndentation -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(
                        DataStoreConstants.PARAGRAPH_INDENTATION,
                        event.indentation
                    )
                    updateStateWithSavedHandle {
                        it.copy(
                            paragraphIndentation = event.indentation
                        )
                    }
                }
            }

            is MainEvent.OnChangeShowStartScreen -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.SHOW_START_SCREEN, event.bool)
                    updateStateWithSavedHandle {
                        it.copy(
                            showStartScreen = event.bool
                        )
                    }
                }
            }

            is MainEvent.OnChangeCheckForUpdates -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.CHECK_FOR_UPDATES, event.bool)
                    updateStateWithSavedHandle {
                        it.copy(
                            checkForUpdates = event.bool
                        )
                    }
                }
            }

            is MainEvent.OnChangeSidePadding -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.SIDE_PADDING, event.sidePadding)
                    updateStateWithSavedHandle {
                        it.copy(
                            sidePadding = event.sidePadding
                        )
                    }
                }
            }

            is MainEvent.OnChangeDoubleClickTranslation -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.DOUBLE_CLICK_TRANSLATION, event.bool)
                    updateStateWithSavedHandle {
                        it.copy(
                            doubleClickTranslation = event.bool
                        )
                    }
                }
            }

            is MainEvent.OnChangeFastColorPresetChange -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.FAST_COLOR_PRESET_CHANGE, event.bool)
                    updateStateWithSavedHandle {
                        it.copy(
                            fastColorPresetChange = event.bool
                        )
                    }
                }
            }

            is MainEvent.OnChangeBrowseFilesStructure -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.BROWSE_FILES_STRUCTURE, event.structure)
                    updateStateWithSavedHandle {
                        it.copy(
                            browseFilesStructure = event.structure.toFilesStructure()
                        )
                    }
                }
            }

            is MainEvent.OnChangeBrowseLayout -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.BROWSE_LAYOUT, event.layout)
                    updateStateWithSavedHandle {
                        it.copy(
                            browseLayout = event.layout.toBrowseLayout()
                        )
                    }
                }
            }

            is MainEvent.OnChangeBrowseAutoGridSize -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.BROWSE_AUTO_GRID_SIZE, event.bool)
                    updateStateWithSavedHandle {
                        it.copy(
                            browseAutoGridSize = event.bool
                        )
                    }
                }
            }

            is MainEvent.OnChangeBrowseGridSize -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.BROWSE_GRID_SIZE, event.size)
                    updateStateWithSavedHandle {
                        it.copy(
                            browseGridSize = event.size
                        )
                    }
                }
            }

            is MainEvent.OnChangeBrowsePinFavoriteDirectories -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(
                        DataStoreConstants.BROWSE_PIN_FAVORITE_DIRECTORIES,
                        event.bool
                    )
                    updateStateWithSavedHandle {
                        it.copy(
                            browsePinFavoriteDirectories = event.bool
                        )
                    }
                }
            }

            is MainEvent.OnChangeBrowseSortOrder -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(DataStoreConstants.BROWSE_SORT_ORDER, event.order)
                    updateStateWithSavedHandle {
                        it.copy(
                            browseSortOrder = event.order.toBrowseSortOrder()
                        )
                    }
                }
            }

            is MainEvent.OnChangeBrowseSortOrderDescending -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(
                        DataStoreConstants.BROWSE_SORT_ORDER_DESCENDING,
                        event.bool
                    )
                    updateStateWithSavedHandle {
                        it.copy(
                            browseSortOrderDescending = event.bool
                        )
                    }
                }
            }

            is MainEvent.OnChangeBrowseIncludedFilterItem -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val set = _state.value.browseIncludedFilterItems.toMutableSet()
                    if (!set.add(event.item)) {
                        set.remove(event.item)
                    }

                    setDatastore.execute(DataStoreConstants.BROWSE_INCLUDED_FILTER_ITEMS, set)
                    updateStateWithSavedHandle {
                        it.copy(
                            browseIncludedFilterItems = set.toList()
                        )
                    }
                }
            }

            is MainEvent.OnChangeTextAlignment -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(
                        DataStoreConstants.TEXT_ALIGNMENT,
                        event.alignment
                    )
                    updateStateWithSavedHandle {
                        it.copy(
                            textAlignment = event.alignment.toTextAlignment()
                        )
                    }
                }
            }

            is MainEvent.OnChangeDoublePressExit -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(
                        DataStoreConstants.DOUBLE_PRESS_EXIT,
                        event.bool
                    )
                    updateStateWithSavedHandle {
                        it.copy(
                            doublePressExit = event.bool
                        )
                    }
                }
            }

            is MainEvent.OnChangeLetterSpacing -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(
                        DataStoreConstants.LETTER_SPACING,
                        event.spacing
                    )
                    updateStateWithSavedHandle {
                        it.copy(
                            letterSpacing = event.spacing
                        )
                    }
                }
            }

            is MainEvent.OnChangeAbsoluteDark -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(
                        DataStoreConstants.ABSOLUTE_DARK,
                        event.bool
                    )
                    updateStateWithSavedHandle {
                        it.copy(
                            absoluteDark = event.bool
                        )
                    }
                }
            }

            is MainEvent.OnChangeCutoutPadding -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(
                        DataStoreConstants.CUTOUT_PADDING,
                        event.bool
                    )
                    updateStateWithSavedHandle {
                        it.copy(
                            cutoutPadding = event.bool
                        )
                    }
                }
            }

            is MainEvent.OnChangeFullscreen -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(
                        DataStoreConstants.FULLSCREEN,
                        event.bool
                    )
                    updateStateWithSavedHandle {
                        it.copy(
                            fullscreen = event.bool
                        )
                    }
                }
            }

            is MainEvent.OnChangeKeepScreenOn -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(
                        DataStoreConstants.KEEP_SCREEN_ON,
                        event.bool
                    )
                    updateStateWithSavedHandle {
                        it.copy(
                            keepScreenOn = event.bool
                        )
                    }
                }
            }

            is MainEvent.OnChangeVerticalPadding -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(
                        DataStoreConstants.VERTICAL_PADDING,
                        event.padding
                    )
                    updateStateWithSavedHandle {
                        it.copy(
                            verticalPadding = event.padding
                        )
                    }
                }
            }

            is MainEvent.OnChangeHideBarsOnFastScroll -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(
                        DataStoreConstants.HIDE_BARS_ON_FAST_SCROLL,
                        event.bool
                    )
                    updateStateWithSavedHandle {
                        it.copy(
                            hideBarsOnFastScroll = event.bool
                        )
                    }
                }
            }

            is MainEvent.OnChangePerceptionExpander -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(
                        DataStoreConstants.PERCEPTION_EXPANDER,
                        event.bool
                    )
                    updateStateWithSavedHandle {
                        it.copy(
                            perceptionExpander = event.bool
                        )
                    }
                }
            }

            is MainEvent.OnChangePerceptionExpanderPadding -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(
                        DataStoreConstants.PERCEPTION_EXPANDER_PADDING,
                        event.padding
                    )
                    updateStateWithSavedHandle {
                        it.copy(
                            perceptionExpanderPadding = event.padding
                        )
                    }
                }
            }

            is MainEvent.OnChangePerceptionExpanderThickness -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setDatastore.execute(
                        DataStoreConstants.PERCEPTION_EXPANDER_THICKNESS,
                        event.thickness
                    )
                    updateStateWithSavedHandle {
                        it.copy(
                            perceptionExpanderThickness = event.thickness
                        )
                    }
                }
            }
        }
    }

    fun init(
        libraryViewModel: LibraryViewModel,
        settingsViewModel: SettingsViewModel,
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            val settings = getAllSettings.execute()

            // All additional execution
            changeLanguage.execute(settings.language)

            if (settings.checkForUpdates) {
                viewModelScope.launch(Dispatchers.IO) {
                    checkForUpdates.execute(
                        postNotification = true
                    )
                }
            }

            updateStateWithSavedHandle { settings }
            isSettingsReady.update { true }
        }

        viewModelScope.launch(Dispatchers.IO) {
            combine(
                libraryViewModel.isReady,
                settingsViewModel.isReady
            ) { (libraryViewModelReady, settingsViewModelReady) ->
                libraryViewModelReady && settingsViewModelReady
            }.collectLatest { ready ->
                isViewModelReady.update {
                    ready
                }
            }
        }

        val isReady = combine(
            isViewModelReady,
            isSettingsReady
        ) { values ->
            values.all { it }
        }

        viewModelScope.launch(Dispatchers.IO) {
            isReady.first { bool ->
                if (bool) {
                    _isReady.update {
                        true
                    }
                }
                bool
            }
        }
    }

    /**
     * Updates [MainState] along with [SavedStateHandle].
     */
    private fun updateStateWithSavedHandle(
        function: (MainState) -> MainState
    ) {
        _state.update {
            stateHandle[Constants.MAIN_STATE] = function(it)
            function(it)
        }
    }
}