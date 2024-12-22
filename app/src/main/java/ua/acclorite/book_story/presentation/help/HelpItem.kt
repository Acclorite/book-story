package ua.acclorite.book_story.presentation.help

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.help.HelpTip
import ua.acclorite.book_story.domain.util.Position
import ua.acclorite.book_story.presentation.core.util.noRippleClickable
import ua.acclorite.book_story.ui.theme.ExpandingTransition

@Composable
fun HelpItem(
    helpTip: HelpTip,
    position: Position
) {
    val showDescription = rememberSaveable { mutableStateOf(false) }

    val animatedArrowRotation = animateFloatAsState(
        targetValue = if (showDescription.value) 0f else -180f,
        animationSpec = tween(400)
    )
    val animatedBackgroundColor = animateColorAsState(
        targetValue = if (showDescription.value) {
            MaterialTheme.colorScheme.surfaceContainerHigh
        } else MaterialTheme.colorScheme.surfaceContainerLow,
        animationSpec = tween(400)
    )
    val animatedArrowContainerColor = animateColorAsState(
        targetValue = if (showDescription.value) {
            MaterialTheme.colorScheme.surfaceContainerHighest
        } else MaterialTheme.colorScheme.surfaceContainer,
        animationSpec = tween(400)
    )

    val extraLargeShape = MaterialTheme.shapes.extraLarge
    val shape = remember(position) {
        when (position) {
            Position.TOP -> extraLargeShape.copy(
                bottomStart = CornerSize(3.dp),
                bottomEnd = CornerSize(3.dp)
            )

            Position.CENTER -> RoundedCornerShape(3.dp)

            Position.SOLO -> extraLargeShape

            Position.BOTTOM -> extraLargeShape.copy(
                topStart = CornerSize(3.dp),
                topEnd = CornerSize(3.dp)
            )
        }
    }
    val paddingValues = remember(position) {
        when (position) {
            Position.TOP -> PaddingValues(top = 4.dp, bottom = 1.dp)
            Position.CENTER -> PaddingValues(vertical = 1.dp)
            Position.SOLO -> PaddingValues(0.dp)
            Position.BOTTOM -> PaddingValues(bottom = 4.dp, top = 1.dp)
        }
    }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(paddingValues)
            .clip(shape)
            .background(animatedBackgroundColor.value)
            .padding(vertical = 18.dp, horizontal = 18.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .noRippleClickable {
                    showDescription.value = !showDescription.value
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = helpTip.title),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Outlined.ArrowDropUp,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.large)
                    .background(
                        animatedArrowContainerColor.value,
                        MaterialTheme.shapes.large
                    )
                    .padding(8.dp)
                    .size(24.dp)
                    .rotate(animatedArrowRotation.value),
                contentDescription = stringResource(id = R.string.arrow_content_desc)
            )
        }

        ExpandingTransition(
            visible = showDescription.value
        ) {
            Column(Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(9.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(9.dp))
                Text(
                    text = buildAnnotatedString {
                        helpTip.description.invoke(this@buildAnnotatedString)
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}