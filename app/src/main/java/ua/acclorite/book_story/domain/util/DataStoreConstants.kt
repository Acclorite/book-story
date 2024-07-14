package ua.acclorite.book_story.domain.util

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object DataStoreConstants {
    // App settings
    val LANGUAGE = stringPreferencesKey("language")
    val THEME = stringPreferencesKey("theme")
    val DARK_THEME = stringPreferencesKey("dark_theme")
    val PURE_DARK = stringPreferencesKey("pure_dark")
    val THEME_CONTRAST = stringPreferencesKey("theme_contrast")
    val SHOW_START_SCREEN = booleanPreferencesKey("guide")
    val BACKGROUND_COLOR = longPreferencesKey("background_color")
    val FONT_COLOR = longPreferencesKey("font_color")
    val FONT = stringPreferencesKey("font")
    val IS_ITALIC = booleanPreferencesKey("font_style")
    val FONT_SIZE = intPreferencesKey("font_size")
    val LINE_HEIGHT = intPreferencesKey("line_height")
    val PARAGRAPH_HEIGHT = intPreferencesKey("paragraph_height")
    val PARAGRAPH_INDENTATION = booleanPreferencesKey("paragraph_indentation")
    val CHECK_FOR_UPDATES = booleanPreferencesKey("check_for_updates")
    val SIDE_PADDING = intPreferencesKey("side_padding")
    val DOUBLE_CLICK_TRANSLATION = booleanPreferencesKey("double_click_translation")
}