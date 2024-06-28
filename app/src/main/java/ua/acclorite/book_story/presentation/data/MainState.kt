package ua.acclorite.book_story.presentation.data

import android.os.Build
import android.os.Parcelable
import androidx.annotation.Keep
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.google.mlkit.nl.translate.TranslateLanguage
import kotlinx.parcelize.Parcelize
import ua.acclorite.book_story.domain.util.Constants
import ua.acclorite.book_story.domain.util.DataStoreConstants
import ua.acclorite.book_story.presentation.ui.DarkTheme
import ua.acclorite.book_story.presentation.ui.PureDark
import ua.acclorite.book_story.presentation.ui.Theme
import ua.acclorite.book_story.presentation.ui.ThemeContrast
import ua.acclorite.book_story.presentation.ui.toDarkTheme
import ua.acclorite.book_story.presentation.ui.toPureDark
import ua.acclorite.book_story.presentation.ui.toTheme
import ua.acclorite.book_story.presentation.ui.toThemeContrast
import java.util.Locale

/**
 * Main State.
 * All app's settings/preferences/permanent-variables are here.
 * Wrapped in SavedStateHandle, so it won't reset.
 */
@Immutable
@Keep
@Parcelize
data class MainState(
    val language: String? = null,
    val theme: Theme? = null,
    val darkTheme: DarkTheme? = null,
    val pureDark: PureDark? = null,
    val themeContrast: ThemeContrast? = null,
    val fontFamily: String? = null,
    val isItalic: Boolean? = null,
    val fontSize: Int? = null,
    val lineHeight: Int? = null,
    val paragraphHeight: Int? = null,
    val paragraphIndentation: Boolean? = null,
    val backgroundColor: Long? = null,
    val fontColor: Long? = null,
    val showStartScreen: Boolean? = null,
    val checkForUpdates: Boolean? = null,
    val sidePadding: Int? = null,
    val enableTranslator: Boolean? = null,
    val translateFrom: String? = null,
    val translateTo: String? = null,
    val doubleClickTranslation: Boolean? = null
) : Parcelable {
    companion object {
        /**
         * Initializes [MainState] by given [Map].
         */
        fun initialize(data: Map<String, Any>): MainState {
            DataStoreConstants.apply {
                val language: String = data[LANGUAGE.name] as? String ?: if (
                    Constants.LANGUAGES.any { Locale.getDefault().language.take(2) == it.first }
                ) {
                    Locale.getDefault().language.take(2)
                } else {
                    "en"
                }

                val theme: String = data[THEME.name] as? String ?: if (
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                ) Theme.DYNAMIC.name else Theme.BLUE.name

                val darkTheme: String = data[DARK_THEME.name] as? String
                    ?: DarkTheme.FOLLOW_SYSTEM.name

                val pureDark: String = data[PURE_DARK.name] as? String
                    ?: PureDark.OFF.name

                val themeContrast: String = data[THEME_CONTRAST.name] as? String
                    ?: ThemeContrast.STANDARD.name

                val showStartScreen: Boolean = data[SHOW_START_SCREEN.name] as? Boolean
                    ?: true

                val backgroundColor: Long = data[BACKGROUND_COLOR.name] as? Long
                    ?: Color.DarkGray.value.toLong()

                val fontColor: Long = data[FONT_COLOR.name] as? Long
                    ?: Color.LightGray.value.toLong()

                val fontFamily: String = data[FONT.name] as? String
                    ?: Constants.FONTS[0].id

                val isItalic: Boolean = data[IS_ITALIC.name] as? Boolean
                    ?: false

                val fontSize: Int = data[FONT_SIZE.name] as? Int
                    ?: 16

                val lineHeight: Int = data[LINE_HEIGHT.name] as? Int
                    ?: 4

                val paragraphHeight: Int = data[PARAGRAPH_HEIGHT.name] as? Int
                    ?: 8

                val paragraphIndentation: Boolean = data[PARAGRAPH_INDENTATION.name] as? Boolean
                    ?: false

                val checkForUpdates: Boolean = data[CHECK_FOR_UPDATES.name] as? Boolean
                    ?: false

                val sidePadding: Int = data[SIDE_PADDING.name] as? Int
                    ?: 6

                val enableTranslator: Boolean = data[ENABLE_TRANSLATOR.name] as? Boolean
                    ?: false

                val translateFrom: String = data[TRANSLATE_FROM.name] as? String
                    ?: "auto"

                val translateTo: String = data[TRANSLATE_TO.name] as? String ?: if (
                    TranslateLanguage.getAllLanguages().any { it == language }
                ) {
                    language
                } else {
                    "en"
                }

                val doubleClickTranslation: Boolean =
                    data[DOUBLE_CLICK_TRANSLATION.name] as? Boolean
                        ?: true

                return MainState(
                    language = language,
                    theme = theme.toTheme(),
                    darkTheme = darkTheme.toDarkTheme(),
                    pureDark = pureDark.toPureDark(),
                    themeContrast = themeContrast.toThemeContrast(),
                    showStartScreen = showStartScreen,
                    backgroundColor = backgroundColor,
                    fontColor = fontColor,
                    fontFamily = fontFamily,
                    isItalic = isItalic,
                    fontSize = fontSize,
                    lineHeight = lineHeight,
                    paragraphHeight = paragraphHeight,
                    paragraphIndentation = paragraphIndentation,
                    checkForUpdates = checkForUpdates,
                    sidePadding = sidePadding,
                    enableTranslator = enableTranslator,
                    translateFrom = translateFrom,
                    translateTo = translateTo,
                    doubleClickTranslation = doubleClickTranslation
                )
            }
        }
    }
}