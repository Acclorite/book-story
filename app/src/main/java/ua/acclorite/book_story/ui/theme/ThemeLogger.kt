@file:Suppress("unused")

package ua.acclorite.book_story.ui.theme

import android.util.Log
import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

private const val THEME = "THEME"

/**
 * This function logs all the colors of the colorScheme.
 * You can use it to extract colors from Material You theme and add it to the app.
 * Sadly, Material Theme Builder (both figma + site) does not provide many colors,
 * that's why I have to use Material You theme on the device itself
 * (which provides tons of different colors and presets of those colors)
 * to generate and add new themes.
 *
 * @param darkTheme Whether the theme is in dark mode.
 * Changes variable suffix to "Dark" if true, "Light" otherwise.
 */
fun ColorScheme.logTheme(darkTheme: Boolean): ColorScheme {
    val suffix = if (darkTheme) "Dark" else "Light"

    fun Color.convertToVal(variableName: String): String {
        return "private val $variableName$suffix = Color(0x${this.convertToHex()})\n"
    }

    Log.i(
        THEME,
        "Theme $suffix:\n" +
                "------------------------\n" +
                primary.convertToVal("primary") +
                onPrimary.convertToVal("onPrimary") +
                primaryContainer.convertToVal("primaryContainer") +
                onPrimaryContainer.convertToVal("onPrimaryContainer") +
                secondary.convertToVal("secondary") +
                onSecondary.convertToVal("onSecondary") +
                secondaryContainer.convertToVal("secondaryContainer") +
                onSecondaryContainer.convertToVal("onSecondaryContainer") +
                tertiary.convertToVal("tertiary") +
                onTertiary.convertToVal("onTertiary") +
                tertiaryContainer.convertToVal("tertiaryContainer") +
                onTertiaryContainer.convertToVal("onTertiaryContainer") +
                error.convertToVal("error") +
                onError.convertToVal("onError") +
                errorContainer.convertToVal("errorContainer") +
                onErrorContainer.convertToVal("onErrorContainer") +
                background.convertToVal("background") +
                onBackground.convertToVal("onBackground") +
                surface.convertToVal("surface") +
                onSurface.convertToVal("onSurface") +
                surfaceVariant.convertToVal("surfaceVariant") +
                onSurfaceVariant.convertToVal("onSurfaceVariant") +
                outline.convertToVal("outline") +
                outlineVariant.convertToVal("outlineVariant") +
                scrim.convertToVal("scrim") +
                inverseSurface.convertToVal("inverseSurface") +
                inverseOnSurface.convertToVal("inverseOnSurface") +
                inversePrimary.convertToVal("inversePrimary") +
                surfaceDim.convertToVal("surfaceDim") +
                surfaceBright.convertToVal("surfaceBright") +
                surfaceContainerLowest.convertToVal("surfaceContainerLowest") +
                surfaceContainerLow.convertToVal("surfaceContainerLow") +
                surfaceContainer.convertToVal("surfaceContainer") +
                surfaceContainerHigh.convertToVal("surfaceContainerHigh") +
                surfaceContainerHighest.convertToVal("surfaceContainerHighest") +
                "------------------------"
    )

    return this
}

@OptIn(ExperimentalStdlibApi::class)
private fun Color.convertToHex(): String {
    return toArgb().toHexString(format = HexFormat.UpperCase)
}