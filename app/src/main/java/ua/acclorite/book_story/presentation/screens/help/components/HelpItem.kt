package ua.acclorite.book_story.presentation.screens.help.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.HelpTip
import ua.acclorite.book_story.presentation.core.navigation.LocalOnNavigate
import ua.acclorite.book_story.presentation.core.util.noRippleClickable
import ua.acclorite.book_story.presentation.ui.ExpandingTransition

/**
 * Help Item.
 * Can be Expanded or Collapsed.
 *
 * @param helpTip [HelpTip] to be shown.
 * @param fromStart Whether user came from Start screen.
 */
@Composable
fun HelpItem(
    helpTip: HelpTip,
    fromStart: Boolean
) {
    val onNavigate = LocalOnNavigate.current

    val showDescription = rememberSaveable {
        mutableStateOf(false)
    }

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

    Column(
        Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.extraLarge)
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .background(
                animatedBackgroundColor.value,
                MaterialTheme.shapes.extraLarge
            )
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
                        helpTip.description.invoke(
                            this@buildAnnotatedString,
                            onNavigate,
                            fromStart
                        )
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}