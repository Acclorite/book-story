package com.acclorite.books_history.util

import androidx.datastore.preferences.core.stringPreferencesKey

object DataStoreConstants {
    val LANGUAGE = stringPreferencesKey("language")
    val THEME = stringPreferencesKey("theme")
    val DARK_THEME = stringPreferencesKey("dark_theme")
    val GUIDE = stringPreferencesKey("guide")

    val BACKGROUND_COLOR = stringPreferencesKey("background_color")
    val FONT_COLOR = stringPreferencesKey("font_color")
    val FONT = stringPreferencesKey("font")
    val FONT_STYLE = stringPreferencesKey("font_style")
    val FONT_SIZE = stringPreferencesKey("font_size")
    val LINE_HEIGHT = stringPreferencesKey("line_height")
    val PARAGRAPH_HEIGHT = stringPreferencesKey("paragraph_height")
    val PARAGRAPH_INDENTATION = stringPreferencesKey("paragraph_indentation")
}