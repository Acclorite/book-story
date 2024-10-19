package ua.acclorite.book_story.presentation.core.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R

/**
 * Checkbox. Has a Circle shape.
 *
 * @param modifier Modifier.
 * @param selected Whether checked or not.
 * @param containerColor Container color.
 * @param size Size of the checkbox icon(not the whole checkbox).
 */
@Composable
fun CircularCheckbox(
    modifier: Modifier = Modifier,
    selected: Boolean,
    containerColor: Color,
    size: Dp = 22.dp
) {
    Icon(
        imageVector = Icons.Default.Check,
        tint = if (selected) containerColor else Color.Transparent,
        modifier = modifier
            .border(
                width = if (!selected) 1.dp else 0.dp,
                if (!selected) MaterialTheme.colorScheme.outline else Color.Transparent,
                CircleShape
            )
            .background(
                if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = CircleShape
            )
            .padding(4.dp)
            .size(size),
        contentDescription = stringResource(id = R.string.checkbox_content_desc)
    )
}