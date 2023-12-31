package com.acclorite.books_history.util

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.acclorite.books_history.R
import com.acclorite.books_history.domain.model.FontWithName

object Constants {

    const val LANGUAGE = "language"
    const val THEME = "theme"
    const val DARK_THEME = "dark_theme"
    const val SHOW_START_SCREEN = "guide"
    const val BACKGROUND_COLOR = "background_color"
    const val FONT_COLOR = "font_color"
    const val FONT = "font"
    const val IS_ITALIC = "font_style"
    const val FONT_SIZE = "font_size"
    const val LINE_HEIGHT = "line_height"
    const val PARAGRAPH_HEIGHT = "paragraph_height"
    const val PARAGRAPH_INDENTATION = "paragraph_indentation"

    val FONTS = listOf(
        FontWithName(
            "Raleway",
            FontFamily(
                Font(R.font.raleway_regular),
                Font(R.font.raleway_regular_italic, style = FontStyle.Italic),
                Font(R.font.raleway_medium, FontWeight.Medium),
                Font(R.font.raleway_medium_italic, FontWeight.Medium, style = FontStyle.Italic),
                Font(R.font.raleway_bold, FontWeight.Bold),
                Font(R.font.raleway_bold_italic, FontWeight.Bold, style = FontStyle.Italic)
            )
        ),
        FontWithName(
            "Open Sans",
            FontFamily(
                Font(R.font.opensans_regular),
                Font(R.font.opensans_regular_italic, style = FontStyle.Italic),
                Font(R.font.opensans_medium, FontWeight.Medium),
                Font(R.font.opensans_medium_italic, FontWeight.Medium, style = FontStyle.Italic),
                Font(R.font.opensans_bold, FontWeight.Bold),
                Font(R.font.opensans_bold_italic, FontWeight.Bold, style = FontStyle.Italic)
            )
        ),
        FontWithName(
            "Mulish",
            FontFamily(
                Font(R.font.mulish_regular),
                Font(R.font.mulish_regular_italic, style = FontStyle.Italic),
                Font(R.font.mulish_medium, FontWeight.Medium),
                Font(R.font.mulish_medium_italic, FontWeight.Medium, style = FontStyle.Italic),
                Font(R.font.mulish_bold, FontWeight.Bold),
                Font(R.font.mulish_bold_italic, FontWeight.Bold, style = FontStyle.Italic)
            )
        ),
        FontWithName(
            "Arimo",
            FontFamily(
                Font(R.font.arimo_regular),
                Font(R.font.arimo_regular_italic, style = FontStyle.Italic),
                Font(R.font.arimo_medium, FontWeight.Medium),
                Font(R.font.arimo_medium_italic, FontWeight.Medium, style = FontStyle.Italic),
                Font(R.font.arimo_bold, FontWeight.Bold),
                Font(R.font.arimo_bold_italic, FontWeight.Bold, style = FontStyle.Italic)
            )
        ),
        FontWithName(
            "Garamond",
            FontFamily(
                Font(R.font.garamond_regular),
                Font(R.font.garamond_regular_italic, style = FontStyle.Italic),
                Font(R.font.garamond_medium, FontWeight.Medium),
                Font(R.font.garamond_medium_italic, FontWeight.Medium, style = FontStyle.Italic),
                Font(R.font.garamond_bold, FontWeight.Bold),
                Font(R.font.garamond_bold_italic, FontWeight.Bold, style = FontStyle.Italic)
            )
        ),
        FontWithName(
            "Roboto Serif",
            FontFamily(
                Font(R.font.robotoserif_regular),
                Font(R.font.robotoserif_regular_italic, style = FontStyle.Italic),
                Font(R.font.robotoserif_medium, FontWeight.Medium),
                Font(R.font.robotoserif_medium_italic, FontWeight.Medium, style = FontStyle.Italic),
                Font(R.font.robotoserif_bold, FontWeight.Bold),
                Font(R.font.robotoserif_bold_italic, FontWeight.Bold, style = FontStyle.Italic)
            )
        ),
        FontWithName(
            "Noto Serif",
            FontFamily(
                Font(R.font.notoserif_regular),
                Font(R.font.notoserif_regular_italic, style = FontStyle.Italic),
                Font(R.font.notoserif_medium, FontWeight.Medium),
                Font(R.font.notoserif_medium_italic, FontWeight.Medium, style = FontStyle.Italic),
                Font(R.font.notoserif_bold, FontWeight.Bold),
                Font(R.font.notoserif_bold_italic, FontWeight.Bold, style = FontStyle.Italic)
            )
        ),
        FontWithName(
            "Noto Sans",
            FontFamily(
                Font(R.font.notosans_regular),
                Font(R.font.notosans_regular_italic, style = FontStyle.Italic),
                Font(R.font.notosans_medium, FontWeight.Medium),
                Font(R.font.notosans_medium_italic, FontWeight.Medium, style = FontStyle.Italic),
                Font(R.font.notosans_bold, FontWeight.Bold),
                Font(R.font.notosans_bold_italic, FontWeight.Bold, style = FontStyle.Italic)
            )
        ),
        FontWithName(
            "Roboto",
            FontFamily(
                Font(R.font.roboto_regular),
                Font(R.font.roboto_regular_italic, style = FontStyle.Italic),
                Font(R.font.roboto_medium, FontWeight.Medium),
                Font(R.font.roboto_medium_italic, FontWeight.Medium, style = FontStyle.Italic),
                Font(R.font.roboto_bold, FontWeight.Bold),
                Font(R.font.roboto_bold_italic, FontWeight.Bold, style = FontStyle.Italic)
            )
        ),
        FontWithName(
            "Jost",
            FontFamily(
                Font(R.font.jost_regular),
                Font(R.font.jost_regular_italic, style = FontStyle.Italic),
                Font(R.font.jost_medium, FontWeight.Medium),
                Font(R.font.jost_medium_italic, FontWeight.Medium, style = FontStyle.Italic),
                Font(R.font.jost_bold, FontWeight.Bold),
                Font(R.font.jost_bold_italic, FontWeight.Bold, style = FontStyle.Italic)
            )
        )
    )
}