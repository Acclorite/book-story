/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

@file:Suppress("UNCHECKED_CAST")

package ua.acclorite.book_story.presentation.main

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.compose.runtime.Immutable
import androidx.datastore.preferences.core.Preferences
import kotlinx.parcelize.Parcelize
import ua.acclorite.book_story.presentation.browse.model.BrowseLayout
import ua.acclorite.book_story.presentation.browse.model.BrowseSortOrder
import ua.acclorite.book_story.presentation.library.model.LibraryLayout
import ua.acclorite.book_story.presentation.library.model.LibrarySortOrder
import ua.acclorite.book_story.presentation.library.model.LibraryTitlePosition
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
import ua.acclorite.book_story.presentation.theme.Theme
import ua.acclorite.book_story.presentation.theme.toTheme
import ua.acclorite.book_story.ui.common.constants.DataStoreConstants
import ua.acclorite.book_story.ui.common.constants.provideFonts
import ua.acclorite.book_story.ui.common.constants.provideLanguages
import java.util.Locale

/**
 * Main State.
 * All app's settings/preferences/permanent-variables are here.
 * Wrapped in SavedStateHandle, so it won't reset.
 */
@Immutable
@Keep
@Parcelize
data class MainState(
    // General Settings
    val language: String = provideDefaultValue {
        val locale = Locale.getDefault().language.take(2)
        provideLanguages().any { locale == it.first }.run {
            if (this) locale
            else "en"// Default language.
        }
    },
    val theme: Theme = provideDefaultValue { Theme.entries().first() },
    val darkTheme: DarkTheme = provideDefaultValue { DarkTheme.FOLLOW_SYSTEM },
    val pureDark: PureDark = provideDefaultValue { PureDark.OFF },
    val absoluteDark: Boolean = provideDefaultValue { false },
    val themeContrast: ThemeContrast = provideDefaultValue { ThemeContrast.STANDARD },
    val showStartScreen: Boolean = provideDefaultValue { true },
    val doublePressExit: Boolean = provideDefaultValue { false },

    // Reader Settings
    val fontFamily: String = provideDefaultValue { provideFonts()[0].id },
    val fontThickness: ReaderFontThickness = provideDefaultValue { ReaderFontThickness.NORMAL },
    val isItalic: Boolean = provideDefaultValue { false },
    val fontSize: Int = provideDefaultValue { 16 },
    val lineHeight: Int = provideDefaultValue { 4 },
    val paragraphHeight: Int = provideDefaultValue { 8 },
    val paragraphIndentation: Int = provideDefaultValue { 0 },
    val sidePadding: Int = provideDefaultValue { 6 },
    val verticalPadding: Int = provideDefaultValue { 0 },
    val doubleClickTranslation: Boolean = provideDefaultValue { false },
    val fastColorPresetChange: Boolean = provideDefaultValue { true },
    val textAlignment: ReaderTextAlignment = provideDefaultValue { ReaderTextAlignment.JUSTIFY },
    val letterSpacing: Int = provideDefaultValue { 0 },
    val cutoutPadding: Boolean = provideDefaultValue { false },
    val fullscreen: Boolean = provideDefaultValue { true },
    val keepScreenOn: Boolean = provideDefaultValue { true },
    val hideBarsOnFastScroll: Boolean = provideDefaultValue { false },
    val perceptionExpander: Boolean = provideDefaultValue { false },
    val perceptionExpanderPadding: Int = provideDefaultValue { 5 },
    val perceptionExpanderThickness: Int = provideDefaultValue { 4 },
    val screenOrientation: ReaderScreenOrientation = provideDefaultValue {
        ReaderScreenOrientation.DEFAULT
    },
    val customScreenBrightness: Boolean = provideDefaultValue { false },
    val screenBrightness: Float = provideDefaultValue { 0.5f },
    val horizontalGesture: ReaderHorizontalGesture = provideDefaultValue {
        ReaderHorizontalGesture.OFF
    },
    val horizontalGestureScroll: Float = provideDefaultValue { 0.7f },
    val horizontalGestureSensitivity: Float = provideDefaultValue { 0.6f },
    val horizontalGestureAlphaAnim: Boolean = provideDefaultValue { true },
    val horizontalGesturePullAnim: Boolean = provideDefaultValue { true },
    val bottomBarPadding: Int = provideDefaultValue { 0 },
    val highlightedReading: Boolean = provideDefaultValue { false },
    val highlightedReadingThickness: Int = provideDefaultValue { 2 },
    val chapterTitleAlignment: ReaderTextAlignment = provideDefaultValue { ReaderTextAlignment.JUSTIFY },
    val images: Boolean = provideDefaultValue { true },
    val imagesCornersRoundness: Int = provideDefaultValue { 8 },
    val imagesAlignment: HorizontalAlignment = provideDefaultValue { HorizontalAlignment.START },
    val imagesWidth: Float = provideDefaultValue { 0.8f },
    val imagesColorEffects: ReaderColorEffects = provideDefaultValue { ReaderColorEffects.OFF },
    val progressBar: Boolean = provideDefaultValue { false },
    val progressBarPadding: Int = provideDefaultValue { 4 },
    val progressBarAlignment: HorizontalAlignment = provideDefaultValue { HorizontalAlignment.CENTER },
    val progressBarFontSize: Int = provideDefaultValue { 8 },
    val progressCount: ReaderProgressCount = provideDefaultValue { ReaderProgressCount.PERCENTAGE },

    // Library settings
    val libraryLayout: LibraryLayout = provideDefaultValue { LibraryLayout.GRID },
    val libraryAutoGridSize: Boolean = provideDefaultValue { true },
    val libraryGridSize: Int = provideDefaultValue { 0 },
    val libraryTitlePosition: LibraryTitlePosition = provideDefaultValue { LibraryTitlePosition.BELOW },
    val libraryReadButton: Boolean = provideDefaultValue { true },
    val libraryShowProgress: Boolean = provideDefaultValue { true },
    val libraryShowBookCount: Boolean = provideDefaultValue { true },
    val libraryShowCategoryTabs: Boolean = provideDefaultValue { true },
    val libraryAlwaysShowDefaultTab: Boolean = provideDefaultValue { false },
    val librarySortOrder: LibrarySortOrder = provideDefaultValue { LibrarySortOrder.LAST_READ },
    val librarySortOrderDescending: Boolean = provideDefaultValue { true },
    val libraryPerCategorySort: Boolean = provideDefaultValue { false },

    // Browse Settings
    val browseLayout: BrowseLayout = provideDefaultValue { BrowseLayout.LIST },
    val browseAutoGridSize: Boolean = provideDefaultValue { true },
    val browseGridSize: Int = provideDefaultValue { 0 },
    val browseSortOrder: BrowseSortOrder = provideDefaultValue { BrowseSortOrder.LAST_MODIFIED },
    val browseSortOrderDescending: Boolean = provideDefaultValue { true },
    val browseIncludedFilterItems: List<String> = provideDefaultValue { emptyList() },
    val browsePinnedPaths: List<String> = provideDefaultValue { emptyList() },
) : Parcelable {
    companion object {
        private fun <D> provideDefaultValue(calculation: () -> D): D {
            return calculation()
        }

        /**
         * Initializes [MainState] by given [Map].
         * If no value provided in [data], assigns default value.
         */
        fun initialize(data: Map<String, Any>): MainState {
            val defaultState = MainState()
            fun <V, T> provideValue(
                key: Preferences.Key<T>,
                convert: T.() -> V = { this as V },
                default: MainState.() -> V
            ): V {
                return (data[key.name] as? T)?.convert() ?: defaultState.default()
            }

            return DataStoreConstants.run {
                MainState(
                    language = provideValue(
                        LANGUAGE
                    ) { language },

                    theme = provideValue(
                        THEME, convert = { toTheme() }
                    ) { theme },

                    darkTheme = provideValue(
                        DARK_THEME, convert = { DarkTheme.valueOf(this) }
                    ) { darkTheme },

                    pureDark = provideValue(
                        PURE_DARK, convert = { PureDark.valueOf(this) }
                    ) { pureDark },

                    absoluteDark = provideValue(
                        ABSOLUTE_DARK
                    ) { absoluteDark },

                    themeContrast = provideValue(
                        THEME_CONTRAST, convert = { ThemeContrast.valueOf(this) }
                    ) { themeContrast },

                    showStartScreen = provideValue(
                        SHOW_START_SCREEN
                    ) { showStartScreen },

                    fontFamily = provideValue(
                        FONT
                    ) { fontFamily },

                    isItalic = provideValue(
                        IS_ITALIC
                    ) { isItalic },

                    fontSize = provideValue(
                        FONT_SIZE
                    ) { fontSize },

                    lineHeight = provideValue(
                        LINE_HEIGHT
                    ) { lineHeight },

                    paragraphHeight = provideValue(
                        PARAGRAPH_HEIGHT
                    ) { paragraphHeight },

                    paragraphIndentation = provideValue(
                        PARAGRAPH_INDENTATION
                    ) { paragraphIndentation },

                    sidePadding = provideValue(
                        SIDE_PADDING
                    ) { sidePadding },

                    doubleClickTranslation = provideValue(
                        DOUBLE_CLICK_TRANSLATION
                    ) { doubleClickTranslation },

                    fastColorPresetChange = provideValue(
                        FAST_COLOR_PRESET_CHANGE
                    ) { fastColorPresetChange },

                    browseLayout = provideValue(
                        BROWSE_LAYOUT, convert = { BrowseLayout.valueOf(this) }
                    ) { browseLayout },

                    browseAutoGridSize = provideValue(
                        BROWSE_AUTO_GRID_SIZE
                    ) { browseAutoGridSize },

                    browseGridSize = provideValue(
                        BROWSE_GRID_SIZE
                    ) { browseGridSize },

                    browseSortOrder = provideValue(
                        BROWSE_SORT_ORDER, convert = { BrowseSortOrder.valueOf(this) }
                    ) { browseSortOrder },

                    browseSortOrderDescending = provideValue(
                        BROWSE_SORT_ORDER_DESCENDING
                    ) { browseSortOrderDescending },

                    browseIncludedFilterItems = provideValue(
                        BROWSE_INCLUDED_FILTER_ITEMS, convert = { toList() }
                    ) { browseIncludedFilterItems },

                    textAlignment = provideValue(
                        TEXT_ALIGNMENT, convert = { ReaderTextAlignment.valueOf(this) }
                    ) { textAlignment },

                    doublePressExit = provideValue(
                        DOUBLE_PRESS_EXIT
                    ) { doublePressExit },

                    letterSpacing = provideValue(
                        LETTER_SPACING
                    ) { letterSpacing },

                    cutoutPadding = provideValue(
                        CUTOUT_PADDING
                    ) { cutoutPadding },

                    fullscreen = provideValue(
                        FULLSCREEN
                    ) { fullscreen },

                    keepScreenOn = provideValue(
                        KEEP_SCREEN_ON
                    ) { keepScreenOn },

                    verticalPadding = provideValue(
                        VERTICAL_PADDING
                    ) { verticalPadding },

                    hideBarsOnFastScroll = provideValue(
                        HIDE_BARS_ON_FAST_SCROLL
                    ) { hideBarsOnFastScroll },

                    perceptionExpander = provideValue(
                        PERCEPTION_EXPANDER
                    ) { perceptionExpander },

                    perceptionExpanderPadding = provideValue(
                        PERCEPTION_EXPANDER_PADDING
                    ) { perceptionExpanderPadding },

                    perceptionExpanderThickness = provideValue(
                        PERCEPTION_EXPANDER_THICKNESS
                    ) { perceptionExpanderThickness },

                    screenOrientation = provideValue(
                        SCREEN_ORIENTATION, convert = { ReaderScreenOrientation.valueOf(this) }
                    ) { screenOrientation },

                    customScreenBrightness = provideValue(
                        CUSTOM_SCREEN_BRIGHTNESS
                    ) { customScreenBrightness },

                    screenBrightness = provideValue(
                        SCREEN_BRIGHTNESS, convert = { this.toFloat() }
                    ) { screenBrightness },

                    horizontalGesture = provideValue(
                        HORIZONTAL_GESTURE, convert = { ReaderHorizontalGesture.valueOf(this) }
                    ) { horizontalGesture },

                    horizontalGestureScroll = provideValue(
                        HORIZONTAL_GESTURE_SCROLL, convert = { toFloat() }
                    ) { horizontalGestureScroll },

                    horizontalGestureSensitivity = provideValue(
                        HORIZONTAL_GESTURE_SENSITIVITY, convert = { toFloat() }
                    ) { horizontalGestureSensitivity },

                    bottomBarPadding = provideValue(
                        BOTTOM_BAR_PADDING
                    ) { bottomBarPadding },

                    highlightedReading = provideValue(
                        HIGHLIGHTED_READING
                    ) { highlightedReading },

                    highlightedReadingThickness = provideValue(
                        HIGHLIGHTED_READING_THICKNESS
                    ) { highlightedReadingThickness },

                    chapterTitleAlignment = provideValue(
                        CHAPTER_TITLE_ALIGNMENT, convert = { ReaderTextAlignment.valueOf(this) }
                    ) { chapterTitleAlignment },

                    images = provideValue(
                        IMAGES
                    ) { images },

                    imagesCornersRoundness = provideValue(
                        IMAGES_CORNERS_ROUNDNESS
                    ) { imagesCornersRoundness },

                    imagesAlignment = provideValue(
                        IMAGES_ALIGNMENT, convert = { HorizontalAlignment.valueOf(this) }
                    ) { imagesAlignment },

                    imagesWidth = provideValue(
                        IMAGES_WIDTH, convert = { toFloat() }
                    ) { imagesWidth },

                    imagesColorEffects = provideValue(
                        IMAGES_COLOR_EFFECTS, convert = { ReaderColorEffects.valueOf(this) }
                    ) { imagesColorEffects },

                    progressBar = provideValue(
                        PROGRESS_BAR
                    ) { progressBar },

                    progressBarPadding = provideValue(
                        PROGRESS_BAR_PADDING
                    ) { progressBarPadding },

                    progressBarAlignment = provideValue(
                        PROGRESS_BAR_ALIGNMENT, convert = { HorizontalAlignment.valueOf(this) }
                    ) { progressBarAlignment },

                    progressBarFontSize = provideValue(
                        PROGRESS_BAR_FONT_SIZE
                    ) { progressBarFontSize },

                    browsePinnedPaths = provideValue(
                        BROWSE_PINNED_PATHS, convert = { toList() }
                    ) { browsePinnedPaths },

                    fontThickness = provideValue(
                        FONT_THICKNESS, convert = { ReaderFontThickness.valueOf(this) }
                    ) { fontThickness },

                    progressCount = provideValue(
                        PROGRESS_COUNT, convert = { ReaderProgressCount.valueOf(this) }
                    ) { progressCount },

                    horizontalGestureAlphaAnim = provideValue(
                        HORIZONTAL_GESTURE_ALPHA_ANIM
                    ) { horizontalGestureAlphaAnim },

                    horizontalGesturePullAnim = provideValue(
                        HORIZONTAL_GESTURE_PULL_ANIM
                    ) { horizontalGesturePullAnim },

                    libraryLayout = provideValue(
                        LIBRARY_LAYOUT, convert = { LibraryLayout.valueOf(this) }
                    ) { libraryLayout },

                    libraryAutoGridSize = provideValue(
                        LIBRARY_AUTO_GRID_SIZE
                    ) { libraryAutoGridSize },

                    libraryGridSize = provideValue(
                        LIBRARY_GRID_SIZE
                    ) { libraryGridSize },

                    libraryReadButton = provideValue(
                        LIBRARY_READ_BUTTON
                    ) { libraryReadButton },

                    libraryShowProgress = provideValue(
                        LIBRARY_SHOW_PROGRESS
                    ) { libraryShowProgress },

                    libraryTitlePosition = provideValue(
                        LIBRARY_TITLE_POSITION, convert = { LibraryTitlePosition.valueOf(this) }
                    ) { libraryTitlePosition },

                    libraryShowBookCount = provideValue(
                        LIBRARY_SHOW_BOOK_COUNT
                    ) { libraryShowBookCount },

                    libraryShowCategoryTabs = provideValue(
                        LIBRARY_SHOW_CATEGORY_TABS
                    ) { libraryShowCategoryTabs },

                    libraryAlwaysShowDefaultTab = provideValue(
                        LIBRARY_ALWAYS_SHOW_DEFAULT_TAB
                    ) { libraryAlwaysShowDefaultTab },

                    librarySortOrder = provideValue(
                        LIBRARY_SORT_ORDER, convert = { LibrarySortOrder.valueOf(this) }
                    ) { librarySortOrder },

                    librarySortOrderDescending = provideValue(
                        LIBRARY_SORT_ORDER_DESCENDING
                    ) { librarySortOrderDescending },

                    libraryPerCategorySort = provideValue(
                        LIBRARY_PER_CATEGORY_SORT
                    ) { libraryPerCategorySort },
                )
            }
        }
    }
}