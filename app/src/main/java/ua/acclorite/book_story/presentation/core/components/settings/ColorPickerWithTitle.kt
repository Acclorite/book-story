package ua.acclorite.book_story.presentation.core.components.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.common.IconButton
import ua.acclorite.book_story.presentation.settings.components.SettingsSubcategoryTitle

@OptIn(FlowPreview::class)
@Composable
fun ColorPickerWithTitle(
    modifier: Modifier = Modifier,
    value: Color,
    presetId: Int,
    title: String,
    horizontalPadding: Dp = 18.dp,
    verticalPadding: Dp = 8.dp,
    onValueChange: (Color) -> Unit
) {
    val initialValue = rememberSaveable(presetId) { value.value.toString() }
    var color by remember(value) { mutableStateOf(value) }

    LaunchedEffect(color) {
        snapshotFlow {
            color
        }.debounce(50).collectLatest {
            onValueChange(it)
        }
    }

    Column(
        modifier
            .fillMaxWidth()
            .padding(vertical = verticalPadding, horizontal = horizontalPadding)
    ) {
        SettingsSubcategoryTitle(
            title = title,
            padding = 0.dp
        )
        Spacer(modifier = Modifier.height(8.dp))
        RevertibleSlider(
            value = color.red to "",
            initialValue = Color(initialValue.toULong()).red,
            title = stringResource(id = R.string.red_color),
            onValueChange = {
                color = color.copy(red = it)
            }
        )
        RevertibleSlider(
            value = color.green to "",
            initialValue = Color(initialValue.toULong()).green,
            title = stringResource(id = R.string.green_color),
            onValueChange = {
                color = color.copy(green = it)
            }
        )
        RevertibleSlider(
            value = color.blue to "",
            initialValue = Color(initialValue.toULong()).blue,
            title = stringResource(id = R.string.blue_color),
            onValueChange = {
                color = color.copy(blue = it)
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
            modifier = Modifier.weight(1f),
            value = value,
            title = title,
            toValue = 255,
            onValueChange = {
                onValueChange(it)
            },
            horizontalPadding = 0.dp,
            verticalPadding = 0.dp
        )

        Spacer(modifier = Modifier.width(10.dp))
        IconButton(
            modifier = Modifier.size(28.dp),
            icon = Icons.Default.History,
            contentDescription = R.string.revert_content_desc,
            disableOnClick = false,
            enabled = initialValue != value.first,
            color = if (initialValue == value.first) MaterialTheme.colorScheme.onSurfaceVariant
            else MaterialTheme.colorScheme.onSurface
        ) {
            onValueChange(initialValue)
        }
    }
}