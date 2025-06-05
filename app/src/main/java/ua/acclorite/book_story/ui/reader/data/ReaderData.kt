/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.reader.data

import androidx.annotation.FontRes
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import kotlinx.collections.immutable.persistentListOf
import ua.acclorite.book_story.R
import ua.acclorite.book_story.core.ui.UIText
import ua.acclorite.book_story.ui.reader.model.FontWithName

object ReaderData {
    val fonts = persistentListOf(
        FontWithName(
            "default",
            UIText.StringResource(R.string.default_string),
            FontFamily.Default
        ),
        FontWithName(
            "raleway",
            UIText.StringValue("Raleway"),
            provideVariableFontFamily(
                normalResId = R.font.raleway_variable,
                italicResId = R.font.raleway_variable_italic
            )
        ),
        FontWithName(
            "open_sans",
            UIText.StringValue("Open Sans"),
            provideVariableFontFamily(
                normalResId = R.font.opensans_variable,
                italicResId = R.font.opensans_variable_italic
            )
        ),
        FontWithName(
            "mulish",
            UIText.StringValue("Mulish"),
            provideVariableFontFamily(
                normalResId = R.font.mulish_variable,
                italicResId = R.font.mulish_variable_italic
            )
        ),
        FontWithName(
            "arimo",
            UIText.StringValue("Arimo"),
            provideVariableFontFamily(
                normalResId = R.font.arimo_variable,
                italicResId = R.font.arimo_variable_italic
            )
        ),
        FontWithName(
            "garamond",
            UIText.StringValue("Garamond"),
            provideVariableFontFamily(
                normalResId = R.font.garamond_variable,
                italicResId = R.font.garamond_variable_italic
            )
        ),
        FontWithName(
            "roboto_serif",
            UIText.StringValue("Roboto Serif"),
            provideVariableFontFamily(
                normalResId = R.font.robotoserif_variable,
                italicResId = R.font.robotoserif_variable_italic
            )
        ),
        FontWithName(
            "noto_serif",
            UIText.StringValue("Noto Serif"),
            provideVariableFontFamily(
                normalResId = R.font.notoserif_variable,
                italicResId = R.font.notoserif_variable_italic
            )
        ),
        FontWithName(
            "noto_sans",
            UIText.StringValue("Noto Sans"),
            provideVariableFontFamily(
                normalResId = R.font.notosans_variable,
                italicResId = R.font.notosans_variable_italic
            )
        ),
        FontWithName(
            "roboto",
            UIText.StringValue("Roboto"),
            provideVariableFontFamily(
                normalResId = R.font.roboto_variable_with_italic,
                italicResId = R.font.roboto_variable_with_italic
            )
        ),
        FontWithName(
            "jost",
            UIText.StringValue("Jost"),
            provideVariableFontFamily(
                normalResId = R.font.jost_variable,
                italicResId = R.font.jost_variable_italic
            )
        ),
        FontWithName(
            "merriweather",
            UIText.StringValue("Merriweather"),
            provideVariableFontFamily(
                normalResId = R.font.merriweather_variable,
                italicResId = R.font.merriweather_variable_italic
            )
        ),
        FontWithName(
            "montserrat",
            UIText.StringValue("Montserrat"),
            provideVariableFontFamily(
                normalResId = R.font.montserrat_variable,
                italicResId = R.font.montserrat_variable_italic
            )
        ),
        FontWithName(
            "nunito",
            UIText.StringValue("Nunito"),
            provideVariableFontFamily(
                normalResId = R.font.nunito_variable,
                italicResId = R.font.nunito_variable_italic
            )
        ),
        FontWithName(
            "roboto_slab",
            UIText.StringValue("Roboto Slab"),
            provideVariableFontFamily(
                normalResId = R.font.robotoslab_variable_with_italic,
                italicResId = R.font.robotoslab_variable_with_italic
            )
        ),
        FontWithName(
            "lora",
            UIText.StringValue("Lora"),
            provideVariableFontFamily(
                normalResId = R.font.lora_variable,
                italicResId = R.font.lora_variable_italic
            )
        ),
        FontWithName(
            "open_dyslexic",
            UIText.StringValue("Open Dyslexic"),
            FontFamily(
                Font(R.font.open_dyslexic_regular),
                Font(R.font.open_dyslexic_italic, style = FontStyle.Italic),
                Font(R.font.open_dyslexic_bold, weight = FontWeight.Bold)
            )
        )
    )
}

@OptIn(ExperimentalTextApi::class)
private fun provideVariableFontFamily(
    @FontRes normalResId: Int,
    @FontRes italicResId: Int
): FontFamily {
    fun provideVariableFont(weight: FontWeight): Array<Font> {
        return arrayOf(
            Font(
                resId = normalResId,
                weight = weight,
                style = FontStyle.Normal,
                variationSettings = FontVariation.Settings(
                    weight = weight,
                    style = FontStyle.Normal
                )
            ),
            Font(
                resId = italicResId,
                weight = weight,
                style = FontStyle.Italic,
                variationSettings = FontVariation.Settings(
                    weight = weight,
                    style = FontStyle.Italic
                )
            )
        )
    }

    return FontFamily(
        *provideVariableFont(weight = FontWeight.W100),
        *provideVariableFont(weight = FontWeight.W200),
        *provideVariableFont(weight = FontWeight.W300),
        *provideVariableFont(weight = FontWeight.W400),
        *provideVariableFont(weight = FontWeight.W500),
        *provideVariableFont(weight = FontWeight.W600),
        *provideVariableFont(weight = FontWeight.W700),
        *provideVariableFont(weight = FontWeight.W800),
        *provideVariableFont(weight = FontWeight.W900)
    )
}