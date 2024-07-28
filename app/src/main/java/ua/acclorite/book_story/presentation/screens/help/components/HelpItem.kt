package ua.acclorite.book_story.presentation.screens.help.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.HelpTip
import ua.acclorite.book_story.domain.util.OnNavigate
import ua.acclorite.book_story.presentation.screens.help.data.HelpEvent
import ua.acclorite.book_story.presentation.ui.SlidingTransition

/**
 * Help Item.
 * Can be Expanded or Collapsed.
 *
 * @param helpTip [HelpTip] to be shown.
 * @param onNavigate Navigator callback.
 * @param fromStart Whether user came from Start screen.
 * @param onHelpEvent [HelpEvent] callback.
 */
@Composable
fun LazyItemScope.HelpItem(
    helpTip: HelpTip,
    onNavigate: OnNavigate,
    fromStart: Boolean,
    onHelpEvent: (HelpEvent) -> Unit
) {
    val showDescription = rememberSaveable {
        mutableStateOf(false)
    }
    val animatedArrowRotation by animateFloatAsState(
        targetValue = if (showDescription.value) 0f else -180f,
        animationSpec = tween(300),
        label = stringResource(id = R.string.arrow_anim_content_desc)
    )

    Column(
        Modifier
            .animateItem()
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(interactionSource = null, indication = null) {
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
                    .size(24.dp)
                    .rotate(animatedArrowRotation),
                contentDescription = stringResource(id = R.string.arrow_content_desc)
            )
        }

        SlidingTransition(
            visible = showDescription.value
        ) {
            Column(Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(16.dp))
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
                helpTip.customContent.invoke(
                    this,
                    onHelpEvent
                )
            }
        }
    }
}