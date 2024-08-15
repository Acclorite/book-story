package ua.acclorite.book_story.presentation.screens.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.presentation.components.CategoryTitle
import kotlin.math.roundToInt

/**
 * Slider with title.
 *
 * @param modifier Modifier.
 * @param value Value to be displayed, in pair with [String] which is the type of value(e.g. "pt")
 * @param valuePlaceholder Placeholder of the value(e.g. "Default", instead of the "1pt")
 * @param showPlaceholder Whether placeholder should be shown.
 * @param fromValue From where user can pick value.
 * @param toValue To where user can pick value.
 * @param title Title of this slider.
 * @param horizontalPadding Horizontal item padding.
 * @param verticalPadding Vertical item padding.
 * @param onValueChange Callback when value changes.
 */
@Composable
fun SliderWithTitle(
    modifier: Modifier = Modifier,
    value: Pair<Int, String>,
    valuePlaceholder: String = "",
    showPlaceholder: Boolean = false,
    fromValue: Int,
    toValue: Int,
    title: String,
    horizontalPadding: Dp = 18.dp,
    verticalPadding: Dp = 8.dp,
    onValueChange: (Int) -> Unit
) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.fillMaxWidth(0.2f)) {
            CategoryTitle(title = title, padding = 0.dp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (!showPlaceholder) "${value.first}${value.second}"
                else valuePlaceholder,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Slider(
            valueRange = fromValue.toFloat()..toValue.toFloat(),
            value = value.first.toFloat(),
            onValueChange = {
                onValueChange(it.roundToInt())
            },
            steps = toValue - fromValue - 1,
            colors = SliderDefaults.colors(
                activeTickColor = MaterialTheme.colorScheme.onPrimary,
                inactiveTickColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

/**
 * Slider with title.
 *
 * @param modifier Modifier.
 * @param value Value to be displayed, in pair with [String] which is the type of value(e.g. "pt")
 * @param valuePlaceholder Placeholder of the value(e.g. "Default", instead of the "1pt")
 * @param showPlaceholder Whether placeholder should be shown.
 * @param toValue To where user can pick value.
 * @param title Title of this slider.
 * @param horizontalPadding Horizontal item padding.
 * @param verticalPadding Vertical item padding.
 * @param onValueChange Callback when value changes.
 */
@Composable
fun SliderWithTitle(
    modifier: Modifier = Modifier,
    value: Pair<Float, String>,
    valuePlaceholder: String = "",
    showPlaceholder: Boolean = false,
    title: String,
    toValue: Int,
    horizontalPadding: Dp = 18.dp,
    verticalPadding: Dp = 8.dp,
    onValueChange: (Float) -> Unit
) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.fillMaxWidth(0.2f)) {
            CategoryTitle(title = title, padding = 0.dp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (!showPlaceholder) "${(value.first * toValue).roundToInt()}${value.second}"
                else valuePlaceholder,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Slider(
            valueRange = 0f..1f,
            value = value.first,
            onValueChange = {
                onValueChange(it)
            },
            colors = SliderDefaults.colors(
                activeTickColor = MaterialTheme.colorScheme.onPrimary,
                inactiveTickColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}