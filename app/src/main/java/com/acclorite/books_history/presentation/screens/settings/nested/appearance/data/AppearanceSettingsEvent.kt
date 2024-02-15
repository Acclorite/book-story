package com.acclorite.books_history.presentation.screens.settings.nested.appearance.data

sealed class AppearanceSettingsEvent() {
    data object OnShowHideDarkThemeDialog : AppearanceSettingsEvent()
}