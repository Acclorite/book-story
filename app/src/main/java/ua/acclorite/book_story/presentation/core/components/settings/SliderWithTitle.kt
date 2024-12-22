package ua.acclorite.book_story.presentation.core.components.settings

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Label
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.presentation.settings.components.SettingsSubcategoryTitle
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
@OptIn(ExperimentalMaterial3Api::class)
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
    val interactionSource = remember { MutableInteractionSource() }
    val placeholder = remember(showPlaceholder, value, valuePlaceholder) {
        derivedStateOf {
            if (!showPlaceholder) "${value.first}${value.second}"
            else valuePlaceholder
        }
    }

    Row(
        modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.fillMaxWidth(0.2f)) {
            SettingsSubcategoryTitle(title = title, padding = 0.dp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = placeholder.value,
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
            interactionSource = interactionSource,
            thumb = {
                Label(
                    label = {
                        PlainTooltip(
                            modifier = Modifier.clip(CircleShape)
                        ) {
                            Text(
                                text = placeholder.value,
                                modifier = Modifier.padding(
                                    vertical = 6.dp,
                                    horizontal = 4.dp
                                )
                            )
                        }
                    },
                    interactionSource = interactionSource
                ) {
                    SliderDefaults.Thumb(
                        interactionSource = interactionSource
                    )
                }
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
@OptIn(ExperimentalMaterial3Api::class)
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
    val interactionSource = remember { MutableInteractionSource() }
    val placeholder = remember(showPlaceholder, value, toValue, valuePlaceholder) {
        derivedStateOf {
            if (!showPlaceholder) "${(value.first * toValue).roundToInt()}${value.second}"
            else valuePlaceholder
        }
    }

    Row(
        modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.fillMaxWidth(0.2f)) {
            SettingsSubcategoryTitle(title = title, padding = 0.dp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = placeholder.value,
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
            interactionSource = interactionSource,
            thumb = {
                Label(
                    label = {
                        PlainTooltip(
                            modifier = Modifier.clip(CircleShape)
                        ) {
                            Text(
                                text = placeholder.value,
                                modifier = Modifier.padding(
                                    vertical = 6.dp,
                                    horizontal = 4.dp
                                )
                            )
                        }
                    },
                    interactionSource = interactionSource
                ) {
                    SliderDefaults.Thumb(
                        interactionSource = interactionSource
                    )
                }
            },
            colors = SliderDefaults.colors(
                activeTickColor = MaterialTheme.colorScheme.onPrimary,
                inactiveTickColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}