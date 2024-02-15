package com.acclorite.books_history.presentation.screens.settings.nested.general.data

sealed class GeneralSettingsEvent() {
    data object OnShowHideLanguageDialog : GeneralSettingsEvent()
}