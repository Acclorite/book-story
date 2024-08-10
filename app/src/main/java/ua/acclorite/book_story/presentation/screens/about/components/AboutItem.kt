package ua.acclorite.book_story.presentation.screens.about.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.acclorite.book_story.presentation.ui.FadeTransitionPreservingSpace

/**
 * About Screen Item.
 * Clickable item, that has title, description and loading state.
 *
 * @param modifier [Modifier].
 * @param title Title of the item.
 * @param description Description of the item.
 * @param verticalPadding Vertical item padding.
 * @param showLoading Whether loading indicator is shown.
 * @param isOnClickEnabled Whether this item is clickable.
 * @param onClick OnClick callback.
 */
@Composable
fun AboutItem(
    modifier: Modifier = Modifier,
    title: String,
    description: AnnotatedString?,
    verticalPadding: Dp = 12.dp,
    showLoading: Boolean = false,
    isOnClickEnabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    Row(
        modifier
            .fillMaxWidth()
            .clickable(enabled = isOnClickEnabled) {
                onClick()
            }
            .padding(horizontal = 18.dp, vertical = verticalPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp
            )
            description?.let {
                Text(
                    it,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        FadeTransitionPreservingSpace(visible = showLoading) {
            Row {
                Spacer(modifier = Modifier.width(18.dp))
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    strokeCap = StrokeCap.Round,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}