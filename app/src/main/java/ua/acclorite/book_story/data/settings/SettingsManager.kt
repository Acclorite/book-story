/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

@file:Suppress("RemoveExplicitTypeArguments")

package ua.acclorite.book_story.data.settings

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.acclorite.book_story.core.data.CoreData
import ua.acclorite.book_story.core.language.Language
import ua.acclorite.book_story.core.language.LanguageUtils
import ua.acclorite.book_story.core.log.logI
import ua.acclorite.book_story.data.local.data_store.DataStore
import ua.acclorite.book_story.data.settings.model.Setting
import ua.acclorite.book_story.presentation.browse.model.BrowseLayout
import ua.acclorite.book_story.presentation.browse.model.BrowseSortOrder
import ua.acclorite.book_story.presentation.library.model.LibraryLayout
import ua.acclorite.book_story.presentation.library.model.LibrarySortOrder
import ua.acclorite.book_story.presentation.library.model.LibraryTitlePosition
import ua.acclorite.book_story.presentation.reader.model.ReaderColorEffects
import ua.acclorite.book_story.presentation.reader.model.ReaderFontThickness
import ua.acclorite.book_story.presentation.reader.model.ReaderHorizontalGesture
import ua.acclorite.book_story.presentation.reader.model.ReaderProgressCount
import ua.acclorite.book_story.presentation.reader.model.ReaderScreenOrientation
import ua.acclorite.book_story.presentation.reader.model.ReaderTextAlignment
import ua.acclorite.book_story.ui.reader.data.ReaderData
import ua.acclorite.book_story.ui.reader.model.FontWithName
import ua.acclorite.book_story.ui.theme.model.DarkTheme
import ua.acclorite.book_story.ui.theme.model.HorizontalAlignment
import ua.acclorite.book_story.ui.theme.model.PureDark
import ua.acclorite.book_story.ui.theme.model.Theme
import ua.acclorite.book_story.ui.theme.model.ThemeContrast
import java.util.Locale
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("UNCHECKED_CAST")
@Singleton
class SettingsManager @Inject constructor(
    private val dataStore: DataStore
) {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val settingsCount = AtomicInteger(0)
    private val initializedSettingsCount = AtomicInteger(0)

    private val _initialized = MutableStateFlow(false)
    val initialized = _initialized.asStateFlow()


    /* ------ Settings --------------------------- */
    /* ------ General ---------------------------- */
    val language = setting<Language, String>(
        key = stringPreferencesKey("language"),
        default = LanguageUtils.findLanguage(
            languages = CoreData.languages,
            locale = Locale.getDefault(),
            defaultLanguage = CoreData.defaultLanguage
        ),
        serialize = { it.locale.toLanguageTag() },
        deserialize = { languageTag ->
            val locale = Locale.forLanguageTag(languageTag)
            LanguageUtils.findLanguage(
                languages = CoreData.languages,
                locale = locale,
                defaultLanguage = CoreData.defaultLanguage
            )
        }
    )
    val theme = setting<Theme, String>(
        key = stringPreferencesKey("theme"), default = Theme.entries().first(),
        serialize = { it.name }, deserialize = { Theme.valueOf(it) }
    )
    val darkTheme = setting<DarkTheme, String>(
        key = stringPreferencesKey("dark_theme"), default = DarkTheme.FOLLOW_SYSTEM,
        serialize = { it.name }, deserialize = { DarkTheme.valueOf(it) }
    )
    val pureDark = setting<PureDark, String>(
        key = stringPreferencesKey("pure_dark"), default = PureDark.OFF,
        serialize = { it.name }, deserialize = { PureDark.valueOf(it) }
    )
    val absoluteDark = setting<Boolean, Boolean>(
        key = booleanPreferencesKey("absolute_dark"), default = false
    )
    val themeContrast = setting<ThemeContrast, String>(
        key = stringPreferencesKey("theme_contrast"), default = ThemeContrast.STANDARD,
        serialize = { it.name }, deserialize = { ThemeContrast.valueOf(it) }
    )
    val showStartScreen = setting<Boolean, Boolean>(
        key = booleanPreferencesKey("guide"), default = true
    )
    val doublePressExit = setting<Boolean, Boolean>(
        key = booleanPreferencesKey("double_press_exit"), default = false
    )
    val showNavigationLabels = setting<Boolean, Boolean>(
        key = booleanPreferencesKey("show_navigation_labels"), default = true
    )

    /* ------ Reader ----------------------------- */
    val fontFamily = setting<FontWithName, String>(
        key = stringPreferencesKey("font"), default = ReaderData.fonts[0],
        serialize = { it.id }, deserialize = { id ->
            ReaderData.fonts.find { it.id == id } ?: ReaderData.fonts[0]
        }
    )
    val fontThickness = setting<ReaderFontThickness, String>(
        key = stringPreferencesKey("font_thickness"), default = ReaderFontThickness.NORMAL,
        serialize = { it.name }, deserialize = { ReaderFontThickness.valueOf(it) }
    )
    val italic = setting<Boolean, Boolean>(
        key = booleanPreferencesKey("font_style"), default = false
    )
    val fontSize = setting<Int, Int>(
        key = intPreferencesKey("font_size"), default = 16
    )
    val lineHeight = setting<Int, Int>(
        key = intPreferencesKey("line_height"), default = 4
    )
    val paragraphHeight = setting<Int, Int>(
        key = intPreferencesKey("paragraph_height"), default = 8
    )
    val paragraphIndentation = setting<Int, Int>(
        key = intPreferencesKey("paragraph_indentation_int"), default = 0
    )
    val sidePadding = setting<Int, Int>(
        key = intPreferencesKey("side_padding"), default = 6
    )
    val verticalPadding = setting<Int, Int>(
        key = intPreferencesKey("vertical_padding"), default = 0
    )
    val doubleClickTranslation = setting<Boolean, Boolean>(
        key = booleanPreferencesKey("double_click_translation"), default = false
    )
    val fastColorPresetChange = setting<Boolean, Boolean>(
        key = booleanPreferencesKey("fast_color_preset_change"), default = true
    )
    val textAlignment = setting<ReaderTextAlignment, String>(
        key = stringPreferencesKey("text_alignment"), default = ReaderTextAlignment.JUSTIFY,
        serialize = { it.name }, deserialize = { ReaderTextAlignment.valueOf(it) }
    )
    val letterSpacing = setting<Int, Int>(
        key = intPreferencesKey("letter_spacing"), default = 0
    )
    val cutoutPadding = setting<Boolean, Boolean>(
        key = booleanPreferencesKey("cutout_padding"), default = false
    )
    val fullscreen = setting<Boolean, Boolean>(
        key = booleanPreferencesKey("fullscreen"), default = true
    )
    val keepScreenOn = setting<Boolean, Boolean>(
        key = booleanPreferencesKey("keep_screen_on"), default = true
    )
    val hideBarsOnFastScroll = setting<Boolean, Boolean>(
        key = booleanPreferencesKey("hide_bars_on_fast_scroll"), default = false
    )
    val perceptionExpander = setting<Boolean, Boolean>(
        key = booleanPreferencesKey("perception_expander"), default = false
    )
    val perceptionExpanderPadding = setting<Int, Int>(
        key = intPreferencesKey("perception_expander_padding"), default = 5
    )
    val perceptionExpanderThickness = setting<Int, Int>(
        key = intPreferencesKey("perception_expander_thickness"), default = 4
    )
    val screenOrientation = setting<ReaderScreenOrientation, String>(
        key = stringPreferencesKey("screen_orientation"), default = ReaderScreenOrientation.DEFAULT,
        serialize = { it.name }, deserialize = { ReaderScreenOrientation.valueOf(it) }
    )
    val customScreenBrightness = setting<Boolean, Boolean>(
        key = booleanPreferencesKey("custom_screen_brightness"), default = false
    )
    val screenBrightness = setting<Float, Double>(
        key = doublePreferencesKey("screen_brightness"), default = 0.5f,
        serialize = { it.toDouble() }, deserialize = { it.toFloat() }
    )
    val horizontalGesture = setting<ReaderHorizontalGesture, String>(
        key = stringPreferencesKey("horizontal_gesture"), default = ReaderHorizontalGesture.OFF,
        serialize = { it.name }, deserialize = { ReaderHorizontalGesture.valueOf(it) }
    )
    val horizontalGestureScroll = setting<Float, Double>(
        key = doublePreferencesKey("horizontal_gesture_scroll"), default = 0.7f,
        serialize = { it.toDouble() }, deserialize = { it.toFloat() }
    )
    val horizontalGestureSensitivity = setting<Float, Double>(
        key = doublePreferencesKey("horizontal_gesture_sensitivity"), default = 0.6f,
        serialize = { it.toDouble() }, deserialize = { it.toFloat() }
    )
    val horizontalGestureAlphaAnim = setting<Boolean, Boolean>(
        key = booleanPreferencesKey("horizontal_gesture_alpha_anim_bool"), default = true
    )
    val horizontalGesturePullAnim = setting<Boolean, Boolean>(
        key = booleanPreferencesKey("horizontal_gesture_pull_anim"), default = true
    )
    val horizontalGestureDisableScrolling = setting<Boolean, Boolean>(
        key = booleanPreferencesKey("horizontal_gesture_disable_scrolling"), default = false
    )
    val bottomBarPadding = setting<Int, Int>(
        key = intPreferencesKey("bottom_bar_padding"), default = 0
    )
    val highlightedReading = setting<Boolean, Boolean>(
        key = booleanPreferencesKey("highlighted_reading"), default = false
    )
    val highlightedReadingThickness = setting<Int, Int>(
        key = intPreferencesKey("highlighted_reading_thickness"), default = 2
    )
    val chapterTitleAlignment = setting<ReaderTextAlignment, String>(
        key = stringPreferencesKey("chapter_title_alignment"),
        default = ReaderTextAlignment.JUSTIFY,
        serialize = { it.name }, deserialize = { ReaderTextAlignment.valueOf(it) }
    )
    val images = setting<Boolean, Boolean>(
        key = booleanPreferencesKey("images"), default = true
    )
    val imagesCaptions = setting<Boolean, Boolean>(
        key = booleanPreferencesKey("images_captions"), default = true
    )
    val imagesCornersRoundness = setting<Int, Int>(
        key = intPreferencesKey("images_corners_roundness"), default = 8
    )
    val imagesAlignment = setting<HorizontalAlignment, String>(
        key = stringPreferencesKey("images_alignment"), default = HorizontalAlignment.START,
        serialize = { it.name }, deserialize = { HorizontalAlignment.valueOf(it) }
    )
    val imagesWidth = setting<Float, Double>(
        key = doublePreferencesKey("images_width"), default = 0.8f,
        serialize = { it.toDouble() }, deserialize = { it.toFloat() }
    )
    val imagesColorEffects = setting<ReaderColorEffects, String>(
        key = stringPreferencesKey("images_color_effects"), default = ReaderColorEffects.OFF,
        serialize = { it.name }, deserialize = { ReaderColorEffects.valueOf(it) }
    )
    val progressBar = setting<Boolean, Boolean>(
        key = booleanPreferencesKey("progress_bar"), default = false
    )
    val progressBarPadding = setting<Int, Int>(
        key = intPreferencesKey("progress_bar_padding"), default = 4
    )
    val progressBarAlignment = setting<HorizontalAlignment, String>(
        key = stringPreferencesKey("progress_bar_alignment"), default = HorizontalAlignment.CENTER,
        serialize = { it.name }, deserialize = { HorizontalAlignment.valueOf(it) }
    )
    val progressBarFontSize = setting<Int, Int>(
        key = intPreferencesKey("progress_bar_font_size"), default = 8
    )
    val progressCount = setting<ReaderProgressCount, String>(
        key = stringPreferencesKey("progress_count"), default = ReaderProgressCount.PERCENTAGE,
        serialize = { it.name }, deserialize = { ReaderProgressCount.valueOf(it) }
    )

    /* ------ Library ---------------------------- */
    val libraryLayout = setting<LibraryLayout, String>(
        key = stringPreferencesKey("library_layout"), default = LibraryLayout.GRID,
        serialize = { it.name }, deserialize = { LibraryLayout.valueOf(it) }
    )
    val libraryAutoGridSize = setting<Boolean, Boolean>(
        key = booleanPreferencesKey("library_auto_grid_size"), default = true
    )
    val libraryGridSize = setting<Int, Int>(
        key = intPreferencesKey("library_grid_size"), default = 0
    )
    val libraryTitlePosition = setting<LibraryTitlePosition, String>(
        key = stringPreferencesKey("library_title_position"), default = LibraryTitlePosition.BELOW,
        serialize = { it.name }, deserialize = { LibraryTitlePosition.valueOf(it) }
    )
    val libraryShowReadButton = setting<Boolean, Boolean>(
        key = booleanPreferencesKey("library_show_read_button"), default = true
    )
    val libraryShowProgress = setting<Boolean, Boolean>(
        key = booleanPreferencesKey("library_show_progress"), default = true
    )
    val libraryShowBookCount = setting<Boolean, Boolean>(
        key = booleanPreferencesKey("library_show_book_count"), default = true
    )
    val libraryShowCategoryTabs = setting<Boolean, Boolean>(
        key = booleanPreferencesKey("library_show_category_tabs"), default = true
    )
    val libraryShowDefaultTab = setting<Boolean, Boolean>(
        key = booleanPreferencesKey("library_show_default_tab"), default = false
    )
    val librarySortOrder = setting<LibrarySortOrder, String>(
        key = stringPreferencesKey("library_sort_order"), default = LibrarySortOrder.LAST_READ,
        serialize = { it.name }, deserialize = { LibrarySortOrder.valueOf(it) }
    )
    val librarySortOrderDescending = setting<Boolean, Boolean>(
        key = booleanPreferencesKey("library_sort_order_descending"), default = true
    )
    val libraryPerCategorySort = setting<Boolean, Boolean>(
        key = booleanPreferencesKey("library_per_category_sort"), default = false
    )

    /* ------ Browse ----------------------------- */
    val browseLayout = setting<BrowseLayout, String>(
        key = stringPreferencesKey("browse_layout"), default = BrowseLayout.LIST,
        serialize = { it.name }, deserialize = { BrowseLayout.valueOf(it) }
    )
    val browseAutoGridSize = setting<Boolean, Boolean>(
        key = booleanPreferencesKey("browse_auto_grid_size"), default = true
    )
    val browseGridSize = setting<Int, Int>(
        key = intPreferencesKey("browse_grid_size"), default = 0
    )
    val browseSortOrder = setting<BrowseSortOrder, String>(
        key = stringPreferencesKey("browse_sort_order"), default = BrowseSortOrder.LAST_MODIFIED,
        serialize = { it.name }, deserialize = { BrowseSortOrder.valueOf(it) }
    )
    val browseSortOrderDescending = setting<Boolean, Boolean>(
        key = booleanPreferencesKey("browse_sort_order_descending"), default = true
    )
    val browseIncludedFilterItems = setting<List<String>, Set<String>>(
        key = stringSetPreferencesKey("browse_included_filter_items"), default = emptyList(),
        serialize = { it.toSet() }, deserialize = { it.toList() }
    )
    val browsePinnedPaths = setting<List<String>, Set<String>>(
        key = stringSetPreferencesKey("browse_pinned_paths"), default = emptyList(),
        serialize = { it.toSet() }, deserialize = { it.toList() }
    )
    /* - - - - - - - - - - - - - - - - - - - - - - */


    private fun <T, P> setting(
        key: Preferences.Key<P>,
        default: T,
        serialize: (T) -> P = { it as P },
        deserialize: (P) -> T = { it as T }
    ): Setting<T, P> {
        settingsCount.incrementAndGet()

        return Setting<T, P>(
            key = key,
            default = default,
            setSetting = {
                scope.launch {
                    logI("Updating setting: [${key.name}].")
                    dataStore.putData(key, it)
                }
            },
            serialize = serialize,
            deserialize = deserialize
        ).also { setting ->
            scope.launch {
                setting.init(dataStore.getNullableData<P>(key))
                logI("Successfully initialized setting: [${key.name}].")
                initializeSetting()
            }
        }
    }

    private fun initializeSetting() {
        if (initializedSettingsCount.incrementAndGet() == settingsCount.get()) {
            logI("Successfully initialized all $settingsCount settings.")
            _initialized.update { true }
        }
    }
}