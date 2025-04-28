/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.core.constants

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
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
    val DOUBLE_PRESS_EXIT = booleanPreferencesKey("double_press_exit")

    // Reader settings
    val DOUBLE_CLICK_TRANSLATION = booleanPreferencesKey("double_click_translation")
    val FAST_COLOR_PRESET_CHANGE = booleanPreferencesKey("fast_color_preset_change")
    val SIDE_PADDING = intPreferencesKey("side_padding")
    val VERTICAL_PADDING = intPreferencesKey("vertical_padding")
    val FONT = stringPreferencesKey("font")
    val FONT_THICKNESS = stringPreferencesKey("font_thickness")
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
    val SCREEN_ORIENTATION = stringPreferencesKey("screen_orientation")
    val CUSTOM_SCREEN_BRIGHTNESS = booleanPreferencesKey("custom_screen_brightness")
    val SCREEN_BRIGHTNESS = doublePreferencesKey("screen_brightness")
    val HORIZONTAL_GESTURE = stringPreferencesKey("horizontal_gesture")
    val HORIZONTAL_GESTURE_SCROLL = doublePreferencesKey("horizontal_gesture_scroll")
    val HORIZONTAL_GESTURE_SENSITIVITY = doublePreferencesKey("horizontal_gesture_sensitivity")
    val HORIZONTAL_GESTURE_ALPHA_ANIM = booleanPreferencesKey("horizontal_gesture_alpha_anim_bool")
    val HORIZONTAL_GESTURE_PULL_ANIM = booleanPreferencesKey("horizontal_gesture_pull_anim")
    val BOTTOM_BAR_PADDING = intPreferencesKey("bottom_bar_padding")
    val HIGHLIGHTED_READING = booleanPreferencesKey("highlighted_reading")
    val HIGHLIGHTED_READING_THICKNESS = intPreferencesKey("highlighted_reading_thickness")
    val CHAPTER_TITLE_ALIGNMENT = stringPreferencesKey("chapter_title_alignment")
    val IMAGES = booleanPreferencesKey("images")
    val IMAGES_CORNERS_ROUNDNESS = intPreferencesKey("images_corners_roundness")
    val IMAGES_ALIGNMENT = stringPreferencesKey("images_alignment")
    val IMAGES_WIDTH = doublePreferencesKey("images_width")
    val IMAGES_COLOR_EFFECTS = stringPreferencesKey("images_color_effects")
    val PROGRESS_BAR = booleanPreferencesKey("progress_bar")
    val PROGRESS_BAR_PADDING = intPreferencesKey("progress_bar_padding")
    val PROGRESS_BAR_ALIGNMENT = stringPreferencesKey("progress_bar_alignment")
    val PROGRESS_BAR_FONT_SIZE = intPreferencesKey("progress_bar_font_size")
    val PROGRESS_COUNT = stringPreferencesKey("progress_count")

    // Library settings
    val LIBRARY_LAYOUT = stringPreferencesKey("library_layout")
    val LIBRARY_AUTO_GRID_SIZE = booleanPreferencesKey("library_auto_grid_size")
    val LIBRARY_GRID_SIZE = intPreferencesKey("library_grid_size")
    val LIBRARY_READ_BUTTON = booleanPreferencesKey("library_read_button")
    val LIBRARY_SHOW_PROGRESS = booleanPreferencesKey("library_show_progress")
    val LIBRARY_TITLE_POSITION = stringPreferencesKey("library_title_position")
    val LIBRARY_SHOW_BOOK_COUNT = booleanPreferencesKey("library_show_book_count")
    val LIBRARY_SHOW_CATEGORY_TABS = booleanPreferencesKey("library_show_category_tabs")
    val LIBRARY_ALWAYS_SHOW_DEFAULT_TAB = booleanPreferencesKey("library_always_show_default_tab")
    val LIBRARY_SORT_ORDER = stringPreferencesKey("library_sort_order")
    val LIBRARY_SORT_ORDER_DESCENDING = booleanPreferencesKey("library_sort_order_descending")
    val LIBRARY_PER_CATEGORY_SORT = booleanPreferencesKey("library_per_category_sort")

    // Browse settings
    val BROWSE_LAYOUT = stringPreferencesKey("browse_layout")
    val BROWSE_AUTO_GRID_SIZE = booleanPreferencesKey("browse_auto_grid_size")
    val BROWSE_GRID_SIZE = intPreferencesKey("browse_grid_size")
    val BROWSE_SORT_ORDER = stringPreferencesKey("browse_sort_order")
    val BROWSE_SORT_ORDER_DESCENDING = booleanPreferencesKey("browse_sort_order_descending")
    val BROWSE_INCLUDED_FILTER_ITEMS = stringSetPreferencesKey("browse_included_filter_items")
    val BROWSE_PINNED_PATHS = stringSetPreferencesKey("browse_pinned_paths")
}