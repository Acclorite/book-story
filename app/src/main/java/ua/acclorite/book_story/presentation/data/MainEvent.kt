package ua.acclorite.book_story.presentation.data

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
    data class OnChangeBackgroundColor(val color: Long) : MainEvent()
    data class OnChangeFontColor(val color: Long) : MainEvent()
    data class OnChangeShowStartScreen(val bool: Boolean) : MainEvent()
    data class OnChangeCheckForUpdates(val bool: Boolean) : MainEvent()
}