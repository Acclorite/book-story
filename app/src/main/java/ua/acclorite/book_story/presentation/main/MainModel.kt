/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.main

import androidx.datastore.preferences.core.Preferences
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import ua.acclorite.book_story.domain.use_case.data_store.ChangeLanguagePreferenceUseCase
import ua.acclorite.book_story.domain.use_case.data_store.GetPreferencesUseCase
import ua.acclorite.book_story.domain.use_case.data_store.PutPreferenceUseCase
import ua.acclorite.book_story.presentation.browse.model.BrowseLayout
import ua.acclorite.book_story.presentation.browse.model.BrowseSortOrder
import ua.acclorite.book_story.presentation.library.model.LibraryLayout
import ua.acclorite.book_story.presentation.library.model.LibrarySortOrder
import ua.acclorite.book_story.presentation.library.model.LibraryTitlePosition
import ua.acclorite.book_story.presentation.main.data.DataStoreData
import ua.acclorite.book_story.presentation.main.model.DarkTheme
import ua.acclorite.book_story.presentation.main.model.HorizontalAlignment
import ua.acclorite.book_story.presentation.main.model.PureDark
import ua.acclorite.book_story.presentation.main.model.ThemeContrast
import ua.acclorite.book_story.presentation.reader.model.ReaderColorEffects
import ua.acclorite.book_story.presentation.reader.model.ReaderFontThickness
import ua.acclorite.book_story.presentation.reader.model.ReaderHorizontalGesture
import ua.acclorite.book_story.presentation.reader.model.ReaderProgressCount
import ua.acclorite.book_story.presentation.reader.model.ReaderScreenOrientation
import ua.acclorite.book_story.presentation.reader.model.ReaderTextAlignment
import ua.acclorite.book_story.ui.reader.data.ReaderData
import ua.acclorite.book_story.ui.theme.Theme
import javax.inject.Inject


@HiltViewModel
class MainModel @Inject constructor(
    private val stateHandle: SavedStateHandle,
    private val putPreferenceUseCase: PutPreferenceUseCase,
    private val changeLanguagePreferenceUseCase: ChangeLanguagePreferenceUseCase,
    private val getPreferencesUseCase: GetPreferencesUseCase
) : ViewModel() {

    private val mutex = Mutex()

    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    private val mainModelReady = MutableStateFlow(false)

    private val _state: MutableStateFlow<MainState> = MutableStateFlow(
        stateHandle["main_state"] ?: MainState()
    )
    val state = _state.asStateFlow()

    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.OnChangeLanguage -> handleLanguageUpdate(event)

            is MainEvent.OnChangeDarkTheme -> handleDatastoreUpdate(
                key = DataStoreData.DARK_THEME,
                value = event.value,
                updateState = {
                    it.copy(darkTheme = DarkTheme.valueOf(this))
                }
            )

            is MainEvent.OnChangePureDark -> handleDatastoreUpdate(
                key = DataStoreData.PURE_DARK,
                value = event.value,
                updateState = {
                    it.copy(pureDark = PureDark.valueOf(this))
                }
            )

            is MainEvent.OnChangeThemeContrast -> handleDatastoreUpdate(
                key = DataStoreData.THEME_CONTRAST,
                value = event.value,
                updateState = {
                    it.copy(themeContrast = ThemeContrast.valueOf(this))
                }
            )

            is MainEvent.OnChangeTheme -> handleDatastoreUpdate(
                key = DataStoreData.THEME,
                value = event.value,
                updateState = {
                    it.copy(theme = Theme.valueOf(this))
                }
            )

            is MainEvent.OnChangeFontFamily -> handleDatastoreUpdate(
                key = DataStoreData.FONT,
                value = event.value,
                updateState = {
                    it.copy(
                        fontFamily = ReaderData.fonts.run {
                            find { font ->
                                font.id == event.value
                            }?.id ?: get(0).id
                        }
                    )
                }
            )

            is MainEvent.OnChangeFontStyle -> handleDatastoreUpdate(
                key = DataStoreData.IS_ITALIC,
                value = event.value,
                updateState = {
                    it.copy(isItalic = this)
                }
            )

            is MainEvent.OnChangeFontSize -> handleDatastoreUpdate(
                key = DataStoreData.FONT_SIZE,
                value = event.value,
                updateState = {
                    it.copy(fontSize = this)
                }
            )

            is MainEvent.OnChangeLineHeight -> handleDatastoreUpdate(
                key = DataStoreData.LINE_HEIGHT,
                value = event.value,
                updateState = {
                    it.copy(lineHeight = this)
                }
            )

            is MainEvent.OnChangeParagraphHeight -> handleDatastoreUpdate(
                key = DataStoreData.PARAGRAPH_HEIGHT,
                value = event.value,
                updateState = {
                    it.copy(paragraphHeight = this)
                }
            )

            is MainEvent.OnChangeParagraphIndentation -> handleDatastoreUpdate(
                key = DataStoreData.PARAGRAPH_INDENTATION,
                value = event.value,
                updateState = {
                    it.copy(paragraphIndentation = this)
                }
            )

            is MainEvent.OnChangeShowStartScreen -> handleDatastoreUpdate(
                key = DataStoreData.SHOW_START_SCREEN,
                value = event.value,
                updateState = {
                    it.copy(showStartScreen = this)
                }
            )

            is MainEvent.OnChangeSidePadding -> handleDatastoreUpdate(
                key = DataStoreData.SIDE_PADDING,
                value = event.value,
                updateState = {
                    it.copy(sidePadding = this)
                }
            )

            is MainEvent.OnChangeDoubleClickTranslation -> handleDatastoreUpdate(
                key = DataStoreData.DOUBLE_CLICK_TRANSLATION,
                value = event.value,
                updateState = {
                    it.copy(doubleClickTranslation = this)
                }
            )

            is MainEvent.OnChangeFastColorPresetChange -> handleDatastoreUpdate(
                key = DataStoreData.FAST_COLOR_PRESET_CHANGE,
                value = event.value,
                updateState = {
                    it.copy(fastColorPresetChange = this)
                }
            )

            is MainEvent.OnChangeBrowseLayout -> handleDatastoreUpdate(
                key = DataStoreData.BROWSE_LAYOUT,
                value = event.value,
                updateState = {
                    it.copy(browseLayout = BrowseLayout.valueOf(this))
                }
            )

            is MainEvent.OnChangeBrowseAutoGridSize -> handleDatastoreUpdate(
                key = DataStoreData.BROWSE_AUTO_GRID_SIZE,
                value = event.value,
                updateState = {
                    it.copy(browseAutoGridSize = this)
                }
            )

            is MainEvent.OnChangeBrowseGridSize -> handleDatastoreUpdate(
                key = DataStoreData.BROWSE_GRID_SIZE,
                value = event.value,
                updateState = {
                    it.copy(browseGridSize = this)
                }
            )

            is MainEvent.OnChangeBrowseSortOrder -> handleDatastoreUpdate(
                key = DataStoreData.BROWSE_SORT_ORDER,
                value = event.value,
                updateState = {
                    it.copy(browseSortOrder = BrowseSortOrder.valueOf(this))
                }
            )

            is MainEvent.OnChangeBrowseSortOrderDescending -> handleDatastoreUpdate(
                key = DataStoreData.BROWSE_SORT_ORDER_DESCENDING,
                value = event.value,
                updateState = {
                    it.copy(browseSortOrderDescending = this)
                }
            )

            is MainEvent.OnChangeBrowseIncludedFilterItem -> handleBrowseIncludedFilterItemUpdate(
                event = event
            )

            is MainEvent.OnChangeTextAlignment -> handleDatastoreUpdate(
                key = DataStoreData.TEXT_ALIGNMENT,
                value = event.value,
                updateState = {
                    it.copy(textAlignment = ReaderTextAlignment.valueOf(this))
                }
            )

            is MainEvent.OnChangeDoublePressExit -> handleDatastoreUpdate(
                key = DataStoreData.DOUBLE_PRESS_EXIT,
                value = event.value,
                updateState = {
                    it.copy(doublePressExit = this)
                }
            )

            is MainEvent.OnChangeLetterSpacing -> handleDatastoreUpdate(
                key = DataStoreData.LETTER_SPACING,
                value = event.value,
                updateState = {
                    it.copy(letterSpacing = this)
                }
            )

            is MainEvent.OnChangeAbsoluteDark -> handleDatastoreUpdate(
                key = DataStoreData.ABSOLUTE_DARK,
                value = event.value,
                updateState = {
                    it.copy(absoluteDark = this)
                }
            )

            is MainEvent.OnChangeCutoutPadding -> handleDatastoreUpdate(
                key = DataStoreData.CUTOUT_PADDING,
                value = event.value,
                updateState = {
                    it.copy(cutoutPadding = this)
                }
            )

            is MainEvent.OnChangeFullscreen -> handleDatastoreUpdate(
                key = DataStoreData.FULLSCREEN,
                value = event.value,
                updateState = {
                    it.copy(fullscreen = this)
                }
            )

            is MainEvent.OnChangeKeepScreenOn -> handleDatastoreUpdate(
                key = DataStoreData.KEEP_SCREEN_ON,
                value = event.value,
                updateState = {
                    it.copy(keepScreenOn = this)
                }
            )

            is MainEvent.OnChangeVerticalPadding -> handleDatastoreUpdate(
                key = DataStoreData.VERTICAL_PADDING,
                value = event.value,
                updateState = {
                    it.copy(verticalPadding = this)
                }
            )

            is MainEvent.OnChangeHideBarsOnFastScroll -> handleDatastoreUpdate(
                key = DataStoreData.HIDE_BARS_ON_FAST_SCROLL,
                value = event.value,
                updateState = {
                    it.copy(hideBarsOnFastScroll = this)
                }
            )

            is MainEvent.OnChangePerceptionExpander -> handleDatastoreUpdate(
                key = DataStoreData.PERCEPTION_EXPANDER,
                value = event.value,
                updateState = {
                    it.copy(perceptionExpander = this)
                }
            )

            is MainEvent.OnChangePerceptionExpanderPadding -> handleDatastoreUpdate(
                key = DataStoreData.PERCEPTION_EXPANDER_PADDING,
                value = event.value,
                updateState = {
                    it.copy(perceptionExpanderPadding = this)
                }
            )

            is MainEvent.OnChangePerceptionExpanderThickness -> handleDatastoreUpdate(
                key = DataStoreData.PERCEPTION_EXPANDER_THICKNESS,
                value = event.value,
                updateState = {
                    it.copy(perceptionExpanderThickness = this)
                }
            )

            is MainEvent.OnChangeScreenOrientation -> handleDatastoreUpdate(
                key = DataStoreData.SCREEN_ORIENTATION,
                value = event.value,
                updateState = {
                    it.copy(screenOrientation = ReaderScreenOrientation.valueOf(this))
                }
            )

            is MainEvent.OnChangeCustomScreenBrightness -> handleDatastoreUpdate(
                key = DataStoreData.CUSTOM_SCREEN_BRIGHTNESS,
                value = event.value,
                updateState = {
                    it.copy(customScreenBrightness = this)
                }
            )

            is MainEvent.OnChangeScreenBrightness -> handleDatastoreUpdate(
                key = DataStoreData.SCREEN_BRIGHTNESS,
                value = event.value.toDouble(),
                updateState = {
                    it.copy(screenBrightness = this.toFloat())
                }
            )

            is MainEvent.OnChangeHorizontalGesture -> handleDatastoreUpdate(
                key = DataStoreData.HORIZONTAL_GESTURE,
                value = event.value,
                updateState = {
                    it.copy(horizontalGesture = ReaderHorizontalGesture.valueOf(this))
                }
            )

            is MainEvent.OnChangeHorizontalGestureScroll -> handleDatastoreUpdate(
                key = DataStoreData.HORIZONTAL_GESTURE_SCROLL,
                value = event.value.toDouble(),
                updateState = {
                    it.copy(horizontalGestureScroll = this.toFloat())
                }
            )

            is MainEvent.OnChangeHorizontalGestureSensitivity -> handleDatastoreUpdate(
                key = DataStoreData.HORIZONTAL_GESTURE_SENSITIVITY,
                value = event.value.toDouble(),
                updateState = {
                    it.copy(horizontalGestureSensitivity = this.toFloat())
                }
            )

            is MainEvent.OnChangeBottomBarPadding -> handleDatastoreUpdate(
                key = DataStoreData.BOTTOM_BAR_PADDING,
                value = event.value,
                updateState = {
                    it.copy(bottomBarPadding = this)
                }
            )

            is MainEvent.OnChangeHighlightedReading -> handleDatastoreUpdate(
                key = DataStoreData.HIGHLIGHTED_READING,
                value = event.value,
                updateState = {
                    it.copy(highlightedReading = this)
                }
            )

            is MainEvent.OnChangeHighlightedReadingThickness -> handleDatastoreUpdate(
                key = DataStoreData.HIGHLIGHTED_READING_THICKNESS,
                value = event.value,
                updateState = {
                    it.copy(highlightedReadingThickness = this)
                }
            )

            is MainEvent.OnChangeChapterTitleAlignment -> handleDatastoreUpdate(
                key = DataStoreData.CHAPTER_TITLE_ALIGNMENT,
                value = event.value,
                updateState = {
                    it.copy(chapterTitleAlignment = ReaderTextAlignment.valueOf(this))
                }
            )

            is MainEvent.OnChangeImages -> handleDatastoreUpdate(
                key = DataStoreData.IMAGES,
                value = event.value,
                updateState = {
                    it.copy(images = this)
                }
            )

            is MainEvent.OnChangeImagesCaptions -> handleDatastoreUpdate(
                key = DataStoreData.IMAGES_CAPTIONS,
                value = event.value,
                updateState = {
                    it.copy(imagesCaptions = this)
                }
            )

            is MainEvent.OnChangeImagesCornersRoundness -> handleDatastoreUpdate(
                key = DataStoreData.IMAGES_CORNERS_ROUNDNESS,
                value = event.value,
                updateState = {
                    it.copy(imagesCornersRoundness = this)
                }
            )

            is MainEvent.OnChangeImagesAlignment -> handleDatastoreUpdate(
                key = DataStoreData.IMAGES_ALIGNMENT,
                value = event.value,
                updateState = {
                    it.copy(imagesAlignment = HorizontalAlignment.valueOf(this))
                }
            )

            is MainEvent.OnChangeImagesWidth -> handleDatastoreUpdate(
                key = DataStoreData.IMAGES_WIDTH,
                value = event.value.toDouble(),
                updateState = {
                    it.copy(imagesWidth = this.toFloat())
                }
            )

            is MainEvent.OnChangeImagesColorEffects -> handleDatastoreUpdate(
                key = DataStoreData.IMAGES_COLOR_EFFECTS,
                value = event.value,
                updateState = {
                    it.copy(imagesColorEffects = ReaderColorEffects.valueOf(this))
                }
            )

            is MainEvent.OnChangeProgressBar -> handleDatastoreUpdate(
                key = DataStoreData.PROGRESS_BAR,
                value = event.value,
                updateState = {
                    it.copy(progressBar = this)
                }
            )

            is MainEvent.OnChangeProgressBarPadding -> handleDatastoreUpdate(
                key = DataStoreData.PROGRESS_BAR_PADDING,
                value = event.value,
                updateState = {
                    it.copy(progressBarPadding = this)
                }
            )

            is MainEvent.OnChangeProgressBarAlignment -> handleDatastoreUpdate(
                key = DataStoreData.PROGRESS_BAR_ALIGNMENT,
                value = event.value,
                updateState = {
                    it.copy(progressBarAlignment = HorizontalAlignment.valueOf(this))
                }
            )

            is MainEvent.OnChangeProgressBarFontSize -> handleDatastoreUpdate(
                key = DataStoreData.PROGRESS_BAR_FONT_SIZE,
                value = event.value,
                updateState = {
                    it.copy(progressBarFontSize = this)
                }
            )

            is MainEvent.OnChangeBrowsePinnedPaths -> handleBrowsePinnedPathsUpdate(
                event = event
            )

            is MainEvent.OnChangeFontThickness -> handleDatastoreUpdate(
                key = DataStoreData.FONT_THICKNESS,
                value = event.value,
                updateState = {
                    it.copy(fontThickness = ReaderFontThickness.valueOf(this))
                }
            )

            is MainEvent.OnChangeProgressCount -> handleDatastoreUpdate(
                key = DataStoreData.PROGRESS_COUNT,
                value = event.value,
                updateState = {
                    it.copy(progressCount = ReaderProgressCount.valueOf(this))
                }
            )

            is MainEvent.OnChangeHorizontalGestureAlphaAnim -> handleDatastoreUpdate(
                key = DataStoreData.HORIZONTAL_GESTURE_ALPHA_ANIM,
                value = event.value,
                updateState = {
                    it.copy(horizontalGestureAlphaAnim = this)
                }
            )

            is MainEvent.OnChangeHorizontalGesturePullAnim -> handleDatastoreUpdate(
                key = DataStoreData.HORIZONTAL_GESTURE_PULL_ANIM,
                value = event.value,
                updateState = {
                    it.copy(horizontalGesturePullAnim = this)
                }
            )

            is MainEvent.OnChangeLibraryLayout -> handleDatastoreUpdate(
                key = DataStoreData.LIBRARY_LAYOUT,
                value = event.value,
                updateState = {
                    it.copy(libraryLayout = LibraryLayout.valueOf(this))
                }
            )

            is MainEvent.OnChangeLibraryAutoGridSize -> handleDatastoreUpdate(
                key = DataStoreData.LIBRARY_AUTO_GRID_SIZE,
                value = event.value,
                updateState = {
                    it.copy(libraryAutoGridSize = this)
                }
            )

            is MainEvent.OnChangeLibraryGridSize -> handleDatastoreUpdate(
                key = DataStoreData.LIBRARY_GRID_SIZE,
                value = event.value,
                updateState = {
                    it.copy(libraryGridSize = this)
                }
            )

            is MainEvent.OnChangeLibraryReadButton -> handleDatastoreUpdate(
                key = DataStoreData.LIBRARY_READ_BUTTON,
                value = event.value,
                updateState = {
                    it.copy(libraryReadButton = this)
                }
            )

            is MainEvent.OnChangeLibraryShowProgress -> handleDatastoreUpdate(
                key = DataStoreData.LIBRARY_SHOW_PROGRESS,
                value = event.value,
                updateState = {
                    it.copy(libraryShowProgress = this)
                }
            )

            is MainEvent.OnChangeLibraryTitlePosition -> handleDatastoreUpdate(
                key = DataStoreData.LIBRARY_TITLE_POSITION,
                value = event.value,
                updateState = {
                    it.copy(libraryTitlePosition = LibraryTitlePosition.valueOf(this))
                }
            )

            is MainEvent.OnChangeLibraryShowBookCount -> handleDatastoreUpdate(
                key = DataStoreData.LIBRARY_SHOW_BOOK_COUNT,
                value = event.value,
                updateState = {
                    it.copy(libraryShowBookCount = this)
                }
            )

            is MainEvent.OnChangeLibraryShowCategoryTabs -> handleDatastoreUpdate(
                key = DataStoreData.LIBRARY_SHOW_CATEGORY_TABS,
                value = event.value,
                updateState = {
                    it.copy(libraryShowCategoryTabs = this)
                }
            )

            is MainEvent.OnChangeLibraryAlwaysShowDefaultTab -> handleDatastoreUpdate(
                key = DataStoreData.LIBRARY_ALWAYS_SHOW_DEFAULT_TAB,
                value = event.value,
                updateState = {
                    it.copy(libraryAlwaysShowDefaultTab = this)
                }
            )

            is MainEvent.OnChangeLibrarySortOrder -> handleDatastoreUpdate(
                key = DataStoreData.LIBRARY_SORT_ORDER,
                value = event.value,
                updateState = {
                    it.copy(librarySortOrder = LibrarySortOrder.valueOf(this))
                }
            )

            is MainEvent.OnChangeLibrarySortOrderDescending -> handleDatastoreUpdate(
                key = DataStoreData.LIBRARY_SORT_ORDER_DESCENDING,
                value = event.value,
                updateState = {
                    it.copy(librarySortOrderDescending = this)
                }
            )

            is MainEvent.OnChangeLibraryPerCategorySort -> handleDatastoreUpdate(
                key = DataStoreData.LIBRARY_PER_CATEGORY_SORT,
                value = event.value,
                updateState = {
                    it.copy(libraryPerCategorySort = this)
                }
            )
        }
    }

    fun init(settingsModelReady: StateFlow<Boolean>) {
        viewModelScope.launch(Dispatchers.Main) {
            val settings = getPreferencesUseCase()

            /* All additional execution */
            changeLanguagePreferenceUseCase(settings.language)

            updateStateWithSavedHandle { settings }
            mainModelReady.update { true }
        }

        val isReady = combine(
            mainModelReady.asStateFlow(),
            settingsModelReady
        ) { values ->
            values.all { it }
        }

        viewModelScope.launch(Dispatchers.Main) {
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

    private fun handleLanguageUpdate(event: MainEvent.OnChangeLanguage) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            changeLanguagePreferenceUseCase(event.value)
            updateStateWithSavedHandle {
                it.copy(language = event.value)
            }
        }
    }

    private fun handleBrowseIncludedFilterItemUpdate(
        event: MainEvent.OnChangeBrowseIncludedFilterItem
    ) {
        val set = _state.value.browseIncludedFilterItems.toMutableSet()
        if (!set.add(event.value)) {
            set.remove(event.value)
        }
        handleDatastoreUpdate(
            key = DataStoreData.BROWSE_INCLUDED_FILTER_ITEMS,
            value = set,
            updateState = {
                it.copy(browseIncludedFilterItems = toList())
            }
        )
    }

    private fun handleBrowsePinnedPathsUpdate(
        event: MainEvent.OnChangeBrowsePinnedPaths
    ) {
        val set = _state.value.browsePinnedPaths.toMutableSet()
        if (!set.add(event.value)) {
            set.remove(event.value)
        }
        handleDatastoreUpdate(
            key = DataStoreData.BROWSE_PINNED_PATHS,
            value = set,
            updateState = {
                it.copy(browsePinnedPaths = toList())
            }
        )
    }

    /**
     * Handles and updates Datastore.
     */
    private fun <V> handleDatastoreUpdate(
        key: Preferences.Key<V>,
        value: V,
        updateState: V.(MainState) -> MainState
    ) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            putPreferenceUseCase(key = key, value = value)
            updateStateWithSavedHandle {
                value.updateState(it)
            }
        }
    }

    /**
     * Updates [MainState] along with [SavedStateHandle].
     */
    private suspend fun updateStateWithSavedHandle(
        function: (MainState) -> MainState
    ) {
        withContext(Dispatchers.Main.immediate) {
            _state.update {
                stateHandle["main_state"] = function(it)
                function(it)
            }
        }
    }

    private suspend inline fun <T> MutableStateFlow<T>.update(function: (T) -> T) {
        mutex.withLock {
            yield()
            this.value = function(this.value)
        }
    }
}