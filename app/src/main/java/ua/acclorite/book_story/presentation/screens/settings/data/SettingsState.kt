package ua.acclorite.book_story.presentation.screens.settings.data

import androidx.compose.runtime.Immutable

@Immutable
data class SettingsState(
    val showReaderTranslatorLanguageBottomSheet: Boolean = false,
    val readerTranslatorLanguageBottomSheetTranslateFrom: Boolean = false,
)
