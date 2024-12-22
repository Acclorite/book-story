package ua.acclorite.book_story.ui.theme.color

import android.annotation.SuppressLint
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext

@SuppressLint("NewApi")
@Composable
fun dynamicTheme(isDark: Boolean): ColorScheme {
    return if (isDark) {
        dynamicDarkColorScheme(LocalContext.current).run {
            copy(
                surface = surface.desaturateColor(
                    saturation = 0.73f,
                    lightness = 0.86f
                ),
                surfaceContainerLowest = surfaceContainerLowest.desaturateColor(
                    saturation = 0.5f,
                    lightness = 0.7f
                ),
                surfaceContainerLow = surfaceContainerLow.desaturateColor(
                    saturation = 0.9f,
                    lightness = 1f
                ),
                surfaceContainer = surfaceContainer.desaturateColor(
                    saturation = 0.9f,
                    lightness = 1f
                ),
                surfaceContainerHigh = surfaceContainerHigh.desaturateColor(
                    saturation = 0.9f,
                    lightness = 1f
                )
            )
        }
    } else {
        dynamicLightColorScheme(LocalContext.current)
    }
}

/**
 * Desaturates MaterialYou dynamic colors.
 * Sometimes they are too colorful, which results in ugly background color.
 */
private fun Color.desaturateColor(saturation: Float, lightness: Float): Color {
    val hsl = FloatArray(3)
    android.graphics.Color.colorToHSV(toArgb(), hsl)

    hsl[1] = hsl[1] * saturation
    hsl[2] = hsl[2] * lightness

    return Color(android.graphics.Color.HSVToColor(hsl))
}