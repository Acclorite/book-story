package ua.acclorite.book_story.presentation.screens.help.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.ui.SlidingTransition

@Composable
fun LazyItemScope.HelpItem(
    title: String,
    description: AnnotatedString,
    customContent: @Composable ColumnScope.() -> Unit = {},
    tags: List<String>,
    shouldShowDescription: Boolean,
    onTitleClick: () -> Unit,
    onTagClick: (String) -> Unit
) {
    val animatedArrowRotation by animateFloatAsState(
        targetValue = if (shouldShowDescription) 0f else -180f,
        animationSpec = tween(300),
        label = stringResource(id = R.string.arrow_anim_content_desc)
    )


    Column(Modifier.animateItem()) {
        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(interactionSource = null, indication = null) {
                    onTitleClick()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
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
            visible = shouldShowDescription
        ) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                ClickableText(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) { offset ->
                    tags.forEach { tag ->
                        description.getStringAnnotations(tag = tag, start = offset, end = offset)
                            .firstOrNull()?.let {
                                onTagClick(tag)
                            }
                    }
                }
                customContent()
            }
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )
    }
}