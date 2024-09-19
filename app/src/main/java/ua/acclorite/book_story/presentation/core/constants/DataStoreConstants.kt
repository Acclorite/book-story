package ua.acclorite.book_story.presentation.core.constants

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

object DataStoreConstants {
    // General settings
    val LANGUAGE = stringPreferencesKey("language")
    val THEME = stringPreferencesKey("theme")
    val DARK_THEME = stringPreferencesKey("dark_theme")
    val PURE_DARK = stringPreferencesKey("pure_dark")
    val ABSOLUTE_DARK = booleanPreferencesKey("absolute_dark")
    val THEME_CONTRAST = stringPreferencesKey("theme_contrast")
    val SHOW_START_SCREEN = booleanPreferencesKey("guide")
    val CHECK_FOR_UPDATES = booleanPreferencesKey("check_for_updates")
    val DOUBLE_PRESS_EXIT = booleanPreferencesKey("double_press_exit")

    // Reader settings
    val DOUBLE_CLICK_TRANSLATION = booleanPreferencesKey("double_click_translation")
    val FAST_COLOR_PRESET_CHANGE = booleanPreferencesKey("fast_color_preset_change")
    val SIDE_PADDING = intPreferencesKey("side_padding")
    val VERTICAL_PADDING = intPreferencesKey("vertical_padding")
    val FONT = stringPreferencesKey("font")
    val IS_ITALIC = booleanPreferencesKey("font_style")
    val FONT_SIZE = intPreferencesKey("font_size")
    val LINE_HEIGHT = intPreferencesKey("line_height")
    val PARAGRAPH_HEIGHT = intPreferencesKey("paragraph_height")
    val PARAGRAPH_INDENTATION = intPreferencesKey("paragraph_indentation_int")
    val TEXT_ALIGNMENT = stringPreferencesKey("text_alignment")
    val LETTER_SPACING = intPreferencesKey("letter_spacing")
    val CUTOUT_PADDING = booleanPreferencesKey("cutout_padding")
    val FULLSCREEN = booleanPreferencesKey("fullscreen")
    val KEEP_SCREEN_ON = booleanPreferencesKey("keep_screen_on")
    val HIDE_BARS_ON_FAST_SCROLL = booleanPreferencesKey("hide_bars_on_fast_scroll")
    val PERCEPTION_EXPANDER = booleanPreferencesKey("perception_expander")
    val PERCEPTION_EXPANDER_PADDING = intPreferencesKey("perception_expander_padding")
    val PERCEPTION_EXPANDER_THICKNESS = intPreferencesKey("perception_expander_thickness")

    // Browse settings
    val BROWSE_FILES_STRUCTURE = stringPreferencesKey("browse_files_structure")
    val BROWSE_LAYOUT = stringPreferencesKey("browse_layout")
    val BROWSE_AUTO_GRID_SIZE = booleanPreferencesKey("browse_auto_grid_size")
    val BROWSE_GRID_SIZE = intPreferencesKey("browse_grid_size")
    val BROWSE_PIN_FAVORITE_DIRECTORIES = booleanPreferencesKey("browse_pin_favorite_directories")
    val BROWSE_SORT_ORDER = stringPreferencesKey("browse_sort_order")
    val BROWSE_SORT_ORDER_DESCENDING = booleanPreferencesKey("browse_sort_order_descending")
    val BROWSE_INCLUDED_FILTER_ITEMS = stringSetPreferencesKey("browse_included_filter_items")
}