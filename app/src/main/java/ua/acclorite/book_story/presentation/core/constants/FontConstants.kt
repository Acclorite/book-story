package ua.acclorite.book_story.presentation.core.constants

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.reader.FontWithName
import ua.acclorite.book_story.domain.ui.UIText

fun Constants.provideFonts(withRandom: Boolean): List<FontWithName> {
    return mutableListOf(
        FontWithName(
            "default",
            UIText.StringResource(R.string.default_string),
            FontFamily.Default
        ),
        FontWithName(
            "raleway",
            UIText.StringValue("Raleway"),
            FontFamily(
                Font(R.font.raleway_regular),
                Font(R.font.raleway_regular_italic, style = FontStyle.Italic),
                Font(R.font.raleway_medium, weight = FontWeight.Medium),
                Font(R.font.raleway_semibold, weight = FontWeight.SemiBold),
                Font(R.font.raleway_bold, weight = FontWeight.Bold),
            )
        ),
        FontWithName(
            "open_sans",
            UIText.StringValue("Open Sans"),
            FontFamily(
                Font(R.font.opensans_regular),
                Font(R.font.opensans_regular_italic, style = FontStyle.Italic),
                Font(R.font.opensans_medium, weight = FontWeight.Medium),
                Font(R.font.opensans_semibold, weight = FontWeight.SemiBold),
                Font(R.font.opensans_bold, weight = FontWeight.Bold),
            )
        ),
        FontWithName(
            "mulish",
            UIText.StringValue("Mulish"),
            FontFamily(
                Font(R.font.mulish_regular),
                Font(R.font.mulish_regular_italic, style = FontStyle.Italic),
                Font(R.font.mulish_medium, weight = FontWeight.Medium),
                Font(R.font.mulish_semibold, weight = FontWeight.SemiBold),
                Font(R.font.mulish_bold, weight = FontWeight.Bold),
            )
        ),
        FontWithName(
            "arimo",
            UIText.StringValue("Arimo"),
            FontFamily(
                Font(R.font.arimo_regular),
                Font(R.font.arimo_regular_italic, style = FontStyle.Italic),
                Font(R.font.arimo_medium, weight = FontWeight.Medium),
                Font(R.font.arimo_semibold, weight = FontWeight.SemiBold),
                Font(R.font.arimo_bold, weight = FontWeight.Bold),
            )
        ),
        FontWithName(
            "garamond",
            UIText.StringValue("Garamond"),
            FontFamily(
                Font(R.font.garamond_regular),
                Font(R.font.garamond_regular_italic, style = FontStyle.Italic),
                Font(R.font.garamond_medium, weight = FontWeight.Medium),
                Font(R.font.garamond_semibold, weight = FontWeight.SemiBold),
                Font(R.font.garamond_bold, weight = FontWeight.Bold),
            )
        ),
        FontWithName(
            "roboto_serif",
            UIText.StringValue("Roboto Serif"),
            FontFamily(
                Font(R.font.robotoserif_regular),
                Font(R.font.robotoserif_regular_italic, style = FontStyle.Italic),
                Font(R.font.robotoserif_medium, weight = FontWeight.Medium),
                Font(R.font.robotoserif_semibold, weight = FontWeight.SemiBold),
                Font(R.font.robotoserif_bold, weight = FontWeight.Bold),
            )
        ),
        FontWithName(
            "noto_serif",
            UIText.StringValue("Noto Serif"),
            FontFamily(
                Font(R.font.notoserif_regular),
                Font(R.font.notoserif_regular_italic, style = FontStyle.Italic),
                Font(R.font.notoserif_medium, weight = FontWeight.Medium),
                Font(R.font.notoserif_semibold, weight = FontWeight.SemiBold),
                Font(R.font.notoserif_bold, weight = FontWeight.Bold),
            )
        ),
        FontWithName(
            "noto_sans",
            UIText.StringValue("Noto Sans"),
            FontFamily(
                Font(R.font.notosans_regular),
                Font(R.font.notosans_regular_italic, style = FontStyle.Italic),
                Font(R.font.notosans_medium, weight = FontWeight.Medium),
                Font(R.font.notosans_semibold, weight = FontWeight.SemiBold),
                Font(R.font.notosans_bold, weight = FontWeight.Bold),
            )
        ),
        FontWithName(
            "roboto",
            UIText.StringValue("Roboto"),
            FontFamily(
                Font(R.font.roboto_regular),
                Font(R.font.roboto_regular_italic, style = FontStyle.Italic),
                Font(R.font.roboto_medium, weight = FontWeight.Medium),
                Font(R.font.roboto_bold, weight = FontWeight.Bold),
            )
        ),
        FontWithName(
            "jost",
            UIText.StringValue("Jost"),
            FontFamily(
                Font(R.font.jost_regular),
                Font(R.font.jost_regular_italic, style = FontStyle.Italic),
                Font(R.font.jost_medium, weight = FontWeight.Medium),
                Font(R.font.jost_semibold, weight = FontWeight.SemiBold),
                Font(R.font.jost_bold, weight = FontWeight.Bold),
            )
        ),
        FontWithName(
            "merriweather",
            UIText.StringValue("Merriweather"),
            FontFamily(
                Font(R.font.merriweather_regular),
                Font(R.font.merriweather_regular_italic, style = FontStyle.Italic),
                Font(R.font.merriweather_medium, weight = FontWeight.Medium),
                Font(R.font.merriweather_bold, weight = FontWeight.Bold),
            )
        ),
        FontWithName(
            "montserrat",
            UIText.StringValue("Montserrat"),
            FontFamily(
                Font(R.font.montserrat_regular),
                Font(R.font.montserrat_regular_italic, style = FontStyle.Italic),
                Font(R.font.montserrat_medium, weight = FontWeight.Medium),
                Font(R.font.montserrat_semibold, weight = FontWeight.SemiBold),
                Font(R.font.montserrat_bold, weight = FontWeight.Bold),
            )
        ),
        FontWithName(
            "nunito",
            UIText.StringValue("Nunito"),
            FontFamily(
                Font(R.font.nunito_regular),
                Font(R.font.nunito_regular_italic, style = FontStyle.Italic),
                Font(R.font.nunito_medium, weight = FontWeight.Medium),
                Font(R.font.nunito_semibold, weight = FontWeight.SemiBold),
                Font(R.font.nunito_bold, weight = FontWeight.Bold),
            )
        ),
        FontWithName(
            "roboto_slab",
            UIText.StringValue("Roboto Slab"),
            FontFamily(
                Font(R.font.robotoslab_regular),
                Font(R.font.robotoslab_medium, weight = FontWeight.Medium),
                Font(R.font.robotoslab_semibold, weight = FontWeight.SemiBold),
                Font(R.font.robotoslab_bold, weight = FontWeight.Bold),
            )
        ),
        FontWithName(
            "lora",
            UIText.StringValue("Lora"),
            FontFamily(
                Font(R.font.lora_regular),
                Font(R.font.lora_regular_italic, style = FontStyle.Italic),
                Font(R.font.lora_medium, weight = FontWeight.Medium),
                Font(R.font.lora_semibold, weight = FontWeight.SemiBold),
                Font(R.font.lora_bold, weight = FontWeight.Bold),
            )
        )
    ).apply {
        if (withRandom) {
            add(
                1,
                FontWithName(
                    "random",
                    UIText.StringResource(R.string.random_string),
                    provideFonts(withRandom = false).random().font
                )
            )
        }
    }
}