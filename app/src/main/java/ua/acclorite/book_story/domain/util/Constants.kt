package ua.acclorite.book_story.domain.util

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.model.Category
import ua.acclorite.book_story.domain.model.Credit
import ua.acclorite.book_story.domain.model.FontWithName
import ua.acclorite.book_story.presentation.ui.Theme

object Constants {

    // Main State Constant
    const val MAIN_STATE = "main_state"

    // Supported file extensions
    val EXTENSIONS = listOf(".txt", ".pdf", ".epub")

    // Supported languages
    val LANGUAGES = listOf(
        Pair("en", "English"),
        Pair("uk", "Українська"),
    )

    // Supported themes
    val THEMES = listOf(
        Pair(Theme.DYNAMIC, UIText.StringResource(R.string.dynamic_theme)),
        Pair(Theme.BLUE, UIText.StringResource(R.string.blue_theme)),
        Pair(Theme.GREEN, UIText.StringResource(R.string.green_theme)),
        Pair(Theme.RED, UIText.StringResource(R.string.red_theme)),
        Pair(Theme.PURPLE, UIText.StringResource(R.string.purple_theme)),
        Pair(Theme.PINK, UIText.StringResource(R.string.pink_theme)),
        Pair(Theme.YELLOW, UIText.StringResource(R.string.yellow_theme)),
        Pair(Theme.AQUA, UIText.StringResource(R.string.aqua_theme)),
    )

    // Credits
    val CREDITS = listOf(
        Credit(
            name = "Tachiyomi (Mihon)",
            source = "GitHub",
            credits = listOf(
                UIText.StringResource(R.string.credits_design),
                UIText.StringValue("Readme")
            ),
            website = "https://github.com/mihonapp/mihon"
        ),
        Credit(
            name = "Libre-Sudoku",
            source = "GitHub",
            credits = listOf(
                UIText.StringValue("Readme")
            ),
            website = "https://github.com/kaajjo/Libre-Sudoku"
        ),
        Credit(
            name = "Kitsune",
            source = "GitHub",
            credits = listOf(
                UIText.StringResource(R.string.credits_updates)
            ),
            website = "https://github.com/Drumber/Kitsune"
        ),
        Credit(
            name = "Freepik",
            source = "Flaticon",
            credits = listOf(
                UIText.StringResource(R.string.credits_icon)
            ),
            website = "https://www.flaticon.com/authors/freepik"
        ),
        Credit(
            name = "Anggara",
            source = "Flaticon",
            credits = listOf(
                UIText.StringResource(R.string.credits_icon)
            ),
            website = "https://www.flaticon.com/authors/anggara"
        ),
        Credit(
            name = "Google",
            source = "Flaticon",
            credits = listOf(
                UIText.StringResource(R.string.credits_icon)
            ),
            website = "https://www.flaticon.com/authors/google"
        ),
        Credit(
            name = "Creative Stall Premium",
            source = "Flaticon",
            credits = listOf(
                UIText.StringResource(R.string.credits_icon)
            ),
            website = "https://www.flaticon.com/authors/creative-stall-premium"
        ),
        Credit(
            name = "Gajah Mada",
            source = "Flaticon",
            credits = listOf(
                UIText.StringResource(R.string.credits_icon)
            ),
            website = "https://www.flaticon.com/authors/gajah-mada"
        ),
        Credit(
            name = "mavadee",
            source = "Flaticon",
            credits = listOf(
                UIText.StringResource(R.string.credits_icon)
            ),
            website = "https://www.flaticon.com/authors/mavadee"
        ),
    )

    // Fonts for Reader
    val FONTS = listOf(
        FontWithName(
            "default",
            UIText.StringResource(R.string.default_font),
            FontFamily.Default
        ),
        FontWithName(
            "raleway",
            UIText.StringValue("Raleway"),
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
            "open_sans",
            UIText.StringValue("Open Sans"),
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
            "mulish",
            UIText.StringValue("Mulish"),
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
            "arimo",
            UIText.StringValue("Arimo"),
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
            "garamond",
            UIText.StringValue("Garamond"),
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
            "roboto_serif",
            UIText.StringValue("Roboto Serif"),
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
            "noto_serif",
            UIText.StringValue("Noto Serif"),
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
            "noto_sans",
            UIText.StringValue("Noto Sans"),
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
            "roboto",
            UIText.StringValue("Roboto"),
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
            "jost",
            UIText.StringValue("Jost"),
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

    val EMPTY_BOOK = Book(
        -1,
        "",
        UIText.StringValue(""),
        null,
        "",
        emptyList(),
        0,
        0,
        0,
        0,
        0f,
        null,
        "",
        null,
        Category.READING,
        null
    )
}