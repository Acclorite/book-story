package ua.acclorite.book_story.presentation.screens.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.CategoryTitle

/**
 * Color picker with title.
 */
@Composable
fun ColorPickerWithTitle(
    value: Color,
    title: String,
    horizontalPadding: Dp = 18.dp,
    verticalPadding: Dp = 8.dp,
    onValueChange: (Color) -> Unit
) {
    val initialValue = remember { value }
    var color by remember { mutableStateOf(value) }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = verticalPadding, horizontal = horizontalPadding)
    ) {
        CategoryTitle(
            title = title,
            padding = 0.dp,
//            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        RevertibleSlider(
            value = color.red to "",
            initialValue = initialValue.red,
            title = stringResource(id = R.string.red_color),
            onValueChange = {
                color = color.copy(red = it)
                onValueChange(color)
            }
        )
        RevertibleSlider(
            value = color.green to "",
            initialValue = initialValue.green,
            title = stringResource(id = R.string.green_color),
            onValueChange = {
                color = color.copy(green = it)
                onValueChange(color)
            }
        )
        RevertibleSlider(
            value = color.blue to "",
            initialValue = initialValue.blue,
            title = stringResource(id = R.string.blue_color),
            onValueChange = {
                color = color.copy(blue = it)
                onValueChange(color)
            }
        )
    }
}

@Composable
private fun RevertibleSlider(
    value: Pair<Float, String>,
    initialValue: Float,
    title: String,
    horizontalPadding: Dp = 0.dp,
    verticalPadding: Dp = 8.dp,
    onValueChange: (Float) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(
            horizontal = horizontalPadding,
            vertical = verticalPadding
        )
    ) {
        SliderWithTitle(
            modifier = Modifier.weight(0.85f),
            value = value,
            title = title,
            toValue = 255,
            onValueChange = {
                onValueChange(it)
            },
            horizontalPadding = 0.dp,
            verticalPadding = 0.dp
        )
        Box(modifier = Modifier.weight(0.15f)) {
            IconButton(
                modifier = Modifier.align(Alignment.CenterEnd),
                enabled = initialValue != value.first,
                onClick = { onValueChange(initialValue) }
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "Revert to initial",
                    modifier = Modifier
                        .size(28.dp),
                    tint = if (initialValue == value.first) MaterialTheme.colorScheme.onSurfaceVariant
                    else MaterialTheme.colorScheme.onSurface
                )
            }

        }
    }
}
