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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.presentation.components.CategoryTitle
import kotlin.math.roundToInt

@Composable
fun SliderWithTitle(
    modifier: Modifier = Modifier,
    value: Pair<Int, String>,
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
                text = "${value.first}${value.second}",
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
            steps = toValue - fromValue
        )
    }
}

@Composable
fun SliderWithTitle(
    modifier: Modifier = Modifier,
    value: Pair<Float, String>,
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
                text = "${(value.first * toValue).roundToInt()}${value.second}",
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
            }
        )
    }
}