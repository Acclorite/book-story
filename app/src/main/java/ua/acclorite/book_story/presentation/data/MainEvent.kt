package ua.acclorite.book_story.presentation.data

import androidx.compose.runtime.Immutable

@Immutable
sealed class MainEvent {
    data class OnChangeLanguage(val lang: String) : MainEvent()
    data class OnChangeTheme(val theme: String) : MainEvent()
    data class OnChangeDarkTheme(val darkTheme: String) : MainEvent()
    data class OnChangePureDark(val pureDark: String) : MainEvent()
    data class OnChangeThemeContrast(val themeContrast: String) : MainEvent()
    data class OnChangeFontFamily(val fontFamily: String) : MainEvent()
    data class OnChangeFontStyle(val fontStyle: Boolean) : MainEvent()
    data class OnChangeFontSize(val fontSize: Int) : MainEvent()
    data class OnChangeLineHeight(val lineHeight: Int) : MainEvent()
    data class OnChangeParagraphHeight(val paragraphHeight: Int) : MainEvent()
    data class OnChangeParagraphIndentation(val bool: Boolean) : MainEvent()
    data class OnChangeShowStartScreen(val bool: Boolean) : MainEvent()
    data class OnChangeCheckForUpdates(val bool: Boolean) : MainEvent()
    data class OnChangeSidePadding(val sidePadding: Int) : MainEvent()
    data class OnChangeDoubleClickTranslation(val bool: Boolean) : MainEvent()
    data class OnChangeFastColorPresetChange(val bool: Boolean) : MainEvent()
    data class OnChangeBrowseFilesStructure(val structure: String) : MainEvent()
    data class OnChangeBrowseLayout(val layout: String) : MainEvent()
    data class OnChangeBrowseAutoGridSize(val bool: Boolean) : MainEvent()
    data class OnChangeBrowseGridSize(val size: Int) : MainEvent()
    data class OnChangeBrowsePinFavoriteDirectories(val bool: Boolean) : MainEvent()
    data class OnChangeBrowseSortOrder(val order: String) : MainEvent()
    data class OnChangeBrowseSortOrderDescending(val bool: Boolean) : MainEvent()
    data class OnChangeBrowseIncludedFilterItem(val item: String) : MainEvent()
    data class OnChangeTextAlignment(val alignment: String) : MainEvent()
}